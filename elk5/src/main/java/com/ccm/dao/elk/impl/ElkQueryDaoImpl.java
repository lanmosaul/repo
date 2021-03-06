package com.ccm.dao.elk.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator.KeyedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import com.ccm.dao.elk.ElkQueryDao;
import com.ccm.model.ads.AdsMessage;
import com.ccm.util.ElkPool;

@Repository("elkQueryDao")
public class ElkQueryDaoImpl implements ElkQueryDao{
	private Log log = LogFactory.getLog(getClass());
	@Resource
	private ElkPool elkPool;
	
	public  BoolQueryBuilder initMulitQueryBuilder(){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		return queryBuilder;
	}
	
	public  BoolQueryBuilder composeEqualCondtionWithMulitQuery(BoolQueryBuilder boolQueryBuilder,Map<String,?>conditionMap,String conditionType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(boolQueryBuilder==null)
			boolQueryBuilder=initMulitQueryBuilder();
		if(conditionMap!=null&&conditionMap.size()>0){
			for (Map.Entry<String,?> entry:conditionMap.entrySet()) {
				Method m=boolQueryBuilder.getClass().getMethod(conditionType,QueryBuilder.class);
				boolQueryBuilder=(BoolQueryBuilder) m.invoke(boolQueryBuilder, QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
			}
		}
		return boolQueryBuilder;
	}
	
	public BoolQueryBuilder composeRangeCondtionWithMulitQuery(BoolQueryBuilder boolQueryBuilder,String fieldName,Map<String,?>conditionMap) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(boolQueryBuilder==null)
			boolQueryBuilder=initMulitQueryBuilder();
		RangeQueryBuilder rangeQueryBuilder =QueryBuilders.rangeQuery(fieldName);
		for (Map.Entry<String,?> entry:conditionMap.entrySet()) {
			Method m=rangeQueryBuilder.getClass().getMethod(entry.getKey(),Object.class);
			rangeQueryBuilder=(RangeQueryBuilder) m.invoke(rangeQueryBuilder,entry.getValue());
		}
		rangeQueryBuilder=rangeQueryBuilder.includeLower(true);
		rangeQueryBuilder=rangeQueryBuilder.includeUpper(true);
		boolQueryBuilder = boolQueryBuilder.must(rangeQueryBuilder);
		return	boolQueryBuilder;
	}
	
	public SearchResponse initSearchRequestBuilder(
			String indexName,String typeName,
			QueryBuilder queryBuilder,String sortField,SortOrder sortOrder,int beginIdx,int pageSize){
		TransportClient client=elkPool.getTransportClientFromPool();
		SearchRequestBuilder srb=client.prepareSearch(indexName)
 		.setTypes(typeName)
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(queryBuilder);
		 if(sortField!=null)
			 srb=srb.addSort(sortField, sortOrder);
		 SearchResponse searchResponse= srb.setFrom(beginIdx).setSize(pageSize).execute().actionGet();
		 elkPool.releaseTransportClient(client);
		 return searchResponse;
	}
	
	public QueryBuilder initSimpleQueryBuilder(String fieldName,Object fieldValue){
		return QueryBuilders.termQuery(fieldName,fieldValue);
	}
	
	public QueryBuilder initFuzzyQueryBuilder(String fieldName,String fieldValue){
		return QueryBuilders.fuzzyQuery(fieldName,fieldValue);
	}
	
	public QueryBuilder intWildCardQueryBuilder(String fieldName,String fieldValue){
		return QueryBuilders.wildcardQuery(fieldName,fieldValue);
	}
	
	public QueryBuilder initStringQueryBuilder(String fieldName,String fieldValue){
		return QueryBuilders.queryStringQuery(fieldValue).field(fieldName);
	}
	
	public QueryBuilder initMatchQueryBuilder(String fieldName,Object fieldValue){
		return QueryBuilders.matchQuery(fieldName, fieldValue);
	}
	
	//save json
	public void postObjectList(String indexName,String typeName,List<?>objectList){
		TransportClient client=elkPool.getTransportClientFromPool();	
		for(Object objectJson:objectList){
		IndexResponse response = client.prepareIndex(indexName, typeName).setSource(objectJson).get();
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// status has stored current instance statement.
		RestStatus _status = response.status();
		log.info("postJson {index="+_index+",type="+_type+",id="+_id+",status="+_status+",version="+_version+"}");
		}
	}
	
	//save json
	public void postAdsMessage(String indexName,List <AdsMessage>msgList){
		TransportClient client=elkPool.getTransportClientFromPool();	
		for(AdsMessage msg:msgList){
			Map <String,Object> msgMap=AdsMessage.convertToMap(msg);
			IndexResponse response = client.prepareIndex(indexName.toLowerCase(),msg.getCreatedDate().toLowerCase(),msg.getAdsId()).setSource(msgMap).get();
			String _index = response.getIndex();
			// Type name
			String _type = response.getType();
			// Document ID (generated or not)
			String _id = response.getId();
			// Version (if it's the first time you index this document, you will get: 1)
			long _version = response.getVersion();
			// status has stored current instance statement.
			RestStatus _status = response.status();
			//log.info("postJson {index="+_index+",type="+_type+",id="+_id+",status="+_status+",version="+_version+"}");
		}
		elkPool.releaseTransportClient(client);
	}
	
	public void postBulkAdsMessage(String indexName,List <AdsMessage>msgList){
		TransportClient client=elkPool.getTransportClientFromPool();	
		List<IndexRequestBuilder> irbList=new ArrayList<IndexRequestBuilder>();
		for(AdsMessage msg:msgList){
			Map <String,Object> msgMap=AdsMessage.convertToMap(msg);
			irbList.add(client.prepareIndex(indexName.toLowerCase(),msg.getCreatedDate().toLowerCase(),msg.getAdsId()).setSource(msgMap));
		}
		postBulkData(client,irbList);
		elkPool.releaseTransportClient(client);
	}
	
	//save json
	public void postJsonList(String indexName,String typeName,List<String>jsonList){
		//application/json
		TransportClient client=elkPool.getTransportClientFromPool();	
		for(String dataJson:jsonList){
		IndexResponse response = client.prepareIndex(indexName, typeName).setSource(dataJson,XContentType.JSON).get();
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// status has stored current instance statement.
		RestStatus _status = response.status();
		log.info("postJson {index="+_index+",type="+_type+",id="+_id+",status="+_status+",version="+_version+"}");
		}
	}
	
	//save map
	public void postMapList(String indexName,String typeName,List<Map<String,?>>MapList){
		//application/json
		TransportClient client=elkPool.getTransportClientFromPool();	
		for(Map<String,?>dataMap:MapList){
		IndexResponse response = client.prepareIndex(indexName,typeName).setSource(dataMap).get();
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// status has stored current instance statement.
		RestStatus _status = response.status();
		log.info("postJson {index="+_index+",type="+_type+",id="+_id+",status="+_status+",version="+_version+"}");
		}
	}
	
	//update map
	public void putMapByIds(String indexName,String typeName,Map<String,Map<String,?>> idMap) throws InterruptedException, ExecutionException{
				Set<Map.Entry<String, Map<String,?>>> entrySet=idMap.entrySet();  
				TransportClient client=elkPool.getTransportClientFromPool();	
				for (Map.Entry<String, Map<String,?>> entry:entrySet) {
				  String id=entry.getKey();
				  Map<String,?> dataMap=entry.getValue();
				  UpdateRequest updateRequest = new UpdateRequest();
				  updateRequest.index(indexName);
				  updateRequest.type(typeName);
				  updateRequest.id(id);
				  updateRequest.doc(dataMap);
				  client.update(updateRequest).get();
			  }
	}
	
	public void fetchDocument(SearchResponse response) {
        while(true) {
            for (SearchHit hit : response.getHits()) {
                Iterator<Entry<String, Object>> iterator = hit.getSource().entrySet().iterator();
                while(iterator.hasNext()) {
                    Entry<String, Object> next = iterator.next();
                    System.out.println(next.getKey() + ": " + next.getValue());
                    if(response.getHits().hits().length == 0) {
                        break;
                    }
                }
            }
            break;
        }
    }
	
	public void getDataById(String indexName,String typeName,String docId) {
			TransportClient client=elkPool.getTransportClientFromPool();
	        GetRequestBuilder requestBuilder = client.prepareGet(indexName,typeName,docId);
	        GetResponse response = requestBuilder.execute().actionGet();
	        GetResponse getResponse = requestBuilder.get();
	        ListenableActionFuture<GetResponse> execute = requestBuilder.execute();
	        System.out.println("client@"+client.hashCode()+"=>"+response.getSourceAsString());
	        elkPool.releaseTransportClient(client);
	        
	    }
	
	public DeleteResponse removeDataById(String indexName,String typeName,String docId){
		TransportClient client=elkPool.getTransportClientFromPool();	
		DeleteResponse response = client.prepareDelete(indexName,typeName,docId).get();
		elkPool.releaseTransportClient(client);
		return response;
	}
	
	
	public long removeDataBySimpleEqualsField(String indexName,String fieldName,Object fieldValue){
		TransportClient client=elkPool.getTransportClientFromPool();	
		BulkByScrollResponse response =
			    DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
			        .filter(QueryBuilders.termQuery(fieldName,fieldValue)) 
			        .source(indexName)                                  
			        .get();                                             
		long result=response.getDeleted();
		elkPool.releaseTransportClient(client);
		return result;
	}
	
	public long removeDataByMultiEqualsField(String indexName,Map<String,?>conditionMap,String conditionType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		TransportClient client=elkPool.getTransportClientFromPool();	
		BulkByScrollResponse response =
			    DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
			        .filter(composeEqualCondtionWithMulitQuery(null,conditionMap,conditionType)) 
			        .source(indexName)                                  
			        .get();                                             
		long result=response.getDeleted(); 
		elkPool.releaseTransportClient(client);
		return result;
	}
	
	public void searchOfFilterAggreator(String indexName,String typeName,String aggreatorName,String field,Object value){
		TransportClient client=elkPool.getTransportClientFromPool();
		AggregationBuilder aggregationBuilder =
			    AggregationBuilders.filter(aggreatorName,QueryBuilders.termQuery(field,value));
		SearchResponse sr = client.prepareSearch(indexName)
		 		.setTypes(typeName).addAggregation(aggregationBuilder).execute().actionGet();
		Filter agg = sr.getAggregations().get(aggreatorName);
		agg.getDocCount(); // Doc count
		elkPool.releaseTransportClient(client);
	}
	
	public void searchOfFiltersAggreator(String indexName,String typeName,String aggreatorName,KeyedFilter... keyedFilters){
		TransportClient client=elkPool.getTransportClientFromPool();
		AggregationBuilder aggregationBuilder =
			    AggregationBuilders.filters(aggreatorName,keyedFilters);
		SearchResponse sr = client.prepareSearch(indexName)
		 		.setTypes(typeName).addAggregation(aggregationBuilder).execute().actionGet();
		Filters agg = sr.getAggregations().get(aggreatorName);
		// For each entry
		for (Filters.Bucket entry : agg.getBuckets()) {
		    String key = entry.getKeyAsString();            // bucket key
		    long docCount = entry.getDocCount();            // Doc count
		    System.out.println("key [{"+key+"}], doc_count [{"+docCount+"}]");
		}
		elkPool.releaseTransportClient(client);
	}
	
	public KeyedFilter[] keyFiltersBuilder(Map<String,Object>fieldMap){
		List<KeyedFilter> keyedFilterList=new ArrayList<KeyedFilter>();
		for(Map.Entry<String,Object> entry:fieldMap.entrySet()){
			keyedFilterList.add(new FiltersAggregator.KeyedFilter(entry.getKey(), QueryBuilders.termQuery(entry.getKey(),entry.getValue())));
		}
		return keyedFilterList.toArray(new KeyedFilter[]{});
	}
	
	public void getAggregationData(String indexName,String typeName,String parentBucketField){
		//parent bucket
		TransportClient client=elkPool.getTransportClientFromPool();
		TermsAggregationBuilder termsAggregationBuilder=AggregationBuilders.terms("by_"+parentBucketField).field(parentBucketField);
		SearchResponse sr = client.prepareSearch(indexName)
		 		.setTypes(typeName).addAggregation(termsAggregationBuilder).execute().actionGet();
		Terms tremResult  = sr.getAggregations().get("by_"+parentBucketField);
		// For each entry
		for (Terms.Bucket entry : tremResult.getBuckets()) {
		    System.out.print("key:=>"+entry.getKey()+";doc count="+entry.getDocCount()); // Doc count
		}
		elkPool.releaseTransportClient(client);
	}
	
	public void postBulkData(TransportClient client,List<IndexRequestBuilder> irbList){
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(IndexRequestBuilder irb:irbList){
			bulkRequest.add(irb);
			}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
		    // process failures by iterating through each bulk response item
			System.out.println("bulk add error");
		}
	}
	
}
