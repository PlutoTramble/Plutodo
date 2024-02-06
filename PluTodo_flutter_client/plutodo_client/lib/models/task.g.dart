// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'task.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Task _$TaskFromJson(Map<String, dynamic> json) => Task()
  ..id = json['id'] as String
  ..name = json['name'] as String
  ..description = json['description'] as String?
  ..isFinished = json['isFinished'] as bool
  ..dateCreated = json['dateCreated'] as String
  ..dateDue = json['dateDue'] as String?
  ..categoryId = json['categoryId'] as String?;

Map<String, dynamic> _$TaskToJson(Task instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'description': instance.description,
      'isFinished': instance.isFinished,
      'dateCreated': instance.dateCreated,
      'dateDue': instance.dateDue,
      'categoryId': instance.categoryId,
    };
