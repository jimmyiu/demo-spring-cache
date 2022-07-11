package dev.iuhh.demo.springcache.external

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExternalCalculationServiceTest {
    private val service = ExternalCalculationService()
    @Test
    fun `should calculate addition correctly`() {
        assertThat(service.calculate(3, 4)).isEqualTo(7)
    }
}