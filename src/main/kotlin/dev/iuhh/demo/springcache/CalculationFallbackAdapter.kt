package dev.iuhh.demo.springcache

import dev.iuhh.demo.springcache.external.CacheableService
import dev.iuhh.demo.springcache.external.ExternalCalculationService
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.stereotype.Service

@Service
class CalculationFallbackAdapter(
    private val externalCalculationService: ExternalCalculationService,
    private val cacheManager: CacheManager
) : CalculationAdapter, CacheableService {
    @CachePut(CACHE_KEY)
    override fun calculate(x: Int, y: Int) = try {
        externalCalculationService.calculate(x, y)
    } catch (ex: RuntimeException) {
        val cached = cacheManager.getCache(CACHE_KEY)
            ?.get(SimpleKey(x, y))
            ?.get() as Int?
        cached ?: throw ex
    }

    @CacheEvict(CACHE_KEY, allEntries = true)
    override fun clearCache() {
    }

    companion object {
        const val CACHE_KEY = "calculate"
    }
}
