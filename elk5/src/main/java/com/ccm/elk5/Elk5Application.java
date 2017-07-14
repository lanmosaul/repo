package com.ccm.elk5;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.ccm.dao.ElkQueryDao;
import com.ccm.util.ElkParam;
@SpringBootApplication
@ComponentScan(basePackages = {"com.ccm.dao","com.ccm.util"})
public class Elk5Application implements CommandLineRunner{
	
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(Elk5Application.class, args).close();
	}
	
	@Resource 
	private ElkQueryDao elkQueryDao;
	
	
	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		//"dept", "employee", "AV0wmEh5X8uqqMmcr1nw"
		System.out.println("begin");
		while(true){
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			conditionMap.put("empname","emp2");
			QueryBuilder qb=elkQueryDao.composeEqualCondtionWithMulitQuery(null, conditionMap, ElkParam.ELC_AND);
			SearchResponse sr=elkQueryDao.initSearchRequestBuilder("dept", "employee",qb,"age",SortOrder.DESC,0,100);
			elkQueryDao.fetchDocument(sr);
//			elkQueryDao.getDataById("dept", "employee", "AV0wmEh5X8uqqMmcr1nw");
			
		}
		/*
		Settings settings = Settings.builder()
		        .put("cluster.name",cluster_name).build();
		TransportClient client = 
				new PreBuiltTransportClient
				(settings)
		        .addTransportAddress(
		        		new InetSocketTransportAddress(InetAddress.getByName(nodes_config), 9300));
		
		QueryBuilder qb=initFuzzyQueryBuilder("empname","e");
		SearchResponse sr=initSearchRequestBuilder(client,"dept","employee",qb,null,null,0,100);
		fetchDocument(sr);
		client.close();
		*/
	}
}
