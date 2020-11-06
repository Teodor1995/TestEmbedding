import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class BlueScreen extends StatefulWidget {
  static const String id = "/blue_screen";

  BlueScreen() : super();

  @override
  _BlueScreenState createState() => _BlueScreenState();
}

class _BlueScreenState extends State<BlueScreen> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue,
      appBar: AppBar(
        title: Text(BlueScreen.id),
      ),
    );
  }
}
