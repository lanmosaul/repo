package com.ccm.dao.elk;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.ccm.model.ads.AdsMessage;

public interface ElkQueryDao {
	public  BoolQueryBuilder initMulitQueryBuilder();
	
	public  BoolQueryBuilder composeEqualCondtionWithMulitQuery(BoolQueryBuilder boolQueryBuilder,Map<String,?>conditionMap,String conditionType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public BoolQueryBuilder composeRangeCondtionWithMulitQuery(BoolQueryBuilder boolQueryBuilder,String fieldName,Map<String,?>conditionMap) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public SearchResponse initSearchRequestBuilder(
			String indexName,String typeName,
			QueryBuilder queryBuilder,String sortField,SortOrder sortOrder,int beginIdx,int pageSize);
	
	public QueryBuilder initSimpleQueryBuilder(String fieldName,Object fieldValue);
	
	public QueryBuilder initFuzzyQueryBuilder(String fieldName,String fieldValue);
	
	public QueryBuilder intWildCardQueryBuilder(String fieldName,String fieldValue);
	
	public QueryBuilder initStringQueryBuilder(String fieldName,String fieldValue);
	
	public QueryBuilder initMatchQueryBuilder(String fieldName,Object fieldValue);
	
	//save json
	public void postObjectList(String indexName,String typeName,List<?>objectList);
	
	public void postAdsMessage(String indexName,List<AdsMessage> msgList);
	//save json
	public void postJsonList(String indexName,String typeName,List<String>jsonList);
	
	//save map
	public void postMapList(String indexName,String typeName,List<Map<String,?>>MapList);
	
	//update map
	public void putMapByIds(String indexName,String typeName,Map<String,Map<String,?>> idMap) throws InterruptedException, ExecutionException;
	
	public void fetchDocument(SearchResponse response);
	
	public void getDataById(String indexName,String typeName,String docId);
	
	public DeleteResponse removeDataById(String indexName,String typeName,String docId);
	
	
	public long removeDataBySimpleEqualsField(String indexName,String fieldName,Object fieldValue);
	
	public long removeDataByMultiEqualsField(String indexName,Map<String,?>conditionMap,String conditionType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	public void postBulkData(List<IndexRequestBuilder> irbList);
}
