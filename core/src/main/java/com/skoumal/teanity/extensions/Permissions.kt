package com.skoumal.teanity.extensions

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

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