//package ru.sber.springmvc.filters
//
//import java.time.LocalTime
//import javax.servlet.Filter
//import javax.servlet.FilterChain
//import javax.servlet.ServletRequest
//import javax.servlet.ServletResponse
//import javax.servlet.annotation.WebFilter
//import javax.servlet.http.Cookie
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//@WebFilter(
//    urlPatterns = ["/api/*", "/app/*"]
//)
//class AuthFilter: Filter {
//    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
//        val req = p0 as HttpServletRequest
//        val resp = p1 as HttpServletResponse
//        val time = LocalTime.now()
//
//        val session = req.getSession(false)
//        val loginURI = req.contextPath + "/login"
//
//        var loggedIn = session != null && session.getAttribute("auth") < time
//    }
//}