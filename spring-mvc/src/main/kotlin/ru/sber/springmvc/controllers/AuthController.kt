package ru.sber.springmvc.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AuthController {

    @RequestMapping("/auth")
    fun loginPage(): String {
        return "auth"
    }

    @RequestMapping("/success")
    fun successPage(): String {
        return "successAuth"
    }
}