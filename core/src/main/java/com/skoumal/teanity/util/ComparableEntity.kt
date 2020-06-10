package com.skoumal.teanity.util

interface ComparableEntity<in T> {

    /**
     * ## Definition
     *
     * [sameAs] defines as this object being the **exact same** as the [other] compared to it. You
     * are strongly compelled to use instance equality checks for this method otherwise you might
     * encounter issues in RecyclerView etc.
     *
     * ### Suggested usage:
     * ```kotlin
     * override fun sameAs(other: T) = this === other
     * ```
     * */
    fun sameAs(other: T): Boolean

    /**
     * ## Definition
     *
     * [contentSameAs] should be by its nature very lenient in resolving content equality. You
     * should always compare only data visible to the user to avoid excessive redraw and very long
     * list diff resolution.
     *
     * ### Suggested usage:
     * ```kotlin
     * override fun contentSameAs(other: out T) = other.compareTo<T> {
     *      name == it.name && date == it.date
     * }
     * ```
     * */
    fun contentSameAs(other: T): Boolean

}