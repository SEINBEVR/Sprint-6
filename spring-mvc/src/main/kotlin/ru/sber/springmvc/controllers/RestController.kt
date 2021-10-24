package ru.sber.springmvc.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.services.BookingService
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api")
class RestController @Autowired constructor(val bookingService: BookingService) {

    @PostMapping("/add")
    fun addAddress(@RequestBody address: Address): ResponseEntity<*> {
        return ResponseEntity(bookingService.addAddress(address), HttpStatus.CREATED)
    }

    @GetMapping("/list")
    fun getAddresses(@RequestParam(required = false) allParams: Map<String, String>): ResponseEntity<ConcurrentHashMap<Int, Address>> {
        val ads = bookingService.getAddresses(allParams)
        return ResponseEntity(ads, HttpStatus.OK)
    }

    @GetMapping("/{id}/view")
    fun getAddress(@PathVariable("id") id: Int): ResponseEntity<Address> {
        val ad = bookingService.getAddress(id)
        return ResponseEntity(ad, HttpStatus.OK)
    }

    @PutMapping("/{id}/edit")
    fun updateAddress(@PathVariable("id") id: Int, @RequestBody address: Address): ResponseEntity<*> {
        return ResponseEntity(bookingService.updateAddress(id = id, address = address), HttpStatus.OK)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteAddress(@PathVariable("id") id: Int): ResponseEntity<*> {
        return ResponseEntity(bookingService.deleteAddress(id), HttpStatus.OK)
    }
}
