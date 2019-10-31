package com.skoumal.teanity.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.skoumal.teanity.util.SubjectsToChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@SubjectsToChange
abstract class TeanityCompatActivity<Binding, ViewModel>
    : AppCompatActivity(),
    CoroutineScope by MainScope(),
    TeanityBinding<Binding> by TeanityBinding.getImpl(),
    TeanityLifecycle,
    TeanityNavigation,
    TeanityEventCollector,
    TeanityUI
        where ViewModel : androidx.lifecycle.ViewModel,
              ViewModel : TeanityUICollector,
              ViewModel : TeanityEventDistributor,
              ViewModel : TeanityLifecycle,
              Binding : ViewDataBinding {

    abstract override val layoutRes: Int
    abstract val viewModel: ViewModel

    override val ui: TeanityUICollector get() = viewModel
    override val distributor: TeanityEventDistributor get() = viewModel
    override val navHostId: Int
        get() = throw IllegalStateException("You must override \"navHostId\" field.")

    override fun getLifecycleChild(): TeanityLifecycle? = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensureBinding(this).also {
            requireInsets(it.root)
        }
        onRestoreInstanceState(savedInstanceState)
        collect()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreInstanceState(savedInstanceState)
    }

}


@SubjectsToChange
abstract class TeanityCompatFragment<Binding, ViewModel> : Fragment(),
    CoroutineScope by MainScope(),
    TeanityBinding<Binding> by TeanityBinding.getImpl(),
    TeanityLifecycle,
    TeanityNavigation,
    TeanityEventCollector,
    TeanityUI
        where ViewModel : androidx.lifecycle.ViewModel,
              ViewModel : TeanityUICollector,
              ViewModel : TeanityEventDistributor,
              ViewModel : TeanityLifecycle,
              Binding : ViewDataBinding {

    abstract override val layoutRes: Int
    abstract val viewModel: ViewModel

    override val ui: TeanityUICollector get() = viewModel
    override val distributor: TeanityEventDistributor get() = viewModel
    override val navHostId: Int
        get() = throw IllegalStateException("You must override \"navHostId\" field.")

    override fun getLifecycleChild(): TeanityLifecycle? = viewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // we require context in event executors,
        // so we might use collectors immediately after it's been attached
        collect()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ensureBinding(this, container).root
        .also { requireInsets(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceState(outState)
    }

}

@SubjectsToChange
abstract class TeanityCompoundViewModel : ViewModel(),
    TeanityUICollector by TeanityUICollector.impl,
    TeanityEventDistributor by TeanityEventDistributor.impl,
    TeanityLifecycle