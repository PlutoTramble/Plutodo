import 'package:json_annotation/json_annotation.dart';

part 'category.g.dart';

@JsonSerializable()
class Category {
  Category();

  late int id;
  late String name;
  late int ordering;

  @JsonKey(includeFromJson: false, includeToJson: false)
  late bool selected = false;

  factory Category.fromJson(Map<String, dynamic> json) => _$CategoryFromJson(json);
  Map<String, dynamic> toJson() => _$CategoryToJson(this);

  factory Category.init(int id, String name, int ordering) =>
    Category()
      ..id = id
      ..name = name
      ..ordering = ordering;

}