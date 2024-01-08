// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'task.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Task _$TaskFromJson(Map<String, dynamic> json) => Task()
  ..id = json['id'] as int
  ..name = json['name'] as String
  ..description = json['description'] as String?
  ..finished = json['finished'] as bool
  ..dateCreated = json['dateCreated'] as String
  ..dateDue = json['dateDue'] as String?
  ..ordering = json['ordering'] as int
  ..categoryId = json['categoryId'] as int?;

Map<String, dynamic> _$TaskToJson(Task instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'description': instance.description,
      'finished': instance.finished,
      'dateCreated': instance.dateCreated,
      'dateDue': instance.dateDue,
      'ordering': instance.ordering,
      'categoryId': instance.categoryId,
    };
