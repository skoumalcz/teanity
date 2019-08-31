package com.skoumal.teanity.extensions

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.skoumal.teanity.util.Text
import com.skoumal.teanity.viewevents.SnackbarEvent

@Deprecated("Method has way too much arguments, use builder instead. This method will be removed in future versions.")
fun AppCompatActivity.requestPermission(
    permission: String,
    explanation: String,
    actionTitle: String,
    view: View,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) = requestPermissions {
    permissions(permission)

    onGranted { onGranted() }
    onDenied { onDenied() }

    onPermissionRationaleRequested { _, token -> token.continuePermissionRequest() }
    onPermanentlyDenied {
        SnackbarEvent {
            message = Text.Sequence(explanation)

            action {
                text = Text.Sequence(actionTitle)
                onClicked {
                    val appSettings = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        "package:$packageName".toUri()
                    ).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    startActivity(appSettings)
                }
            }
        }.consume(view, this@requestPermission)
    }
}

@Deprecated("Method lacks abstraction since it only copies [requestPermission]. This method will be removed in future versions.")
fun AppCompatActivity.requestPermissionSoft(
    permission: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) = requestPermissions {
    permissions(permission)

    onGranted { onGranted() }
    onDenied { onDenied() }
}

inline fun Activity.requestPermissions(body: RequestPermissionBuilder.() -> Unit) =
    RequestPermissionBuilder(this, body)

class RequestPermissionBuilder(private val activity: Activity) {

    private var onGrantedListener: OnPermissionGrantedListener = {}
    private var onDeniedListener: OnPermissionDeniedListener = {}
    private var onPermanentlyDeniedListener: OnPermissionDeniedListener = {}
    private var onRationaleRequestListener: OnPermissionsRationaleRequest = { _, token ->
        token.cancelPermissionRequest()
    }

    var permissions: List<String> = listOf()

    fun permissions(vararg permissions: String) {
        this.permissions = permissions.toList()
    }

    fun onGranted(listener: OnPermissionGrantedListener) {
        onGrantedListener = listener
    }

    fun onDenied(listener: OnPermissionDeniedListener) {
        onDeniedListener = listener
    }

    fun onPermanentlyDenied(listener: OnPermissionDeniedListener) {
        onPermanentlyDeniedListener = listener
    }

    fun onPermissionRationaleRequested(listener: OnPermissionsRationaleRequest) {
        onRationaleRequestListener = listener
    }

    internal fun build() = Dexter.withActivity(activity)
        .withPermissions(*permissions.toTypedArray())
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                val grantedResponses = report?.grantedPermissionResponses.orEmpty()
                val deniedResponses = report?.deniedPermissionResponses.orEmpty()

                if (grantedResponses.isNotEmpty()) {
                    onGrantedListener(grantedResponses)
                }
                if (deniedResponses.isNotEmpty()) {
                    onDeniedListener(deniedResponses)
                }
                if (report?.isAnyPermissionPermanentlyDenied == true) {
                    val permanentlyDeniedResponses =
                        deniedResponses.filter { it.isPermanentlyDenied }
                    onPermanentlyDeniedListener(permanentlyDeniedResponses)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                onRationaleRequestListener(permissions.orEmpty(), token ?: return)
            }
        })
        .check()

    companion object {
        internal inline operator fun invoke(
            activity: Activity,
            builder: RequestPermissionBuilder.() -> Unit
        ) = RequestPermissionBuilder(activity).apply(builder).build()
    }

}

typealias OnPermissionGrantedListener = (List<PermissionGrantedResponse>) -> Unit
typealias OnPermissionDeniedListener = (List<PermissionDeniedResponse>) -> Unit
typealias OnPermissionsRationaleRequest = (List<PermissionRequest>, PermissionToken) -> Unit