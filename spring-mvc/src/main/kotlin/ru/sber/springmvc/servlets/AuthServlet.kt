//package ru.sber.springmvc.servlets
//
//import org.springframework.beans.factory.annotation.Autowired
//import ru.sber.springmvc.services.auth.AuthService
//import java.time.LocalTime
//import javax.servlet.annotation.WebServlet
//import javax.servlet.http.HttpServlet
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//@WebServlet(
//    name = "AuthServlet",
//    description = "Servlet for authentication",
//    urlPatterns = ["/login"]
//)
//class AuthServlet @Autowired constructor(val authService: AuthService): HttpServlet() {
//
//    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
//        val username = req?.getParameter("username")
//        val password = req?.getParameter("password")
//        var messages = HashMap<String, String>()
//        var correctData = false
//        val timeOfAuth = LocalTime.now()
//
//        if(username == null || username.isEmpty())
//            messages.put("username", "Please enter username")
//
//        if(password == null || password.isEmpty())
//            messages.put("password", "Please enter password")
//
//        if(messages.isEmpty())
//            correctData = authService.getAuthParams(username, password)
//
//        if(correctData) {
//            req?.session?.setAttribute("auth", timeOfAuth)
//            resp?.sendRedirect("success.html")
//            return
//        } else {
//            messages.put("login", "Unknown login, try again")
//        }
//        req?.setAttribute("messages", messages)
//        req?.getRequestDispatcher("/auth.html")?.forward(req, resp)
//    }
//
//    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
//        req?.getRequestDispatcher("/auth.html")?.forward(req, resp)
//    }
//}