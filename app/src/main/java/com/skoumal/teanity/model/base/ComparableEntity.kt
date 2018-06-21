package com.skoumal.teanity.model.base

interface ComparableEntity<in T> {

    fun sameAs(other: T): Boolean
    fun contentSameAs(other: T): Boolean
}