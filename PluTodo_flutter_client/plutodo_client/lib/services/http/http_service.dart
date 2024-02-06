import 'package:dio/dio.dart';
import 'package:plutodo_client/models/authentication/login_dto.dart';
import 'package:plutodo_client/models/authentication/register_dto.dart';
import 'package:plutodo_client/models/category.dart';
import 'package:plutodo_client/models/task.dart';
import 'package:plutodo_client/services/http/dio_fetcher.dart';

class HttpService {
  static const String _url = "http://localhost:8080";
  //static const String _url = "http://10.0.2.2:5170";
  static const Map<String, dynamic> _header = {
    "Accept": "application/json",
    "content-type":"application/json"
  };

  Dio dio = fetchDio();

  Future<void> login(LoginDto login) async {
    try{
      await dio.post(
        '$_url/Authentication/login',
        data: login.toJson(),
        options: Options(headers: _header)
      );
    }
    catch(e){
      rethrow;
    }
  }

  Future<void> register(RegisterDto register) async {
    try{
      await dio.post(
          '$_url/Authentication/register',
          data: register.toJson(),
          options: Options(headers: _header)
      );
    }
    catch(e){
      rethrow;
    }
  }

  Future<List<Category>> fetchAllCategories() async {
    try {
      var response = await dio.get(
        '$_url/api/Category/getFromUser',
        options: Options(headers: _header)
      );

      return (response.data as List)
          .map((e) => Category.fromJson(e))
          .toList();
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Category> sendNewCategory(Category category) async {
    try{
      var response = await dio.post(
          '$_url/api/Category/add',
          data: category,
          options: Options(headers: _header)
      );

      return Category.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Category> sendModifiedCategory(Category category) async {
    try{
      var response = await dio.put(
          '$_url/api/Category/edit',
          data: category,
          options: Options(headers: _header)
      );

      return Category.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<void> removeCategory(String id) async {
    try{
      await dio.delete(
          '$_url/api/Category/delete?id=$id',
          options: Options(headers: _header)
      );
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> fetchTasksFromCategoryId(String id) async {
    try {
      var response = await dio.get(
          '$_url/api/Task/getTasksFromCategory?id=$id',
          options: Options(headers: _header)
      );

      return (response.data as List)
          .map((e) => Task.fromJson(e))
          .toList();
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> fetchAllTasks() async {
    try {
      var response = await dio.get(
          '$_url/api/Task/getAll',
          options: Options(headers: _header)
      );

      return (response.data as List)
          .map((e) => Task.fromJson(e))
          .toList();
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Task> sendNewTask(Task task) async {
    try{
      var response = await dio.post(
          '$_url/api/Task/add',
          data: task,
          options: Options(headers: _header)
      );

      return Task.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Task> sendModifiedTask(Task task) async {
    try{
      var response = await dio.put(
          '$_url/api/Task/edit',
          data: task,
          options: Options(headers: _header)
      );

      return Task.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<void> removeTask(String id) async {
    try{
      await dio.delete(
          '$_url/api/Task/delete?id=$id',
          options: Options(headers: _header)
      );
    }
    catch(e) {
      rethrow;
    }
  }
}

