import 'package:flutter/services.dart';

class TreelSdk {
  static const MethodChannel _channel = MethodChannel('treel_sdk_channel');

  static Future<String> startScanning() async {
    return await _channel.invokeMethod('startScanning');
  }

  static Future<String> syncVehicleConfig(
      List<Map<String, dynamic>> configs) async {
    return await _channel.invokeMethod('syncVehicleConfig', configs);
  }

  static Future<String> fetchTpmsData(String vinNumber) async {
    return await _channel.invokeMethod('fetchTpmsData', vinNumber);
  }
}
