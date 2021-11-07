package ru.sber.springmvc.services

import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import ru.sber.springmvc.dto.Address

@Service
class BookingServiceImpl: BookingService {

    var bookContainer = ConcurrentHashMap<Int, Address>()
    var indexOfIds = 0

    override fun addAddress(address: Address): Address {
        bookContainer[indexOfIds] = address
        return bookContainer[indexOfIds++]!!
    }

    override fun getAddresses(allParams: Map<String, String>): ConcurrentHashMap<Int, Address> {
        val resultSearch = ConcurrentHashMap<Int, Address>()

        val id = allParams.getOrDefault("id", "undefined")
        val name = allParams.getOrDefault("name", "undefined")
        val surname = allParams.getOrDefault("surname", "undefined")
        val address = allParams.getOrDefault("address", "undefined")
        val telephone = allParams.getOrDefault("telephone", "undefined")

        for ((k, v) in bookContainer){
            if (id != "undefined"){
                if (k != id.toInt())
                    continue
            }
            if (name != "undefined"){
                if (v.name != name)
                    continue
            }
            if (surname != "undefined"){
                if (v.surname != surname)
                    continue
            }
            if (address != "undefined"){
                if (v.address != address)
                    continue
            }
            if (telephone != "undefined"){
                if (v.telephone != telephone)
                    continue
            }
            resultSearch[resultSearch.size] = v
        }
        return resultSearch
    }

    override fun getAddress(id: Int): Address? = bookContainer[id]

    override fun updateAddress(address: Address, id: Int): Address {
        bookContainer[id] = address
        return address
    }

    @Secured("ROLE_ADMIN")
    override fun deleteAddress(id: Int): Address {
        val tmp = bookContainer[id]
        bookContainer.remove(id)
        return tmp!!
    }

    override fun getId(address: Address): Int? {
        for(pair in bookContainer.entries) {
            if(address == pair.value)
                return pair.key
        }
        return null
    }
}