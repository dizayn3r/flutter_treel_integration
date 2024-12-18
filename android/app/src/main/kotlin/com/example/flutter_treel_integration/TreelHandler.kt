package com.example.flutter_treel_integration

import android.content.Context
import com.treel.androidsdk.TreelTagScan
import com.treel.androidsdk.data.constant.VehicleType
import com.treel.androidsdk.data.database.VehicleConfiguration
import com.treel.androidsdk.event.EventCallbackListener
import com.treel.androidsdk.exception.BleScanException
import com.treel.androidsdk.notification.AlertNotification
import io.flutter.plugin.common.MethodChannel

class TreelHandler(private val context: Context) : EventCallbackListener {

    private val treelTagScan: TreelTagScan = TreelTagScan(context).apply {
        addOnEventCallbackListener(this@TreelHandler)
    }

    // Start BLE Scanning
    fun startScanning(): String {
        return try {
            treelTagScan.startBleScanning(context)
            "Scanning Started Successfully"
        } catch (e: Exception) {
            "Error Starting Scanning: ${e.message}"
        }
    }

    // Sync Vehicle Configurations
    fun syncVehicleConfigurations(configList: List<Map<String, Any>>): String {
        return try {
            val configurations = ArrayList<VehicleConfiguration>()

            for (config in configList) {
                val vehicleConfig = VehicleConfiguration().apply {
                    vinNumber = config["vinNumber"] as? String ?: ""
                    macAddress = config["macAddress"] as? String ?: ""
                    tyrePosition = config["tyrePosition"] as? String ?: ""
                    recommendedPressureSetPoint = (config["recommendedPressureSetPoint"] as? Double)?.toInt() ?: 32
                    lowPressureSetPoint = (config["lowPressureSetPoint"] as? Double)?.toInt() ?: 28
                    highPressureSetPoint = (config["highPressureSetPoint"] as? Double)?.toInt() ?: 36
                    highTemperatureSetPoint = (config["highTemperatureSetPoint"] as? Double)?.toInt() ?: 100
                    vehicleType = VehicleType.CAR.toString()
                }
                configurations.add(vehicleConfig)
            }

            treelTagScan.syncVehicleConfigurations(configurations)
            "Vehicle Configurations Synced Successfully"
        } catch (e: Exception) {
            "Error Syncing Configurations: ${e.message}"
        }
    }

    private fun syncVehicleConfig(arguments: Any?, result: MethodChannel.Result) {
        try {
            val configurations = ArrayList<VehicleConfiguration>()

            val config = VehicleConfiguration().apply {
                vinNumber = "VIN12345"
                macAddress = "00:11:22:33:44:55"
                tyrePosition = "FrontLeft"
                recommendedPressureSetPoint = 32
                lowPressureSetPoint = 28
                highPressureSetPoint = 36
                highTemperatureSetPoint = 100
                vehicleType = VehicleType.CAR.toString()
            }
            configurations.add(config)

            treelTagScan?.syncVehicleConfiguration(configurations)
            result.success("Vehicle Configuration Synced")
        } catch (e: Exception) {
            result.error("SYNC_CONFIG_ERROR", "Failed to sync vehicle configurations", e.message)
        }
    }

    // Fetch Latest TPMS Data
    fun fetchTpmsData(vinNumber: String): String {
        return try {
            val tpmsData = treelTagScan.fetchLatestTpmsData(vinNumber)
            tpmsData?.toString() ?: "No TPMS Data Found"
        } catch (e: Exception) {
            "Error Fetching TPMS Data: ${e.message}"
        }
    }

    // Event Callback Implementations
    override fun handleBleException(errorCode: Int?) {
        val errorMessage = when (errorCode) {
            BleScanException.SCAN_FAILED_CONFIGURATION_NOT_AVAILABLE -> "Sensor Configuration Not Available"
            BleScanException.BLUETOOTH_NOT_AVAILABLE -> "Bluetooth is not available"
            BleScanException.BLUETOOTH_DISABLED -> "Bluetooth is disabled. Please enable it."
            BleScanException.LOCATION_PERMISSION_MISSING -> "Location permission is missing."
            BleScanException.LOCATION_SERVICES_DISABLED -> "Location services are disabled."
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> "Scan is already started."
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> "Internal error occurred during scanning."
            else -> "Unknown BLE Exception"
        }
        println("BLE Exception: $errorMessage")
    }

    override fun onTpmsDataReceived(vehicleConfiguration: VehicleConfiguration) {
        println("TPMS Data Received: ${vehicleConfiguration.vinNumber}, Pressure: ${vehicleConfiguration.recommendedPressureSetPoint}")
    }

    override fun showAlertNotification(alertNotification: AlertNotification) {
        println("Alert Notification: ${alertNotification.getAlertsText()}")
    }

    override fun onSyncTpmsDataToCloud(message: String) {
        println("Sync TPMS Data to Cloud: $message")
    }
}
