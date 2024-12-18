import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

import 'treel_sdk.dart';

class TreelSdkPage extends StatefulWidget {
  const TreelSdkPage({super.key});

  @override
  TreelSdkPageState createState() => TreelSdkPageState();
}

class TreelSdkPageState extends State<TreelSdkPage> {
  @override
  void initState() {
    requestNotificationPermission();
    super.initState();
  }

  String _status = "Waiting...";

  void _startScanning() async {
    final result = await TreelSdk.startScanning();
    setState(() => _status = result);
  }

  void _syncConfig() async {
    final configs = [
      {
        "vinNumber": "VIN12345",
        "macAddress": "00:11:22:33:44:55",
        "tyrePosition": "FrontLeft",
        "recommendedPressureSetPoint": 32.0,
        "lowPressureSetPoint": 28.0,
        "highPressureSetPoint": 36.0,
        "highTemperatureSetPoint": 100.0,
      }
    ];
    final result = await TreelSdk.syncVehicleConfig(configs);
    setState(() => _status = result);
  }

  void _fetchData() async {
    final result = await TreelSdk.fetchTpmsData("VIN12345");
    setState(() => _status = result);
  }

  void requestNotificationPermission() async {
    if (await Permission.notification.isDenied) {
      await Permission.notification.request();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('TREEL SDK Integration')),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text('Status: $_status'),
          ElevatedButton(
              onPressed: _startScanning, child: const Text("Start Scanning")),
          ElevatedButton(
              onPressed: _syncConfig, child: const Text("Sync Vehicle Config")),
          ElevatedButton(
              onPressed: _fetchData, child: const Text("Fetch TPMS Data")),
        ],
      ),
    );
  }
}
