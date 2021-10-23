package com.example.springmvc.filter

import java.time.Instant
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/api/*", "/app/*"])
private class AuthenticationFilter : HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        context = filterConfig.servletContext
        context.log("Authentication filter is initialized")
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val cookies = request!!.cookies

        if (cookies == null) {
            context.log("cookies not found")
            context.log("Unauthorized access request")
            response!!.sendRedirect("/login")
        }
        else {
            val currentTime = Instant.now().toString()
            var logMessage = ""

            for (cookie in cookies) {
                if (cookie.name == "auth") {
                    if (currentTime > cookie.value) {
                        logMessage = "cookie is valid"
                        context.log(logMessage)
                        chain!!.doFilter(request, response)
                    }
                    else {
                        logMessage = "wrong cookie value: cookie value - ${cookie.value}, current value - $currentTime"
                        context.log(logMessage)
                        response!!.sendRedirect("/login")
                    }
                }
            }

            if (logMessage.isEmpty()) {
                context.log("cookie not found")
                response!!.sendRedirect("/login")
            }
        }
    }
}