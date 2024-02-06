import 'package:json_annotation/json_annotation.dart';

part 'category.g.dart';

@JsonSerializable()
class Category {
  Category();

  late String? id;
  late String name;
  late String color;

  @JsonKey(includeFromJson: false, includeToJson: false)
  late bool selected = false;

  factory Category.fromJson(Map<String, dynamic> json) => _$CategoryFromJson(json);
  Map<String, dynamic> toJson() => _$CategoryToJson(this);

  factory Category.init(String id, String name, String color) =>
    Category()
      ..id = id
      ..name = name
      ..color = color;
}