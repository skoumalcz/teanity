package com.skoumal.teanity.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.util.SubjectsToChange
import com.skoumal.teanity.viewevent.GenericNavDirections
import com.skoumal.teanity.viewevent.NavigationEvent
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ContextExecutor
import com.skoumal.teanity.viewevent.base.FragmentExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SubjectsToChange
interface TeanityBinding<Binding : ViewDataBinding> {

    val layoutRes: Int
    val binding: Binding

    fun ensureBinding(activity: Activity): Binding
    fun ensureBinding(fragment: Fragment, parent: ViewGroup?): Binding

    companion object {
        fun <Binding : ViewDataBinding> getImpl() = TeanityBindingImpl<Binding>()
    }

}

@SubjectsToChange
class TeanityBindingImpl<Binding : ViewDataBinding> internal constructor() :
    TeanityBinding<Binding> {

    override val layoutRes: Int get() = TODO("Not implemented")
    override lateinit var binding: Binding
        private set

    override fun ensureBinding(activity: Activity): Binding = DataBindingUtil
        .setContentView<Binding>(activity, layoutRes)
        .also { binding = it }

    override fun ensureBinding(fragment: Fragment, parent: ViewGroup?): Binding = DataBindingUtil
        .inflate<Binding>(fragment.layoutInflater, layoutRes, parent, true)
        .also { binding = it }

}

@SubjectsToChange
interface TeanityLifecycle {

    fun getLifecycleChild(): TeanityLifecycle? = null

    @CallSuper
    fun saveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
        getLifecycleChild()?.saveInstanceState(outState)
    }

    @CallSuper
    fun restoreInstanceState(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(this, savedInstanceState)
        getLifecycleChild()?.restoreInstanceState(savedInstanceState)
    }
}

@SubjectsToChange
interface TeanityNavigation {
    val navHostId: Int

    fun Activity.getNavController() = findNavController(navHostId)
    fun Fragment.getNavController() = findNavController()

    fun Activity.navigate(event: NavigationEvent) {
        getNavController().navigate(event.navDirections, event.navOptions, event.getExtras(this))
        when (val d = event.navDirections) {
            is GenericNavDirections -> if (d.clearTask) finish()
        }
    }

    fun Fragment.navigate(event: NavigationEvent) {
        getNavController().navigate(event.navDirections, event.navOptions, event.getExtras(this))
        when (val d = event.navDirections) {
            is GenericNavDirections -> if (d.clearTask) activity?.finish()
        }
    }
}

@SubjectsToChange
interface TeanityEventDistributor : CoroutineScope {

    val events: Channel<ViewEvent>

    fun <T : ViewEvent> T.publish() {
        launch { events.send(this@publish) }
    }

    companion object {
        val impl get() = TeanityEventDistributorImpl()
    }
}

@SubjectsToChange
class TeanityEventDistributorImpl internal constructor() :
    TeanityEventDistributor,
    CoroutineScope by MainScope() {
    override val events = Channel<ViewEvent>()
}

@SubjectsToChange
interface TeanityEventCollector : CoroutineScope {
    val distributor: TeanityEventDistributor

    fun collect() {
        launch {
            distributor.events.consumeAsFlow().onEach {
                when (this@TeanityEventCollector) {
                    is AppCompatActivity -> execute(it)
                    is Fragment -> execute(it)
                }
            }.launchIn(this)
        }
    }

    fun Fragment.execute(event: ViewEvent) {
        activity?.execute(event) ?: context?.execute(event)
        when (event) {
            is FragmentExecutor -> kotlin.runCatching { event(this) }
                .onFailure { event.onFailure(it) }
        }
    }

    fun AppCompatActivity.execute(event: ViewEvent) {
        (this as Context).execute(event)
        when (event) {
            is ActivityExecutor -> kotlin.runCatching { event(this) }
                .onFailure { event.onFailure(it) }
        }
    }

    fun Context.execute(event: ViewEvent) {
        when (event) {
            is ContextExecutor -> kotlin.runCatching { event(this) }
                .onFailure { event.onFailure(it) }
        }
    }
}

@SubjectsToChange
interface TeanityUI {
    val ui: TeanityUICollector

    fun peekSystemWindowInsets(insets: Insets) = Unit
    fun consumeSystemWindowInsets(insets: Insets): Insets? = null

    fun requireInsets(root: View) {
        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            insets.asInsets()
                .also { peekSystemWindowInsets(it) }
                .let { consumeSystemWindowInsets(it) }
                ?.also { ui.insets.postValue(it) }
                ?.subtractBy(insets) ?: insets
        }
        if (ViewCompat.isAttachedToWindow(root)) {
            ViewCompat.requestApplyInsets(root)
        } else {
            root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(v: View) = Unit
                override fun onViewAttachedToWindow(v: View) {
                    ViewCompat.requestApplyInsets(v)
                }
            })
        }
    }

    private fun WindowInsetsCompat.asInsets() = Insets.of(
        systemWindowInsetLeft,
        systemWindowInsetTop,
        systemWindowInsetRight,
        systemWindowInsetBottom
    )

    private fun Insets.subtractBy(insets: WindowInsetsCompat) = insets.replaceSystemWindowInsets(
        insets.systemWindowInsetLeft - left,
        insets.systemWindowInsetTop - top,
        insets.systemWindowInsetRight - right,
        insets.systemWindowInsetBottom - bottom
    )
}

@SubjectsToChange
interface TeanityUICollector {

    val insets: MutableLiveData<Insets>

    companion object {
        val impl get() = TeanityUICollectorImpl()
    }
}

@SubjectsToChange
class TeanityUICollectorImpl internal constructor() : TeanityUICollector {
    override val insets = MutableLiveData<Insets>(Insets())
}