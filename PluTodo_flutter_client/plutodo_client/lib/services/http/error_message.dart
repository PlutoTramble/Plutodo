import 'package:json_annotation/json_annotation.dart';


part 'error_message.g.dart';


@JsonSerializable()
class ErrorMessage {
  ErrorMessage();

  late String message;

  factory ErrorMessage.fromJson(Map<String, dynamic> json) => _$ErrorMessageFromJson(json);
}