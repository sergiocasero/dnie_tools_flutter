package com.sergiocasero.dnie_tools

import android.app.Activity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/** DnieToolsPlugin */
class DnieToolsPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel

    private val dnieReader by lazy {
        DnieReader(activity = activity) {
            channel.invokeMethod("onNfcResponse", when (it) {
                DnieResponse.Initializing ->
                    """
                        {
                            "status": "INIT",
                            "type": null,
                            "uri": null,
                            "data": null
                        }
                    """
                DnieResponse.Ready ->
                    """
                        {
                            "status": "READY",
                            "type": null,
                            "uri": null,
                            "data": null
                        }
                    """
                is DnieResponse.InProgress ->
                    """
                        {
                            "status": "IN_PROGRESS",
                            "type": "${it.type}",
                            "uri": null,
                            "data": null
                        }
                    """.trimIndent()
                is DnieResponse.DnieInfo ->
                    """
                        {
                            "status": "INFO",
                            "type": null,
                            "uri": null,
                            "data": {
                                "nif": "${it.nif}",
                                "birth": "${it.birth}",
                                "expiry": "${it.expiry}",
                                "docNumber": "${it.docNumber}",
                                "expeditionCountry": "${it.expeditionCountry}",
                                "sex": "${it.sex}",
                                "nationality": "${it.nationality}",
                                "name": "${it.name}",
                                "surname": "${it.surname}",
                                "address": "${it.address}",
                                "city": "${it.city}",
                                "state": "${it.state}"
                            }
                        }
                    """.trimIndent()
                is DnieResponse.Image ->
                    """
                        {
                            "status": "IMAGE",
                            "type": "${it.imageType}",
                            "uri": "${it.image}",
                            "data": null
                        }
                    """.trimIndent()
                is DnieResponse.Error ->
                    """
                        {
                            "status": "ERROR",
                            "type": "${it.message}",
                            "uri": ": null,
                            "data": null
                        }
                    """.trimIndent()
            })
        }
    }

    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dnie_tools")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "readDnie") {
            result.success("")
            GlobalScope.launch {
                call.argument<String>("can")?.let {
                    dnieReader.resume(it)
                }
            }
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        // Nothing to do
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        // Nothing to do
    }

    override fun onDetachedFromActivity() {
        dnieReader.stop()
    }
}
