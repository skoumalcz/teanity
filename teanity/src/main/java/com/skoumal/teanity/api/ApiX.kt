package com.skoumal.teanity.api

import androidx.annotation.WorkerThread
import io.reactivex.*
import kotlin.reflect.KClass

/**
 * @since 0.3
 */
class ApiXException @JvmOverloads constructor(
    message: String, cause: Throwable? = null
) : IllegalStateException(message, cause)

/**
 * ### Use in conjunction with [build], you shouldn't ever need to create it yourself
 *
 * Allows for wrapping builder evaluation within [ObservableSource]
 *
 * @since 0.3
 */
class ApiX<Super : ApiXBuilder<Super>>(
    private val builder: Super
) {

    @WorkerThread
    private fun constructCallable() {
        if (!builder.onEvaluateCallback(builder))
            throw ApiXException("Api values do not comply with evaluation preconditions.")
    }

    private fun asCallable() = Completable.fromCallable(this::constructCallable)

    /** Evaluates conditions set by [ApiXBuilder.onEvaluate] or returns success if none set; wherever preconditions match fires [result] */
    fun <T> afterEvaluate(result: Observable<T>): Observable<T> = asCallable().andThen(result)

    /** @see afterEvaluate */
    fun <T> afterEvaluate(result: Single<T>): Single<T> = asCallable().andThen(result)

    /** @see afterEvaluate */
    fun <T> afterEvaluate(result: Maybe<T>): Maybe<T> = asCallable().andThen(result)

    /** @see afterEvaluate */
    fun <T> afterEvaluate(result: Flowable<T>): Flowable<T> = asCallable().andThen(result)

    /** @see afterEvaluate */
    fun afterEvaluate(result: Completable): Completable = asCallable().andThen(result)


    /** @see afterEvaluate */
    fun <T> afterEvaluateObservable(result: ObservableResult<Super, T>): Observable<T> =
        asCallable().andThen(result(builder))

    /** @see afterEvaluate */
    fun <T> afterEvaluateSingle(result: SingleResult<Super, T>): Single<T> = asCallable().andThen(result(builder))

    /** @see afterEvaluate */
    fun <T> afterEvaluateMaybe(result: MaybeResult<Super, T>): Maybe<T> = asCallable().andThen(result(builder))

    /** @see afterEvaluate */
    fun <T> afterEvaluateFlowable(result: FlowableResult<Super, T>): Flowable<T> = asCallable().andThen(result(builder))

    /** @see afterEvaluate */
    fun afterEvaluateCompletable(result: CompletableResult<Super>): Completable = asCallable().andThen(result(builder))
}

typealias ObservableResult<Type, ReturnType> = Type.() -> Observable<ReturnType>
typealias SingleResult<Type, ReturnType> = Type.() -> Single<ReturnType>
typealias MaybeResult<Type, ReturnType> = Type.() -> Maybe<ReturnType>
typealias FlowableResult<Type, ReturnType> = Type.() -> Flowable<ReturnType>
typealias CompletableResult<Type> = Type.() -> Completable

/**
 * Extendable class that allows plug-in evaluation of values downstream in observable environment without chance to cause crash. (Under a condition that Observable has Error subscriber)
 *
 * To build this class use [build] extension function (*Returns [ApiX]*) which allows a seamless predefined evaluation within Observable; otherwise evaluation of set preconditions will not happen.
 *
 * @since 0.3
 * */
abstract class ApiXBuilder<SuperClass : ApiXBuilder<SuperClass>> {

    internal var onEvaluateCallback: EvaluateCallback<SuperClass> = { true }

    fun onEvaluate(callback: EvaluateCallback<SuperClass>) {
        onEvaluateCallback = callback
    }

    abstract class Creator<Parent : Any>(private val cls: KClass<Parent>) {
        operator fun invoke(body: Parent.() -> Unit) = create(cls).apply(body)

        private fun <Child : Any> create(cls: KClass<Child>): Child = cls.java.newInstance()
    }

}

typealias EvaluateCallback<SuperClass> = SuperClass.() -> Boolean

/**
 * Builds [ApiXBuilder] and returns [ApiX] with [Super] type token. This function is designed to allow chaining within repositories
 *
 * @since 0.3
 * */
inline fun <reified Super : ApiXBuilder<Super>> Super.build() = ApiX(this)