package org.sdl.salesdemo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Configuration;
/**
 *
 * @author shannonlal
 */
@Configuration
@EnableCaching
public class EHCacheConfiguration {
    @Bean
    public EhCacheManagerFactoryBean ehCahceFactory(){      
        EhCacheManagerFactoryBean ehCache = new EhCacheManagerFactoryBean();
        ehCache.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCache.setCacheManagerName("customerCache");
        ehCache.setShared(true);
        return ehCache;
    }
     
    /**
     * The following will configure the cache manager
     * @param ehCahceFactory
     * @return 
     */
    @Bean
    public EhCacheCacheManager cachceManager(EhCacheManagerFactoryBean ehCahceFactory){
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        ehCacheCacheManager.setCacheManager(ehCahceFactory.getObject());
        return ehCacheCacheManager;
    }
}
