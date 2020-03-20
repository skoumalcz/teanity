@file:Suppress("unused")

package com.skoumal.teanity.tools.annotation

@RequiresOptIn(message = "Class or Method's behavior will be changed in future versions. Read planned changes and start acting now!")
@Retention(AnnotationRetention.BINARY)
annotation class SubjectsToFutureChange(val projectedVersion: String)