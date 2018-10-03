package com.skoumal.teanity.example

import com.chibatching.kotpref.KotprefModel

object Config : KotprefModel() {
    override val kotprefName: String = "config"

    var token by stringPref(default = "", key = "token")
}