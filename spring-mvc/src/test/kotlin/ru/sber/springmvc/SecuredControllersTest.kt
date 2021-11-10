package ru.sber.springmvc

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class SecuredControllersTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @WithMockUser(username = "user", password = "user")
    @Test
    fun `should successfully add address to bookcontainer and get list of addresses`() {
        mockMvc.perform(post("/app/add")
            .param("name", "Ivan")
            .param("surname", "Ivanov")
            .param("address", "Pushkina")
            .param("telephoneNumber","880")
            .with(csrf()))
            .andExpect(status().isOk)

        mockMvc.perform(get("/app/list")
            .with(csrf()))
            .andExpect(content().string(containsString("Ivan")))
    }

    @WithMockUser(username = "api", password = "api", roles = ["API"])
    @Test
    fun `should successfully get a list of addresses through api`() {

        mockMvc.perform(get("/api/list"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "user", password = "user")
    @Test
    fun `should get forbidden status for user trying to get list of addresses through api`() {
        mockMvc.perform(get("/api/list")
            .with(csrf()))
            .andExpect(status().isForbidden)
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun `should delete address`() {
        mockMvc.perform(post("/app/add")
            .param("name", "Ivan")
            .param("surname", "Ivanov")
            .param("address", "Pushkina")
            .param("telephoneNumber","880")
            .with(csrf()))
            .andExpect(status().isOk)

        mockMvc.perform(get("/app/0/delete")
            .with(csrf()))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "user", password = "user")
    @Test
    fun `should reject deleting address`() {
        mockMvc.perform(post("/app/add")
            .param("name", "Ivan")
            .param("surname", "Ivanov")
            .param("address", "Pushkina")
            .param("telephoneNumber","880")
            .with(csrf()))
            .andExpect(status().isOk)

        mockMvc.perform(get("/app/0/delete")
            .with(csrf()))
            .andExpect(status().isForbidden)
    }
}