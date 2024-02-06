import 'package:json_annotation/json_annotation.dart';

part 'register_dto.g.dart';

@JsonSerializable()
class RegisterDto {
  RegisterDto(this.username, this.emailAddress, this.password, this.passwordConfirm);

  final String username;
  final String emailAddress;
  final String password;
  final String passwordConfirm;

  factory RegisterDto.fromJson(Map<String, dynamic> json) => _$RegisterDtoFromJson(json);
  Map<String, dynamic> toJson() => _$RegisterDtoToJson(this);
}