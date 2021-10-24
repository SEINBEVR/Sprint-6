package ru.sber.springmvc.servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(urlPatterns = ["/login"])
class AuthServlet: HttpServlet() {
    private val username = "admin"
    private val password = "admin"

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val usernamePost = req?.getParameter("username")
        val passwordPost = req?.getParameter("password")

        if(usernamePost.equals(username) && passwordPost.equals(password)) {
            val cookie = Cookie("auth", System.currentTimeMillis().toString())
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
        val cookies = req!!.cookies
        if(cookies != null) {
            for(cookie in cookies) {
                if(cookie.name == "auth" && cookie.value < System.currentTimeMillis().toString()) {
                    resp!!.contentType = "text/html"
                    val pw = resp.writer
                    pw.println("<font color=green>You already have authorized.</font>")
                }
            }
        } else
        req.getRequestDispatcher("/auth.html").forward(req, resp)
    }
}