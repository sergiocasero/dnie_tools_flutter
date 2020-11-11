import 'dart:async';

import 'package:dnie_tools_flutter/dnie_tools_flutter.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _can = "";

  @override
  void initState() {
    super.initState();
    _initializeDNIe();
  }

  Future<void> _initializeDNIe() async {
    DnieTools.stream.listen((event) {
      print(event);
    });

    await DnieTools.read(_can);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('DNIe plugin app'),
        ),
        body: Center(child: Text("")),
      ),
    );
  }
}
