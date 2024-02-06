import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/models/category.dart';
import 'package:plutodo_client/models/task.dart';

import 'http/http_service.dart';

class TaskService {
  final _httpService = getIt<HttpService>();

  Future<List<Category>> getAllCategories() async {
    try{
      return await _httpService.fetchAllCategories();
      // Server needs to sort by order
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Category> addNewCategory(Category category) async {
    try{
      return await _httpService.sendNewCategory(category);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Category> editCategory(Category category) async {
    try{
      return await _httpService.sendModifiedCategory(category);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<bool> deleteCategory(String id) async {
    try{
      await _httpService.removeCategory(id);

      return true;
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> getTodosFromCategory(String id) async {
    try{
      List<Task> tasks = await _httpService.fetchTasksFromCategoryId(id);
      tasks.removeWhere((element) => element.finished);
      tasks.sort((a, b) {
        return DateTime.parse(a.dateDue ?? DateTime.utc(3000).toString())
            .compareTo(DateTime.parse(b.dateDue ?? DateTime.utc(3000).toString()));
      });

      return tasks;
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> getAllTodos() async {
    try{
      List<Task> tasks = await _httpService.fetchAllTasks();
      tasks.removeWhere((element) => element.finished);
      tasks.sort((a, b) {
        return DateTime.parse(a.dateDue ?? DateTime.utc(3000).toString())
            .compareTo(DateTime.parse(b.dateDue ?? DateTime.utc(3000).toString()));
      });

      return tasks;
    }
    catch(e) {
      rethrow;
    }
  }

  Future<List<Task>> getAllFinishedTodos() async {
    try{
      List<Task> tasks = await _httpService.fetchAllTasks();
      tasks.removeWhere((element) => !element.finished);
      tasks.sort((a, b) {
        return DateTime.parse(a.dateDue ?? DateTime.utc(3000).toString())
            .compareTo(DateTime.parse(b.dateDue ?? DateTime.utc(3000).toString()));
      });

      return tasks;
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Task> addNewTask(Task task, String categoryId) async {
    try{
      return await _httpService.sendNewTask(task);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<Task> editTask(Task task) async {
    try{
      return await _httpService.sendModifiedTask(task);
    }
    catch(e) {
      rethrow;
    }
  }

  Future<bool> deleteTask(String id) async {
    try{
      await _httpService.removeTask(id);

      return true;
    }
    catch(e) {
      rethrow;
    }
  }

  bool isMobile(BuildContext context){
    MediaQueryData mediaQuery = MediaQuery.of(context);
    return mediaQuery.size.width < 1070;
  }
}