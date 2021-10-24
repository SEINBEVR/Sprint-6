package ru.sber.springmvc.mvc

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.sber.springmvc.controllers.MvcController
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.services.BookingService
import java.util.concurrent.ConcurrentHashMap

@SpringBootTest
@AutoConfigureMockMvc
class ControllersMvcIntegrationTest {
    @RelaxedMockK
	private lateinit var bookingService: BookingService

    @InjectMockKs
    private lateinit var mvcController: MvcController
    init {
        MockKAnnotations.init(this)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

        @BeforeEach
        fun before() {
            this.mockMvc = MockMvcBuilders.standaloneSetup(mvcController).build()
        }

    @Test
    fun `test get addresses`() {
        val book = ConcurrentHashMap<Int, Address>()

        book.put(0, Address("Ivan", "Ivanov", "Pushkina", "805"))
        book.put(1, Address("Kolya", "Petrov", "Pushkina321", "705"))

        every { bookingService.getAddresses(HashMap()) } returns book

        mockMvc.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("addresses"))
            .andExpect(model().attribute("addresses", book))
    }

    @Test
    fun `test get addresses by param`() {
        val param = HashMap<String, String>()
        param.put("telephone","805")

        val checkAtt = ConcurrentHashMap<Int, Address>()
        checkAtt.put(1, Address("Kolya", "Petrov", "Pushkina321", "705"))

        every { bookingService.getAddresses(param) } returns checkAtt

        mockMvc.perform(get("/app/list")
            .param("telephone", "805"))
            .andExpect(status().isOk)
            .andExpect(view().name("addresses"))
            .andExpect(model().attribute("addresses", checkAtt))
    }

    @Test
    fun `test get address`() {
        val address = Address("Ivan", "Ivanov", "Pushkina", "805")
        val book = ConcurrentHashMap<Int, Address>()
        book.put(0, address)

        every { bookingService.getAddress(0)} returns address

        mockMvc.perform(get("/app/0/view"))
            .andExpect(status().isOk)
            .andExpect(view().name("addresses"))
            .andExpect(model().attribute("addresses", book))
    }

    @Test
    fun `test add address`() {
        mockMvc.perform(get("/app/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("create"))

        mockMvc.perform(
            post("/app/add")
            .param("name", "Ivan")
            .param("surname", "Ivanov")
            .param("address", "Pushkina")
            .param("telephoneNumber","880"))
            .andExpect(status().isOk)
            .andExpect(view().name("result"))
            .andExpect(model().attribute("action","Вы успешно добавили запись"))

    }

    @Test
    fun `test update address`() {
        val book = ConcurrentHashMap<Int, Address>()

        book.put(0, Address("Ivan", "Ivanov", "Pushkina", "805"))
        book.put(1, Address("Kolya", "Petrov", "Pushkina321", "705"))

        for(id in 0..1) {
            mockMvc.perform(get("/app/$id/edit"))
                .andExpect(status().isOk)
                .andExpect(view().name("update"))

            mockMvc.perform(post("/app/$id/edit")
                .param("name", book[id]!!.name)
                .param("surname", book[id]!!.surname)
                .param("address", book[id]!!.address)
                .param("telephone", book[id]!!.telephone))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(view().name("result"))
                .andExpect(model().attribute("action","Вы успешно обновили запись"))

        }
    }

    @Test
    fun `test delete address`() {
        val book = ConcurrentHashMap<Int, Address>()

        book.put(0, Address("Ivan", "Ivanov", "Pushkina", "805"))
        book.put(1, Address("Kolya", "Petrov", "Pushkina321", "705"))

        for(id in 0..1) {
            every { bookingService.deleteAddress(id) } returns book.remove(id)!!

            mockMvc.perform(get("/app/$id/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(view().name("result"))
                .andExpect(model().attribute("action","Вы успешно удалили запись"))
        }
    }
}
