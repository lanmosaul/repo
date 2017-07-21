package com.ccm.dao.mongo;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.ccm.dao.GenericDao;
import com.ccm.dao.mongo.Pagination;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * mongodb　基础操作类,结合部分GenericDao方法，未实现的方法如用到请自行在子类实现
 */
public class MongodbBaseDao<T> implements GenericDao<T, String>{
	@Resource
	private MongoTemplate mongoTemplate;
	
	/**
	 * 通过条件查询,查询分页结果
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	public Pagination<T> getPage(int pageNo, int pageSize, Query query) {
		Long totalCount = this.mongoTemplate.count(query, this.getEntityClass());
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount.intValue());
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<T> datas = this.find(query);
		page.setDatas(datas);
		page.setTotalCount(totalCount.intValue());
		return page;
	}

	/**
	 * 通过条件查询实体(集合)
	 * 
	 * @param query
	 */
	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}
	public List<?> find(Query query,Class<?> clz) {
		return mongoTemplate.find(query, clz);
	}
	/**返回对象的指定字段数据*/
	public List<T> find(Query query,T clz,String... resFiled) {
		List<T> list = new ArrayList<T>();
		BasicDBObject keys = new BasicDBObject();
		for (int i = 0; i < resFiled.length; i++) {
			keys.put(resFiled[i], 1);
		}
		DBCollection dbc = mongoTemplate.getCollection(mongoTemplate.getCollectionName(clz.getClass()));
		DBCursor dbcursor = dbc.find(query.getQueryObject(), keys).addOption(Bytes.QUERYOPTION_NOTIMEOUT);
		try {
			while (dbcursor.hasNext()) {
				DBObject obj = dbcursor.next();
				if (obj != null) {
					list.add(dbObject2Bean(obj, clz));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbcursor.close();
		return list;
	}
	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param query
	 * @return
	 */
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}
	public Object findOne(Query query,Class<?> cla) {
		return mongoTemplate.findOne(query, cla);
	}
	/**
	 * 查询并且修改记录
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public T findAndModify(Query query, Update update) {
		return this.mongoTemplate.findAndModify(query, update, this.getEntityClass());
	}

	/**
	 * 按条件查询,并且删除记录
	 * 
	 * @param query
	 * @return
	 */
	public void remove(Query query) {
		remove(query, this.getEntityClass());
	}
	/**
	 * 按条件查询,并且删除记录
	 * @param query Class
	 * @return
	 */
	public void remove(Query query,Class<?> clz) {
		this.mongoTemplate.remove(query, clz);
	}
	/**
	 * 通过条件查询更新第1数据
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public void updateFirst(Query query, Update update) {
		mongoTemplate.updateFirst(query, update, this.getEntityClass());
	}
	
	/**根据query更新所有*/
	public void update(Query query,Object obj) {
		mongoTemplate.updateMulti(query, buildBaseUpdate(obj), this.getEntityClass());
	}
	
	public void update(Query query, Update update) {
		 mongoTemplate.updateMulti(query, update, this.getEntityClass());
	}
	
	public void updateById(Object obj){
		Map<String,String> idMap = getObjId(obj);
		update(new Query(Criteria.where(idMap.get("name")).is(idMap.get("value"))), obj);
	}
	
	@SuppressWarnings("unchecked")
	public T saveOrUpdateById(Object obj){
		Map<String,String> idMap = getObjId(obj);
		T t = findById(idMap.get("value"));
		if(t != null){
			this.updateById(obj);
		}else{
			obj = this.save(obj);
		}
		return (T)obj;
	}
	private Map<String,String> getObjId(Object obj){
		String idValue = "";
		String idName = "";
		Map<String,String> map = new HashMap<String, String>();
		
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Id mongoId = field.getAnnotation(Id.class);
				if (mongoId != null) {
					idValue = field.get(obj)!=null ? field.get(obj).toString() : "";
					idName = field.getName();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("name", idName);
		map.put("value", idValue.toString());
		return map;
	}
	private Update buildBaseUpdate(Object t) {  
        Update update = new Update();  
        Field[] fields = t.getClass().getDeclaredFields();  
        for (Field field : fields) {
            field.setAccessible(true);
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            Id mongoId = field.getAnnotation(Id.class);
            if(isStatic || mongoId != null){//id不用更新
            	continue;
            }
            try {
                Object value = field.get(t);  
                if (value != null) {
                   update.set(field.getName(), value);  
                }  
            } catch (Exception e) {  
                e.printStackTrace();
            }
        }
        update.set("lastModifyTime",new Date());
        return update;
    }
	/**
	 * 保存一个对象到mongodb
	 * 
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T save(Object bean) {
		mongoTemplate.save(bean);
		return (T)bean;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dropEntity(Class entityClass) {
		  mongoTemplate.dropCollection(entityClass);
	}
	/**
	 * 通过ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	private T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 * 
	 * @param id
	 * @param collectionName
	 *            集合名
	 * @return
	 */
	public T findById(String id, String collectionName) {
		return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
	}
	public Object findById(String id, Class<?> cla) {
		return mongoTemplate.findById(id, cla);
	}
	 /** 
	   * 把DBObject转换成bean对象 
	   * @param dbObject 
	   * @param bean 
	   * @return 
	   * @throws IllegalAccessException 
	   * @throws InvocationTargetException 
	   * @throws NoSuchMethodException 
	   */  
	  public T dbObject2Bean(DBObject dbObject, T bean) throws Exception {  
	    if (bean == null) {  
	      return null;
	    }
//	    Class.forName("ss").n
	    Field[] fields = bean.getClass().getDeclaredFields();  
	    for (Field field : fields) {  
	      field.setAccessible(true);
	      String varName = field.getName();  
	      Object object = dbObject.get(varName);  
	      if (object != null) {  
	        BeanUtils.setProperty(bean, varName, object);  
	      }
	    }
	    return bean;  
	  }  
	/**
	 * 获取需要操作的实体类class
	 * 
	 * @return
	 */
	protected Class<T> getEntityClass(){
		return null;
	}
	public void saveALL(List<?> list) {
		mongoTemplate.insertAll(list);
	}
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	@Override
	public List<T> getAll() {
		return this.mongoTemplate.findAll(getEntityClass());
	}

	@Override
	public List<T> getAllDistinct() {
		return null;
	}

	@Override
	public T get(String id) {
		return findById(id);
	}

	@Override
	public boolean exists(String id) {
		return false;
	}

	@Override
	public void remove(String id) {
	}

	@Override
	public void softDelete(String primaryKey) {
	}

	@Override
	public void unDelete(String primaryKey) {
	}
}
