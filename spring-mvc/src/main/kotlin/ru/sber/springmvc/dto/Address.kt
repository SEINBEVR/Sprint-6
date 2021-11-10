package ru.sber.springmvc.dto

data class Address(
    val name: String?,
    val surname: String?,
    val address: String?,
    val telephone: String?
) {
    var id: Int = -1
}

