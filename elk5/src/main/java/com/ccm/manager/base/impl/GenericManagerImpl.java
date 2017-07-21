package com.ccm.manager.base.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ccm.dao.GenericDao;
import com.ccm.manager.base.GenericManager;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>
 * To register this class in your Spring context file, use the following XML.
 * 
 * <pre>
 *     &lt;bean id="userManager" class="com.ccm.api.base.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="com.ccm.api.base.dao.hibernate.GenericDaoHibernate"&gt;
 *                 &lt;constructor-arg value="com.ccm.api.base.model.User"/&gt;
 *                 &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 * <p/>
 * <p>
 * If you're using iBATIS instead of Hibernate, use:
 * 
 * <pre>
 *     &lt;bean id="userManager" class="com.ccm.api.base.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="com.ccm.api.base.dao.ibatis.GenericDaoiBatis"&gt;
 *                 &lt;constructor-arg value="com.ccm.api.base.model.User"/&gt;
 *                 &lt;property name="dataSource" ref="dataSource"/&gt;
 *                 &lt;property name="sqlMapClient" ref="sqlMapClient"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 * 
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	// 项目配置属性
	protected Properties projectProperties;

	@Resource
	public void setProjectProperties(Properties projectProperties) {
		this.projectProperties = projectProperties;
	}

	/**
	 * GenericDao instance, set by constructor of child classes
	 */
	protected GenericDao<T, PK> dao;

	// @Autowired
	// private CompassSearchHelper compass;

	public GenericManagerImpl() {
	}

	public GenericManagerImpl(GenericDao<T, PK> genericDao) {
		this.dao = genericDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAll() {
		return dao.getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		return dao.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(PK id) {
		return dao.exists(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T object) {
		return dao.save(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(PK id) {
		dao.remove(id);
	}

	/**
	 * 逻辑删除或启用
	 */
	public void updateDelFlag(String id, boolean flag) {
		if (flag) {
			dao.softDelete(id);
		} else {
			dao.unDelete(id);
		}
	}

}
