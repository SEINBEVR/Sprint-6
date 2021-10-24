package ru.sber.springmvc.rest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.services.BookingService
import java.util.concurrent.ConcurrentHashMap

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllersRestIntegrationTest(@LocalServerPort var port: Int) {

    private val headers = HttpHeaders()

    @Autowired
    private lateinit var bookingService: BookingService

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val fullURI = "http://localhost:$port/"

    private fun getCookie(
        username: String = "admin",
        password: String = "admin",
        loginURI: String = "$fullURI/login"
    ): String {

        val form = LinkedMultiValueMap<String, String>()
        form.set("username", username)
        form.set("password", password)

        val loginResponse = restTemplate
            .postForEntity(
                loginURI,
                HttpEntity(form, HttpHeaders()),
                String::class.java
            )

        return loginResponse.headers["Set-Cookie"]!![0]
    }

    @BeforeEach
    fun setUpCookie() {
        val cookie = getCookie()

        headers.add("Cookie", cookie)
    }

    @Test
    fun `test add address`() {
        val address = Address("Ivan", "Ivanov", "Pushkina", "805")

        val URL = "$fullURI/api/add"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.POST,
            HttpEntity(address, headers),
            Address::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(address.name, response.body!!.name)
        assertEquals(address.surname, response.body!!.surname)
        assertEquals(address.address, response.body!!.address)
        assertEquals(address.telephone, response.body!!.telephone)
    }

    @Test
    fun `test get addresses`() {
        val book = ConcurrentHashMap<Int, Address?>()

        book.put(0, Address("Ivan", "Ivanov", "Pushkina", "805"))
        book.put(1, Address("Kolya", "Petrov", "Pushkina321", "705"))

        val URL = "$fullURI/api/list"

        val response = restTemplate.exchange(
            URL, HttpMethod.GET,
            HttpEntity(book.values, headers),
            ConcurrentHashMap::class.java)

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body)
    }

    @Test
    fun `test get addresses by param`() {
        val book = ConcurrentHashMap<Int, Address>()

        book.put(0, Address("Ivan", "Ivanov", "Pushkina", "805"))

        val URL = "$fullURI/api/list?name=Ivan"

        val response = restTemplate.exchange(
            URL, HttpMethod.GET,
            HttpEntity(book.values, headers),
            ConcurrentHashMap::class.java)

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body)
    }

    @Test
    fun `test get address`() {
        bookingService.addAddress(Address("Ivan", "Ivanov", "Pushkina", "805"))

        val URL = "$fullURI/api/0/view"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            HttpEntity(null, headers),
            Address::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(bookingService.getAddress(0)!!.name, response.body!!.name)
        assertEquals(bookingService.getAddress(0)!!.surname, response.body!!.surname)
        assertEquals(bookingService.getAddress(0)!!.address, response.body!!.address)
        assertEquals(bookingService.getAddress(0)!!.telephone, response.body!!.telephone)
    }

    @Test
    fun `test update address`() {
        val oldAddress = Address("Ivan", "Ivanov", "Pushkina", "805")

        bookingService.addAddress(oldAddress)

        val newAddress = Address("Kolya", "Petrov", "Pushkina321", "705")

        val URL = "$fullURI/api/0/edit"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.PUT,
            HttpEntity(newAddress, headers),
            Address::class.java)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(newAddress.name, response.body!!.name)
        assertEquals(newAddress.surname, response.body!!.surname)
        assertEquals(newAddress.address, response.body!!.address)
        assertEquals(newAddress.telephone, response.body!!.telephone)
    }

    @Test
    fun `test delete address`() {
        bookingService.addAddress(Address("Ivan", "Ivanov", "Pushkina", "805"))
        val id = 0

        val URL = "$fullURI/api/0/delete"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            Address::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(null, bookingService.getAddress(0))
    }
}
