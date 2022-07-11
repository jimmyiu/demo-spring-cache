package dev.iuhh.demo.springcache

import com.ninjasquad.springmockk.MockkBean
import dev.iuhh.demo.springcache.external.ExternalCalculationService
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class CalculationFallbackAdapterTest {
    @MockkBean
    lateinit var externalCalculationService: ExternalCalculationService

    @Autowired
    lateinit var adapter: CalculationFallbackAdapter

    @Test
    fun `should return calculation result from external calculation service`() {
        every { externalCalculationService.calculate(any(), any()) } returns 100
        assertThat(adapter.calculate(1, 2)).isEqualTo(100)
    }

    @Nested
    @DisplayName("Given calculation was involved successfully once with result 100")
    inner class GivenCalculationSucceededOnce {
        @BeforeEach
        fun setup() {
            every { externalCalculationService.calculate(any(), any()) } returns 100
            adapter.calculate(1, 2)
        }

        @Test
        fun `should return fallback cache when calculation service throws RuntimeException`() {
            every { externalCalculationService.calculate(any(), any()) } throws RuntimeException()
            assertThat(adapter.calculate(1, 2)).isEqualTo(100)
        }

        @Test
        fun `should return result from external service when external service health`() {
            every { externalCalculationService.calculate(any(), any()) } returns 50
            assertThat(adapter.calculate(1, 2)).isEqualTo(50)
        }
    }
}