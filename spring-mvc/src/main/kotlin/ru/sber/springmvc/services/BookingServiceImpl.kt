package ru.sber.springmvc.services

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import ru.sber.springmvc.dto.Address
import ru.sber.springmvc.dto.AddressBook

@Service
class BookingServiceImpl: BookingService {

    private var addressBook = AddressBook(bookOfAddresses = ConcurrentHashMap(), indexOfIds = 0)

    override fun addAddress(address: Address) {
        addressBook.addAddressToBook(address)
    }

    override fun getAddresses(): ConcurrentHashMap<Int, Address> {
        return addressBook.bookContainerParam
    }

    override fun getAddress(id: Int): Address? {
        return addressBook.bookContainerParam.get(id)
    }

    override fun updateAddress(address: Address, id: Int) {
        addressBook.bookContainerParam.put(id, address)
    }

    override fun deleteAddress(id: Int) {
        addressBook.bookContainerParam.remove(id)
    }

}