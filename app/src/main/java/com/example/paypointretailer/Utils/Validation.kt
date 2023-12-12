package com.example.paypointretailer.Utils

import android.widget.EditText
import java.util.regex.Pattern

object Validation {
    val EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    val VEHICLE_NUMER_REGEX = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$"
    val PASSWORD1 = ("^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$")
    val PASSWORD = ("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#\\\$%^&+=])(?=\\\\S+\\\$).{8,16}\\\$")
    val VALID_URL ="^[//A-Za-z0-9-\\+]+(\\.[//A-Za-z0-9-]+)*." + "[//A-Za-z0-9-]+(\\.[//A-Za-z0-9]+)*(\\.[//A-Za-z]{0,})$"

    fun checkEmail(email: String?): Boolean {
        var flag = true
        if (email!!.isEmpty()) {
            flag = false
        } else if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            flag = false
        }
        return flag
    }

    fun checkUserName(text: String?): Boolean {
        var flag = true
        if (text == null) {
            flag = false
        } else if (text.isEmpty()) {
            flag = false
        } else if (text.length < 2 || text.length > 32) {
            flag = false
        }
        return flag
    }

    fun checkPassword(password: String?, checkStrongValidation: Boolean = true): Boolean {
        var flag = true
        if (password == null) {
            flag = false
        } else if (password.isEmpty()) {
            flag = false
        } else if (!isValidPassword(password)) {
            flag = false
        }
//        else if (!isValidPasswordPattern(password) && checkStrongValidation) {
//            flag = false
//        }
        return flag
    }

    fun isNotBlank(text: String): Boolean {
        var flag = true
        if (text.isEmpty()) {
            flag = false
        }
        return flag
    }

    fun checkMobile(text: String): Boolean {
        var flag = true
        if (text.matches("[0-9]+".toRegex()) && text.length > 2) {
            if (text.isEmpty()) {
                flag = false
            } else if (text.length <= 9) {
                flag = false
            }
        } else {
            flag = false
        }
        return flag
    }

    fun checkConfPass(pass: String, cpass: String): Boolean {
        var flag = true
        if (cpass.isEmpty()) {
            flag = false
        } else if (pass != cpass) {
            flag = false
        }
        return flag
    }

    fun isValidPassword(password: String): Boolean {
        var flag = true
        if (password.length < 8 || password.length > 24) {
            flag = false
        }
        return flag
    }

    fun checkPasswordValidation(password: String?): Boolean {
        var flag = true
        if (password!!.isEmpty()) {
            flag = false
        } else if (!Pattern.compile(PASSWORD).matcher(password).matches()) {
            flag = false
        }
        return flag
    }

    fun isValidPasswordPattern(input: String): Boolean {
        val regex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#\$%^&+=])(?=\\S+\$).{8,24}\$".toRegex()
        return regex.matches(input)

    }

    fun getTrimtext(et: EditText): String {
        return et.text.toString().trim { it <= ' ' }
    }

    fun checkUsername(username: String): Boolean {
        var flag = true
        if (username.isEmpty()) {
            flag = false
        } else if (username.length < 2 && username.length > 32) {
            flag = false
        }
        return flag
    }

    fun checkProductName(text: String): Boolean {
        var flag = true
        if (text == null) {
            flag = false
        } else if (text.isEmpty()) {
            flag = false
        } else if (text.length !in 2..100) {
            flag = false
        }
        return flag
    }
    fun isValidUrlS(url: String): Boolean {
        return url.matches(VALID_URL.toRegex())
    }

}