package com.ccm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ElkPoolConfig {
	@Value("${elastic-clustername}")
	private String cluster_name;
	@Value("${elastic-nodes}")
	private String nodes_config;
	@Value("${elastic-init-transportClient}")
	private int init_client;
	@Value("${elastic-max-transportClient}")
	private int max_client;
	@Value("${elastic-min-transportClient}")
	private int min_client;
	@Value("${elastic-timeout}")
	private long timemout;
	public int getInit_client() {
		return init_client;
	}
	public void setInit_client(int init_client) {
		this.init_client = init_client;
	}
	public String getCluster_name() {
		return cluster_name;
	}
	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}
	public String getNodes_config() {
		return nodes_config;
	}
	public void setNodes_config(String nodes_config) {
		this.nodes_config = nodes_config;
	}
	public int getMax_client() {
		return max_client;
	}
	public void setMax_client(int max_client) {
		this.max_client = max_client;
	}
	public int getMin_client() {
		return min_client;
	}
	public void setMin_client(int min_client) {
		this.min_client = min_client;
	}
	public long getTimemout() {
		return timemout;
	}
	public void setTimemout(long timemout) {
		this.timemout = timemout;
	}
	
	
	
}
