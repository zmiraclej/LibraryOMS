/**  
* @Package com.flea.common.shiro
* @Description: TODO
* @author bruce
* @date 2016年6月22日 下午4:14:02
* @version V1.0  
*/ 
package com.flea.common.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * @author bruce
 * @2016年6月22日 下午4:14:02
 */
public class SpringCacheManagerWrapper implements CacheManager {

	
	private  org.springframework.cache.CacheManager  cacheManager;
	
	public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
	  org.springframework.cache.Cache  cache = cacheManager.getCache(name);
		return new SpringCacheWrapper(cache);
	}

	static class SpringCacheWrapper implements Cache {
		private org.springframework.cache.Cache springCache;
	
		public SpringCacheWrapper(org.springframework.cache.Cache springCache) {
			this.springCache = springCache;
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#get(java.lang.Object)
		 */
		@Override
		public Object get(Object key) throws CacheException {
			Object  value = springCache.get(key);
			if(value  instanceof SimpleValueWrapper){
				return ((SimpleValueWrapper)value).get();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#put(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object put(Object key, Object value) throws CacheException {
		    springCache.put(key, value);
            return value;
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#remove(java.lang.Object)
		 */
		@Override
		public Object remove(Object key) throws CacheException {
			springCache.evict(key);
			return null;
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#clear()
		 */
		@Override
		public void clear() throws CacheException {
			springCache.clear();
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#size()
		 */
		@Override
		public int size() {
		   if(springCache.getNativeCache()  instanceof  Ehcache){
			   Ehcache ehcahe = (Ehcache)springCache.getNativeCache();
			   return ehcahe.getSize();
		   }
			return 0;
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#keys()
		 */
		@Override
		public Set keys() {
			 if(springCache.getNativeCache() instanceof Ehcache) {
	            Ehcache ehcache = (Ehcache) springCache.getNativeCache();
	            return new HashSet(ehcache.getKeys());
	          }
	         throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
		}

		/* (non-Javadoc)
		 * @see org.apache.shiro.cache.Cache#values()
		 */
		@Override
		public Collection values(){
			if(springCache.getNativeCache() instanceof Ehcache){
				 Ehcache ehcache = (Ehcache) springCache.getNativeCache();
	             List keys = ehcache.getKeys();
	             if(!CollectionUtils.isEmpty(keys)){
	            	 List values = new ArrayList(keys.size());
	            	 for(Object key:keys){
	            		 Object value = get(key);
	                        if (value != null) {
	                            values.add(value);
	                       }
	            	 }
	            	 return Collections.unmodifiableList(values);
	             }else{
	            	 return Collections.emptyList();
	             }
			}
			throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
		}
		 
		 
	}
}
