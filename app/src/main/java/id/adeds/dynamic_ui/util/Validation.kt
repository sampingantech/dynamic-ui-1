package id.adeds.dynamic_ui.util

import java.util.regex.Pattern

object Validation {
    fun isStringBase64(data: String): Boolean {
        return data.contains("Base64:")
    }

    fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            return false
        }
        return true
    }
}