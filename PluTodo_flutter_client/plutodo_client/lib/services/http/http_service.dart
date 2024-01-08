import 'package:dio/dio.dart';
import 'package:plutodo_client/models/authentication/login_dto.dart';
import 'package:plutodo_client/models/authentication/register_dto.dart';
import 'package:plutodo_client/models/category.dart';
import 'package:plutodo_client/models/task.dart';
import 'package:plutodo_client/services/http/dio_fetcher.dart';

class HttpService {
  static const String _url = "http://localhost:5170";
  //static const String _url = "http://10.0.2.2:5170";
  static const Map<String, dynamic> _header = {
    "Accept": "application/json",
    "content-type":"application/json"
  };

  Dio dio = fetchDio();

  Future<void> login(LoginDto login) async {
    try{
      await dio.post(
        '$_url/api/users/login',
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
          '$_url/api/users/register',
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
        '$_url/api/Categories/GetAll',
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
          '$_url/api/Categories/New',
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
          '$_url/api/Categories/Edit/${category.id}',
          data: category,
          options: Options(headers: _header)
      );

      return Category.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<void> removeCategory(int id) async {
    try{
      await dio.delete(
          '$_url/api/Categories/Delete/$id',
          options: Options(headers: _header)
      );
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> fetchTasksFromCategoryId(int id) async {
    try {
      var response = await dio.get(
          '$_url/api/Todos/GetFromCategory/$id',
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
          '$_url/api/Todos/GetAll',
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

  Future<Task> sendNewTask(Task task, int categoryId) async {
    try{
      var response = await dio.post(
          '$_url/api/Todos/New/$categoryId',
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
          '$_url/api/Todos/Edit/${task.id}',
          data: task,
          options: Options(headers: _header)
      );

      return Task.fromJson(response.data);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<void> removeTask(int id) async {
    try{
      await dio.delete(
          '$_url/api/Todos/Delete/$id',
          options: Options(headers: _header)
      );
    }
    catch(e) {
      rethrow;
    }
  }
}

