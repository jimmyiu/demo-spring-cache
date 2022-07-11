package dev.iuhh.demo.springcache

import dev.iuhh.demo.springcache.external.ExternalCalculationService
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.stereotype.Service

@Service
class CalculationFallbackAdapter(
    private val externalCalculationService: ExternalCalculationService,
    private val cacheManager: CacheManager
) {
    @CachePut(CACHE_KEY)
    fun calculate(x: Int, y: Int) = try {
        externalCalculationService.calculate(x, y)
    } catch (ex: RuntimeException) {
        val cached = cacheManager.getCache(CACHE_KEY)
            ?.get(SimpleKey(x, y))
            ?.get() as Int?
        cached ?: throw ex
    }

    companion object {
        const val CACHE_KEY = "calculate"
    }
}
