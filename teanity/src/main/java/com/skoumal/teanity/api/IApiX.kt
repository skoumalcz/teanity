package com.skoumal.teanity.api

/**
 * Extendable class that provides standard kotlin-dsl interface for setting up API calls. This can be used in a variety
 * of ways, but of course depends on project setup and developer preference.
 *
 * **Variables**
 * * [offset] - defaults to [OFFSET_START]
 * * [limit] - defaults to [GENERIC_LIMIT]
 * */
interface IApiX {

    var offset: Int
    var limit: Int

    /**
     * Counts current page based on [offset] specified earlier.
     *
     * Can be overridden as such:
     *
     * ```kotlin
     *      override val page get() = super.page + 1
     * ```
     *
     * to achieve a natural number sequence.
     * */
    val page: Int get() = offset / limit

    companion object {
        const val OFFSET_START = 0
        const val GENERIC_LIMIT = 20
    }

    class Default : IApiX {

        override var offset: Int = OFFSET_START
        override var limit: Int = GENERIC_LIMIT

    }

}