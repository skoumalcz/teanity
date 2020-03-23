@file:Suppress("unused")

package com.skoumal.teanity.tools.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS)
@Retention(AnnotationRetention.BINARY)
annotation class RemoveOnDeprecation(
    val projectedVersion: String
)