package tbank.mr_irmag.cbr_ru.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearCacheTask {
    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRateString = "${clear.all.cache.fixed.rate}", initialDelayString = "${clear.all.cache.init.delay}")
    public void reportCurrentTime() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }
}