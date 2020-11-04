import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/pigeon.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Embedded Flutter',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String text = "no action call";
  static const String nameChannel = 'ru.test.embedding/hello';
  static const platform = const MethodChannel(nameChannel);

  SearchRequest request = SearchRequest()..query = "Aaron";
  Api api = Api();

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
    api.search(request);
    // platform.invokeMapMethod("counter", _counter);
  }

  @override
  void initState() {
    platform.setMethodCallHandler((call) {
      switch(call.method){
        case "hello":{
          setState(() {
            text = text + "\nmethod=${call.method}, arguments=${call.arguments}";
            pushHelloMessage();
          });
        }
      }
      var result;
      return result;
    });
    setState(() {
      text = "method channel init as \"$nameChannel\"";
    });

    super.initState();
  }

  void pushHelloMessage() async {
    await Future.delayed(Duration(seconds: 3));
    platform.invokeMapMethod("hello", "from flutter module");
    setState(() {
      text = text + "\nmessage to native is sent";
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(text),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
