package ru.sber.springmvc.services

import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import ru.sber.springmvc.dto.Address

@Service
class BookingServiceImpl: BookingService {

    override fun addAddress(address: Address): Address {
        address.id = indexOfIds
        bookContainer[indexOfIds] = address
        return bookContainer[indexOfIds++]!!
    }

    override fun getAddresses(allParams: Map<String, String>): Map<Int, Address> {
        return bookContainer
            .filterValues { if(allParams.containsKey("name")) it.name == allParams.get("name") else true }
            .filterValues { if(allParams.containsKey("surname")) it.surname == allParams.get("surname") else true }
            .filterValues { if(allParams.containsKey("address")) it.address == allParams.get("address") else true }
            .filterValues { if(allParams.containsKey("telephone")) it.telephone == allParams.get("telephone") else true }
    }

    override fun getAddress(id: Int): Address? = bookContainer[id]

    override fun updateAddress(address: Address, id: Int): Address {
        address.id = id
        bookContainer[id] = address
        return address
    }

    @Secured("ROLE_ADMIN")
    override fun deleteAddress(id: Int): Address? {
        val tmp = bookContainer[id]
        if(tmp != null)
            bookContainer.remove(id)
        return tmp
    }

    companion object {
        var bookContainer = ConcurrentHashMap<Int, Address>()
        var indexOfIds = 0
    }
}