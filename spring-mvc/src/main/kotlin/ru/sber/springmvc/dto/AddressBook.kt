package ru.sber.springmvc.dto

import java.util.concurrent.ConcurrentHashMap

class AddressBook(bookOfAddresses: ConcurrentHashMap<Int, Address>) {

    var bookContainerParam: ConcurrentHashMap<Int, Address>
        init {
            bookContainerParam = bookOfAddresses
        }
    var indexOfIdsParam = 0

    fun addAddressToBook(address: Address) {
        bookContainerParam[indexOfIdsParam++] = address
    }

    fun getAddressFromBook(bookContainer: ConcurrentHashMap<Int, Address>, id: Int): Address {
        return bookContainer[id]!!
    }
}