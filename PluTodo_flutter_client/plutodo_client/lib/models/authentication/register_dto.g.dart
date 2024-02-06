// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'register_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RegisterDto _$RegisterDtoFromJson(Map<String, dynamic> json) => RegisterDto(
      json['username'] as String,
      json['emailAddress'] as String,
      json['password'] as String,
      json['passwordConfirm'] as String,
    );

Map<String, dynamic> _$RegisterDtoToJson(RegisterDto instance) =>
    <String, dynamic>{
      'username': instance.username,
      'emailAddress': instance.emailAddress,
      'password': instance.password,
      'passwordConfirm': instance.passwordConfirm,
    };
