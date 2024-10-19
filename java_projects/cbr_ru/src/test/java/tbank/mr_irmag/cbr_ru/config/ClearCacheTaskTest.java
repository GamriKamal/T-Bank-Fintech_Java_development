package tbank.mr_irmag.cbr_ru.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import tbank.mr_irmag.cbr_ru.config.ClearCacheTask;

import java.util.List;

import static org.mockito.Mockito.*;

class ClearCacheTaskTest {

    @InjectMocks
    private ClearCacheTask clearCacheTask;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reportCurrentTime_SuccessClearingCache_ShouldClearAllCaches() {
        // Arrange
        String cacheName1 = "cache1";
        String cacheName2 = "cache2";

        when(cacheManager.getCacheNames()).thenReturn(List.of(cacheName1, cacheName2));
        when(cacheManager.getCache(cacheName1)).thenReturn(mock(Cache.class));
        when(cacheManager.getCache(cacheName2)).thenReturn(mock(Cache.class));

        // Act
        clearCacheTask.reportCurrentTime();

        // Assert
        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager.getCache(cacheName1), times(1)).clear();
        verify(cacheManager.getCache(cacheName2), times(1)).clear();
    }
}
