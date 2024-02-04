import 'package:intl/intl.dart';
import 'package:json_annotation/json_annotation.dart';

part 'task.g.dart';

@JsonSerializable()
class Task {
  Task();

  late int id;
  late String name;
  late String? description;
  late bool finished;
  late String dateCreated;
  late String? dateDue;
  late int ordering;
  late int? categoryId;

  @JsonKey(includeToJson: false, includeFromJson: false)
  bool isNew = false;

  @JsonKey(includeFromJson: false, includeToJson: false)
  late bool selected = false;

  factory Task.fromJson(Map<String, dynamic> json) => _$TaskFromJson(json);
  Map<String, dynamic> toJson() => _$TaskToJson(this);

  factory Task.init(
      int id,
      String name,
      String? description,
      bool finished,
      String? dateDue,
      int ordering,
      int? categoryId) =>
      Task()
        ..id = id
        ..name = name
        ..description = description
        ..finished = finished
        ..dateDue = dateDue
        ..dateCreated = DateFormat("yyyy-MM-dd HH:mm:ss").format(DateTime.now())
        ..ordering = ordering
        ..categoryId = categoryId;
}