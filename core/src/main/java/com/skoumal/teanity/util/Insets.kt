package com.skoumal.teanity.util

data class Insets(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
) {

    companion object {
        val empty: Insets? = null
    }

}