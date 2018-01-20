package com.flea.common;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;


public class SolrContent {
	Log log = LogFactory.getLog(this.getClass());
	private String url;
	public SolrContent(String url) {
		this.url = url;
	}
	public SolrClient createClient() {
		return new HttpSolrClient(url);
	}
	
	/**
	 * 高亮显示
	 * @param query
	 * @param hl
	 */
	public void toHighlight(SolrQuery query,String... hl) {
		if (query==null||hl==null)return;
		query.setHighlight(true);
		query.setHighlightRequireFieldMatch(false);
		query.setHighlightSimplePost("</spen>");//前缀
		query.setHighlightSimplePre("<spen class='highlight'>");//后缀
		query.setHighlightRequireFieldMatch(false);
		for (int i = 0; i < hl.length; i++) {
			query.setParam("hl.fl", hl[i]);
		}
		
	}
	public void colse(SolrClient client) {
		try {
			if (client!=null)client.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
	public void rollback(SolrClient client) {
		try {
			if (client!=null)client.rollback();
		} catch (SolrServerException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
