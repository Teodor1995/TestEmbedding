import 'package:pigeon/pigeon_lib.dart';

class SearchRequest {
  String query;
}

class SearchReply {
  String result;
}

@HostApi()
abstract class Api {
  SearchReply search(SearchRequest request);
}

// flutter pub run pigeon --input lib/pigeons/message.dart --dart_out lib/pigeon.dart --java_out ../../TestEmbedding/app/src/main/java/dev/flutter/pigeon/Pigeon.java --java_package "dev.flutter.pigeon"