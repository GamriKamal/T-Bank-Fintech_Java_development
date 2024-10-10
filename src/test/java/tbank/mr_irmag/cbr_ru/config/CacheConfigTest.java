package tbank.mr_irmag.cbr_ru.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheConfigTest {

    private CacheConfig cacheConfig;

    @BeforeEach
    void setUp() {
        cacheConfig = new CacheConfig();
    }

    @Test
    void customKeyGenerator_SuccessGeneration_ShouldGenerateCorrectKey() throws NoSuchMethodException {
        // Arrange
        KeyGenerator keyGenerator = cacheConfig.customKeyGenerator();
        String expectedKey = "USD-RUB-100.0";
        Object[] params = new Object[]{"USD", "RUB", 100.0};

        // Act
        String generatedKey = keyGenerator.generate(this, getTestMethod(), params).toString();

        // Assert
        assertEquals(expectedKey, generatedKey);
    }

    private Method getTestMethod() throws NoSuchMethodException {
        return this.getClass().getDeclaredMethod("customKeyGenerator_SuccessGeneration_ShouldGenerateCorrectKey");
    }

}
