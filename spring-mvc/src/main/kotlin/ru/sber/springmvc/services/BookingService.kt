package ru.sber.springmvc.services

import ru.sber.springmvc.dto.Address
import java.util.concurrent.ConcurrentHashMap

interface BookingService {

    fun addAddress(address: Address)

    fun getAddresses(): ConcurrentHashMap<Int, Address>

    fun getAddress(id: Int): Address?

    fun updateAddress(address: Address, id: Int)

    fun deleteAddress(id: Int)

}