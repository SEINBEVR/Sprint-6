package ru.sber.springmvc.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.services.BookingService
import java.util.concurrent.ConcurrentHashMap

@Controller
@RequestMapping("/app")
class MvcController @Autowired constructor(val bookingService: BookingService) {

    @RequestMapping("/add", method = [RequestMethod.GET])
    fun addAddressGetPage(): String {
        return "create"
    }

    @RequestMapping("/add", method = [RequestMethod.POST])
    fun addAddress(@ModelAttribute form: Address, model: Model): String {
        bookingService.addAddress(form)
        model.addAttribute("action", "Вы успешно добавили запись")
        return "result"
    }

    @RequestMapping("/list", method = [RequestMethod.GET])
    fun getAddresses(@RequestParam(required = false) allParams: Map<String, String>, model: Model): String {
        val addresses = bookingService.getAddresses(allParams)
        model.addAttribute("addresses", addresses)
        return "addresses"
    }

    @RequestMapping("/{id}/view", method = [RequestMethod.GET])
    fun getAddress(@PathVariable("id") id: Int, model: Model): String {
        val addresses = ConcurrentHashMap<Int, Address?>()
        bookingService.getAddress(id)?.let { addresses.put(id, it) }
        model.addAttribute("addresses", addresses)
        return "addresses"
    }

    @RequestMapping("/{id}/edit", method = [RequestMethod.GET])
    fun updateAddressGetPage(@PathVariable("id") id: Int, model: Model): String {
        model.addAttribute("id", id.toString())
        return "update"
    }

    @RequestMapping("/{id}/edit")
    fun updateAddress(@PathVariable("id") id: Int, @ModelAttribute form: Address, model: Model): String {
        bookingService.updateAddress(id = id, address = Address(
            name = form.name,
            surname = form.surname,
            address = form.address,
            telephone = form.telephone
        )
        )
        model.addAttribute("action", "Вы успешно обновили запись")
        return "result"
    }

    @RequestMapping("/{id}/delete")
    fun deleteAddress(@PathVariable("id") id: Int, model: Model): String {

        val tmp = bookingService.deleteAddress(id)
        if(tmp != null) {
            model.addAttribute("action", "Вы успешно удалили запись")
        }
        else {
            model.addAttribute("action", "Вы пытаетесь удалить несущствующую запись")
        }
        return "result"
        }
    }

