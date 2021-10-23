package com.example.springmvc.servlet

import java.time.Instant
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "AuthenticationServlet", urlPatterns = ["/login"])
class AuthenticationServlet: HttpServlet() {
    private val username = "admin"
    private val password = "password"

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req!!.getRequestDispatcher("/authenticationPage.html").forward(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (username == req?.getParameter("username") && password == req.getParameter("password")) {
            val cookie = Cookie("auth", Instant.now().toString())
            resp!!.addCookie(cookie)
            resp.sendRedirect("/menuPage.html")
        }
        else {
            resp!!.sendRedirect("/notAuthenticationPage.html")
        }
    }
}