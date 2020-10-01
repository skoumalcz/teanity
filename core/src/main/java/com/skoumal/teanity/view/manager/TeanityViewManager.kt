package com.skoumal.teanity.view.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

interface TeanityViewManager<View, Binding : ViewDataBinding> {

    val binding: Binding

    fun attach(view: View)
    fun resume(view: View)
    fun detach()

    interface Props {

        val layoutRes: Int
        val viewModel: ViewModel

    }

    interface InsetConsumer {

        fun peekSystemWindowInsets(insets: Insets) = Unit
        fun consumeSystemWindowInsets(insets: Insets): Insets? = null

    }

    interface ViewCreator {

        fun addViewCreator(creator: Callback)
        fun removeViewCreator(creator: Callback?)

        fun createView(inflater: LayoutInflater, parent: ViewGroup?): View?

        interface Callback {

            fun createView(inflater: LayoutInflater, parent: ViewGroup?): View

        }

        companion object {

            val delegate: ViewCreator get() = ViewCreatorImpl()

        }

    }

    companion object {

        fun <A, B> activity(): TeanityViewManager<A, B>
                where A : AppCompatActivity, A : Props,
                      B : ViewDataBinding = TeanityActivityManager()

        fun <F, B> fragment(): TeanityViewManager<F, B>
                where F : Fragment, F : Props,
                      B : ViewDataBinding = TeanityFragmentManager()

    }

}

// ---

private class ViewCreatorImpl : TeanityViewManager.ViewCreator {

    private val registry = hashSetOf<TeanityViewManager.ViewCreator.Callback>()

    override fun addViewCreator(creator: TeanityViewManager.ViewCreator.Callback) {
        registry.add(creator)
    }

    override fun removeViewCreator(creator: TeanityViewManager.ViewCreator.Callback?) {
        if (creator == null) {
            registry.clear()
            return
        }

        registry.remove(creator)
    }

    override fun createView(inflater: LayoutInflater, parent: ViewGroup?): View? {
        return registry.firstOrNull()?.createView(inflater, parent)
    }

}
