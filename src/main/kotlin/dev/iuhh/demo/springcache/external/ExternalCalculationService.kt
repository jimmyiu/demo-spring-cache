package dev.iuhh.demo.springcache.external

import org.springframework.stereotype.Component

@Component
class ExternalCalculationService {
    fun calculate(x: Int, y: Int) = x + y
}
