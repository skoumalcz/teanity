package com.skoumal.teanity.viewevent

import android.content.Context
import android.view.View
import androidx.annotation.IntDef
import com.google.android.material.snackbar.Snackbar
import com.skoumal.teanity.extensions.backgroundColor
import com.skoumal.teanity.extensions.textColor
import com.skoumal.teanity.util.Color
import com.skoumal.teanity.util.Text
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevent.base.ViewEvent
import android.graphics.Color as AColor

class SnackbarEvent private constructor(
    private val builder: Builder
) : ViewEvent() {

    fun consume(fragment: TeanityFragment<*, *>) {
        consume(fragment.snackbarView, fragment.requireContext())
    }

    fun consume(activity: TeanityActivity<*, *>) {
        consume(activity.snackbarView, activity)
    }

    fun consume(view: View, context: Context) {
        val res = context.resources
        Snackbar.make(
            view,
            builder.message.getText(res),
            builder.length
        ).also {
            val action = builder.action
            val button = action.text.getText(res)
            if (button.isBlank()) return@also

            it.setAction(button, action.onClickListener)
            it.setActionTextColor(action.color.getColor(context))
        }.also {
            val colors = builder.background?.getColor(context) ?: return@also

            it.backgroundColor(colors)
        }.also {
            val colors = builder.messageColor?.getColor(context) ?: return@also

            it.textColor(colors)
        }.show()
    }

    companion object {
        operator fun invoke(builder: Builder.() -> Unit) = SnackbarEvent(Builder().apply(builder))
    }

    class Builder {

        @IntDef(value = [Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG, Snackbar.LENGTH_INDEFINITE])
        @Retention(AnnotationRetention.SOURCE)
        annotation class Length

        @Length
        var length: Int = Snackbar.LENGTH_SHORT
        var message: Text = Text.Sequence("")
        var messageColor: Color? = null
        var background: Color? = null

        internal var action = ActionBuilder()

        fun action(body: ActionBuilder.() -> Unit) {
            action = action.apply(body)
        }

    }

    class ActionBuilder {
        var color: Color = Color.ColorInt(AColor.WHITE)
        var text = Text.Sequence("")

        internal var onClickListener: (View) -> Unit = {}

        fun onClicked(listener: (View) -> Unit) {
            onClickListener = listener
        }
    }

}