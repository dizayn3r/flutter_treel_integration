package com.example.flutter_treel_integration

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "treel_sdk_channel"
    private lateinit var treelHandler: TreelHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        treelHandler = TreelHandler(this)

        // Setup Flutter MethodChannel
        flutterEngine?.dartExecutor?.binaryMessenger?.let {
            MethodChannel(it, CHANNEL)
                .setMethodCallHandler { call, result ->
                    when (call.method) {
                        "startScanning" -> {
                            val response = treelHandler.startScanning()
                            result.success(response)
                        }

                        "syncVehicleConfig" -> {
                            val configs = call.arguments as? List<Map<String, Any>>
                            val response = treelHandler.syncVehicleConfigurations(configs ?: emptyList())
                            result.success(response)
                        }

                        "fetchTpmsData" -> {
                            val vinNumber = call.arguments as? String ?: ""
                            val response = treelHandler.fetchTpmsData(vinNumber)
                            result.success(response)
                        }

                        else -> result.notImplemented()
                    }
                }
        }
    }
}
