package com.rescuehub.rescuehubserver.security

import org.springframework.util.SerializationUtils
import java.util.Base64
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


interface CookieOps {

    fun <T> deserializeCookie(request: HttpServletRequest, name: String): T? {
        return request.getCookies()?.find { it.getName() == name }?.let {
            val decoder = Base64.getUrlDecoder()
            SerializationUtils.deserialize(decoder.decode(it.getValue())) as T
        }
    }

    fun serialize(obj: Any): String = Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj))

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int): Unit {
        val cookie = Cookie(name, value)
        cookie.setPath("/")
        cookie.setHttpOnly(true)
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String): Unit {
        request.getCookies()?.filter { it.getName() == name }?.forEach {
            it.setValue("")
            it.setPath("/")
            it.setMaxAge(0)
            response.addCookie(it)
        }
    }
}

