
import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/task/category/category_dialog.dart';
import 'package:plutodo_client/interfaces/task/tasklist_interface.dart';
import 'package:plutodo_client/models/category.dart';
import 'package:plutodo_client/services/task_service.dart';


class CategoryInterface extends StatefulWidget {
  CategoryInterface({super.key}) {}
  final TaskService _taskService = getIt<TaskService>();

  final ValueNotifier<int> selectedCategoryId = ValueNotifier<int>(-1);

  @override
  State<CategoryInterface> createState() => _CategoyInterface();
}

class _CategoyInterface extends State<CategoryInterface> {
  late List<Category> _categories = [];
  Category? selectedCategory;

  void _selectCategory(int index) {
    int id = _categories[index].id!;
    selectedCategory = _categories[index];

    _categories.forEach((element) {
      element.id == id ? element.selected = true: element.selected = false;
    });

    setState(() {
      _categories;
      selectedCategory;
    });
    widget.selectedCategoryId.value = id;
  }

  void _handleModifiedCategory(Map<String, dynamic> categoryStringified,
      bool newCategory) async {
    Category category
      = Category.fromJson(categoryStringified);

    if(newCategory){
      category = await widget._taskService.addNewCategory(category);

      _categories.add(category);
    }
    else {
      category = await widget._taskService.editCategory(category);

      _categories.forEach((element) {
        if(element.id == category.id){
          element = category;
        }
      });

      setState(() {
        selectedCategory = category;
      });
    }

    setState(() {
      _categories;
    });
  }

  void _deleteSelectedCategory() async {
    try{
      await widget._taskService.deleteCategory(selectedCategory!.id);

      _categories.removeWhere((element) => element.id == selectedCategory!.id);

      selectedCategory = null;
      widget.selectedCategoryId.value = -1;

      setState(() {
        _categories;
        selectedCategory;
      });
    }
    catch(e) {
      rethrow;
    }
  }
  
  void _addBasicCategoriesToList() {
    _categories = [
      Category.init(-3, "All tasks", -3),
      //Category.init(-2, "All tasks by date", -2),
      Category.init(-1, "Finished tasks", -1)
    ];
  }

  Future<void> _getAllCategories() async {
    _addBasicCategoriesToList();
    _categories.addAll(await widget._taskService.getAllCategories());

    setState(() {
      _categories;
    });
  }
  
  void _editCategory(Category? category) {
    showDialog<String>(
        context: context,
        builder: (BuildContext context) =>
            CategoryDialog(
              category: category ?? selectedCategory,
              ordering: selectedCategory!.ordering,
            )
    ).then((value) {
      if(value != "Cancel" && value != "Delete") {
        _handleModifiedCategory(json.decode(value!), true);
      }
      else if(value == "Delete") {
        _deleteSelectedCategory();
      }
    });
  }

  @override
  void initState() {
    _getAllCategories();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Container(
        decoration: BoxDecoration(
          color: Theme.of(context).colorScheme.surface,
          borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.zero,
              bottomLeft: Radius.circular(20),
              bottomRight: Radius.zero
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Container(
                padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 5),
                height: 70,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    TextButton(
                      onPressed: () => showDialog<String>(
                          context: context,
                          builder: (BuildContext context) =>
                              CategoryDialog(ordering: _categories.length + 1,)
                      ).then((value) {
                        if(value != "Cancel") {
                          _handleModifiedCategory(json.decode(value!), true);
                        }
                      }),
                      child: const Text(
                        "Add new\ncategory",
                        textAlign: TextAlign.center,
                        textScaler: TextScaler.linear(1),
                      ),
                    ),


                    if(!widget._taskService.isMobile(context))
                      OutlinedButton(
                        onPressed: () => selectedCategory != null
                            ? _editCategory(null) : null,
                        child: const Text(
                          "Edit selected\ncategory",
                          textAlign: TextAlign.center,
                          textScaler: TextScaler.linear(1),
                        ),
                      ),


                  ],
                )
            ),

            Expanded(
              child: ListView.builder(
                itemCount: _categories.length,
                itemBuilder: (context, index) => Container(
                  width: double.infinity,
                  margin: const EdgeInsets.all(5),
                  padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 5),
                  decoration: BoxDecoration(
                    color: !_categories[index].selected
                        ? Theme.of(context).colorScheme.inversePrimary
                        : Theme.of(context).colorScheme.primary,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  constraints: const BoxConstraints(
                    minHeight: 60,
                  ),

                  child: InkWell(
                    onTap: () => _selectCategory(index),
                    onLongPress: () => _editCategory(_categories[index]),
                    child: Row(
                      children: [

                        Expanded(
                          child: Text(
                            _categories[index].name,
                            textScaler: const TextScaler.linear(1),
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              color: !_categories[index].selected
                                  ? Theme.of(context).colorScheme.surface
                                  : Theme.of(context).colorScheme.inversePrimary,
                            ),
                          ),
                        ),

                      ],
                    ),
                  ),
                ),
              ),
            ),

          ],
        ),
      ),
    );
  }

}