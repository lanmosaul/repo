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
	private Map<TransportClient, Date> occupiedTransportClientMap = new ConcurrentHashMap<TransportClient, Date>();

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
				occupiedTransportClientMap.put(client, new Date());
			}
		}
		System.out.print("end of init");
	}

	public TransportClient getTransportClientFromPool(){
		TransportClient client = cacheClientQueue.poll();
		if (client == null) {
			// TODO Auto-generated catch block
			if (occupiedTransportClientMap.size() < elkPoolConfig.getMax_client()) {
				Settings settings = Settings.builder().put("cluster.name", elkPoolConfig.getCluster_name()).build();
				try {
					client = new PreBuiltTransportClient(settings).addTransportAddress(
							new InetSocketTransportAddress(InetAddress.getByName(elkPoolConfig.getNodes_config()), 9300));
				}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				occupiedTransportClientMap.put(client, new Date());
				return client;
			} else {
				return null;
			}
		}
		return client;
	}

	public void releaseTransportClient(TransportClient client){
		occupiedTransportClientMap.remove(client);
		try {
			cacheClientQueue.put(client);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void  handleTimeOut(){
		System.out.println("before cacheClientQueue==>"+cacheClientQueue.size());
		System.out.println("before occupiedClient==>"+occupiedTransportClientMap.size());
		Iterator<Map.Entry<TransportClient, Date>> it = occupiedTransportClientMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<TransportClient, Date> entry = it.next();
			TransportClient tc = entry.getKey();
			Date createdDate = entry.getValue();
			long curTime = System.currentTimeMillis();
			long timeInterval = curTime - createdDate.getTime();
			if (timeInterval >= elkPoolConfig.getTimemout()) {
				System.out.println("remove client@"+tc.hashCode());
				cacheClientQueue.remove(tc);
				tc.close();
				it.remove();
			}
		}
		System.out.println("after cacheClientQueue==>"+cacheClientQueue.size());
		System.out.println("after occupiedClient==>"+occupiedTransportClientMap.size());
	}
//	@PostConstruct
	private void monitorTimeOut() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.currentThread().sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handleTimeOut();
			}
		});
		t.start();
		
	}
}
