package com.skoumal.teanity.test.ui

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasErrorResource(expectedError: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) = Unit
    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextInputLayout) {
            return false
        }

        return item.error == item.resources.getString(expectedError)
    }
}

fun withDrawable(resourceId: Int): Matcher<View> {
    return DrawableMatcher(resourceId)
}

fun noDrawable(): Matcher<View> {
    return DrawableMatcher(-1)
}

class DrawableMatcher(private val resourceId: Int) : TypeSafeMatcher<View>() {

    private var resName: String? = null

    override fun matchesSafely(target: View): Boolean {
        if (target !is ImageView) {
            return false
        }
        if (resourceId < 0) {
            return target.drawable == null
        }

        val expectedDrawable = ContextCompat.getDrawable(target.context, resourceId) ?: return false
        resName = target.context.resources.getResourceEntryName(resourceId)

        val bitmap = target.drawable.toBitmap()
        val otherBitmap = expectedDrawable.toBitmap()
        return bitmap.sameAs(otherBitmap)
    }

    override fun describeTo(description: Description) {
        description.apply {
            appendText("with drawable from resource id: ")
            appendValue(resourceId)
            resName?.let {
                appendText("[")
                appendText(it)
                appendText("]")
            }
        }
    }
}

fun withTitle(expectedTitle: Int, vararg args: Any) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("Title with resource id: ")
            appendValue(expectedTitle)
        }
    }

    override fun matchesSafely(item: View?) = when (item) {
        is Toolbar -> item.title == item.resources.getString(expectedTitle, args)
        is CollapsingToolbarLayout -> item.title == item.resources.getString(expectedTitle, args)
        else -> false
    }
}

fun withTitle(expectedTitle: String) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("Title with resource id: ")
            appendValue(expectedTitle)
        }
    }

    override fun matchesSafely(item: View?) = when (item) {
        is Toolbar -> item.title == expectedTitle
        is CollapsingToolbarLayout -> item.title == expectedTitle
        else -> false
    }
}

fun withSubtitle(expectedTitle: Int, vararg args: Any) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("Subtitle with resource id: ")
            appendValue(expectedTitle)
        }
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is Toolbar) {
            return false
        }

        return item.subtitle == item.resources.getString(expectedTitle, args)
    }
}

fun withMenuItem(expectedItem: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("Menu item with resource id: ")
            appendValue(expectedItem)
        }
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is Toolbar) return false

        return item.menu.children.any { it.itemId == expectedItem }
    }
}

fun withLayout(expectedLayout: Int) = object : TypeSafeMatcher<RecyclerView.ViewHolder>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("ViewHolder with layout id: ")
            appendValue(expectedLayout)
        }
    }

    override fun matchesSafely(item: RecyclerView.ViewHolder?): Boolean {
        //will not work unless passed viewtype as layout id
        return item?.itemViewType == expectedLayout
    }
}

fun withPosition(position: Int) = object : TypeSafeMatcher<RecyclerView.ViewHolder>() {
    override fun describeTo(description: Description?) {
        description?.apply {
            appendText("ViewHolder with position: ")
            appendValue(position)
        }
    }

    override fun matchesSafely(item: RecyclerView.ViewHolder?): Boolean {
        return item?.layoutPosition == position
    }
}