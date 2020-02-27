package com.skoumal.teanity.tools.annotation

@Retention(AnnotationRetention.BINARY)
annotation class RemoveOnDeprecation(
    val projectedVersion: String
)