package ru.sber.springmvc.services

import ru.sber.springmvc.dto.Address
import java.util.concurrent.ConcurrentHashMap

interface BookingService {

    fun addAddress(address: Address): Address

    fun getAddresses(allParams: Map<String, String>): ConcurrentHashMap<Int, Address>

    fun getAddress(id: Int): Address?

    fun updateAddress(address: Address, id: Int): Address

    fun deleteAddress(id: Int): Address

    fun getId(address: Address): Int?

}