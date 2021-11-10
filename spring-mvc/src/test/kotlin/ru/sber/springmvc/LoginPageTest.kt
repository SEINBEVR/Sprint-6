package ru.sber.springmvc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.services.BookingService
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginPageTest {

    @LocalServerPort
    private var port: Long = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var bookingService: BookingService

    private val headers = HttpHeaders()

    private fun url(ep: String) = "http://localhost:$port/$ep"

    @Test
    fun `should redirect to login page getAddresses`() {
        val response = restTemplate.exchange(url("app/list"),
            HttpMethod.GET,
            HttpEntity(null, headers),
        String::class.java)
        assertTrue(response.body!!.contains("Login page"))
    }

    @Test
    fun `should redirect to login page addAddress`() {
        val response = restTemplate.exchange(url("app/add"),
            HttpMethod.POST,
            HttpEntity<Address>(Address("Ivan", "Ivanov", "Pushkina", "805"), headers),
            Address::class.java)
        assertEquals(response.statusCode, HttpStatus.FOUND)
    }

    @Test
    fun `should redirect to login page getAddress`() {
        val response = restTemplate.exchange(url("app/0/view"),
            HttpMethod.GET,
            HttpEntity<Address>(null, headers),
            String::class.java)
        assertTrue(response.body!!.contains("Login page"))
    }
}