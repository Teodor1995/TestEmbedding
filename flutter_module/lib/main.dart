import 'package:flutter/material.dart';
import 'package:flutter_module/blue_screen.dart';
import 'package:flutter_module/white_screen.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Embedded Flutter',
        debugShowCheckedModeBanner: false,
        themeMode: ThemeMode.system,
        theme: ThemeData.light(),
        darkTheme: ThemeData.dark(),
        initialRoute: WhiteScreen.id,
        routes: {
          WhiteScreen.id: (_) => WhiteScreen(),
          BlueScreen.id: (_) => BlueScreen(),
        });
  }
}
