package com.skoumal.teanity.util

interface ComparableEntity<in T> {

    fun sameAs(other: T): Boolean
    fun contentSameAs(other: T): Boolean
}