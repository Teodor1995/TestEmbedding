import 'package:pigeon/pigeon_lib.dart';

// flutter pub run pigeon --input lib/pigeons/message.dart --dart_out lib/pigeon.dart --java_out ../../TestEmbedding/app/src/main/java/dev/flutter/pigeon/Pigeon.java --java_package "dev.flutter.pigeon"

class Bundle {
  final int count;

  const Bundle(this.count);
}

class CounterResponse {
  final Bundle bundle;

  const CounterResponse(this.bundle);
}

@HostApi()
abstract class Api {
  void notifyNative(Bundle bundle);
}
