package ru.sber.springmvc.servlets

import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(
    "/login"
)
class AuthServlet: HttpServlet() {
    private val username = "admin"
    private val password = "admin"

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val usernamePost = req?.getParameter("username")
        val passwordPost = req?.getParameter("password")

        if(usernamePost.equals(username) && passwordPost.equals(password)) {
            val cookie = Cookie("auth", Calendar.getInstance().timeInMillis.toString())
            resp!!.addCookie(cookie)
            resp.sendRedirect("/app/add")
        } else {
            val requestDispatcher = servletContext.getRequestDispatcher("/auth.html")
            val out = resp!!.writer
            out.println("<font color=red>Either user name or password is wrong.</font>")
            requestDispatcher.include(req, resp)
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req!!.getRequestDispatcher("/auth.html").forward(req, resp)
    }
}