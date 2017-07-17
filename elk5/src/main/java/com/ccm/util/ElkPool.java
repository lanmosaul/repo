package com.ccm.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

@Component
public class ElkPool {
	@Resource
	private ElkPoolConfig elkPoolConfig;
	private Map<TransportClient, Date> timeoutTransportClientMap = new ConcurrentHashMap<TransportClient, Date>();

	private BlockingQueue<TransportClient> cacheClientQueue = null;
	@PostConstruct
	public void init() throws UnknownHostException, InterruptedException {
		cacheClientQueue = new LinkedBlockingQueue<TransportClient>(elkPoolConfig.getMax_client());
		if (elkPoolConfig.getMin_client() > 0) {
			for (int i = 1; i <= elkPoolConfig.getInit_client(); i++) {
				System.out.println("creating client:"+i);
				Settings settings = Settings.builder().put("cluster.name", elkPoolConfig.getCluster_name()).build();
				TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(elkPoolConfig.getNodes_config()), 9300));
				cacheClientQueue.put(client);
				timeoutTransportClientMap.put(client, new Date());
			}
		}
		System.out.print("end of init");
	}

	public TransportClient getTransportClientFromPool(){
		System.out.println("pool size"+cacheClientQueue.size());
		TransportClient client= cacheClientQueue.poll();
		if(client!=null){
			Date createdDate=timeoutTransportClientMap.get(client);
			long curTime = System.currentTimeMillis();
			long timeInterval = curTime - createdDate.getTime();
			//timeout
			if (timeInterval >= elkPoolConfig.getTimemout()) {
				timeoutTransportClientMap.remove(client);
				client.close();
				System.out.println("remove client "+client.hashCode()+" with timeout");
				return getTransportClientFromPool();
			}
			return client;
		}else{
			// TODO Auto-generated catch block
			if (timeoutTransportClientMap.size() < elkPoolConfig.getMax_client()) {
				Settings settings = Settings.builder().put("cluster.name", elkPoolConfig.getCluster_name()).build();
				try {
					client = new PreBuiltTransportClient(settings).addTransportAddress(
							new InetSocketTransportAddress(InetAddress.getByName(elkPoolConfig.getNodes_config()), 9300));
				}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timeoutTransportClientMap.put(client, new Date());
				System.out.println("put new client "+client.hashCode()+" in the queue");
				return client;
			} else {
				return null;
			}
		}
	}

	public void releaseTransportClient(TransportClient client){
		Date createdDate=timeoutTransportClientMap.get(client);
		long curTime = System.currentTimeMillis();
		long timeInterval = curTime - createdDate.getTime();
		//timeout
		if (timeInterval >= elkPoolConfig.getTimemout()) {
			timeoutTransportClientMap.remove(client);
			client.close();
			System.out.println("remove client "+client.hashCode()+" with timeout");
		}else{
			try {
				cacheClientQueue.put(client);
				System.out.println("put old client "+client.hashCode()+" in the queue");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
