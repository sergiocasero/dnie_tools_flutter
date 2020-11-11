import 'dart:async';
import 'dart:convert';

import 'package:dnie_tools/DnieResponse.dart';
import 'package:flutter/services.dart';

class DnieTools {
  static const MethodChannel _channel = const MethodChannel('dnie_tools');

  static StreamController<DnieResponse> _controller =
      StreamController.broadcast();

  // final Function(DnieStatus) onStatusChange;
  // final Function(DnieInfo) onDnieInfo;

  static Future<void> read(String can) async {
    await Future.delayed(Duration(seconds: 2));
    _channel.setMethodCallHandler((call) {
      switch (call.method) {
        case "onNfcResponse":
          _controller.add(DnieResponse.fromJson(jsonDecode(call.arguments)));
          break;
      }
      return Future.delayed(Duration());
    });

    await _channel.invokeMethod("readDnie", {'can': can});
  }

  static Stream get stream => _controller.stream;
}
