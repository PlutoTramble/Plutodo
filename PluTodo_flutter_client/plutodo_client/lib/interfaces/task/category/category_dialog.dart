import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:plutodo_client/models/category.dart';

class CategoryDialog extends StatefulWidget {
  const CategoryDialog({super.key, this.category, required this.color});

  final Category? category;
  final String color;

  @override
  State<CategoryDialog> createState() => _CategoryDialog();
}

class _CategoryDialog extends State<CategoryDialog> {
  late TextEditingController categoryNameController;

  String? validateCategoryName(String? value) {
    if(value == null || value.isEmpty){
      return "Task name needs to be filled";
    }
    if(value.length > 100){
      return "Task name needs to be under 100 characters";
    }
    return null;
  }
  
  Category getModifiedCategory() {
    Category category;
    if(widget.category == null) {
      category = Category.init(
          "",
          categoryNameController.value.text,
          widget.color
      );
    }
    else {
      category = widget.category!;
      category.name = categoryNameController.value.text;
    }
    return category;
  }

  @override
  void initState() {
    categoryNameController =
        TextEditingController(text: widget.category?.name ?? "");

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(
          widget.category == null
              ? 'Create a new category'
              : "Edit selected category"
      ),
      content: TextFormField(
        controller: categoryNameController,
        decoration: const InputDecoration(
            border: OutlineInputBorder(),
            labelText: "Category name"
        ),
        validator: (value) => validateCategoryName(value),
      ),
      actions: [
        Row(
          mainAxisAlignment: widget.category != null
              ? MainAxisAlignment.spaceBetween
              : MainAxisAlignment.end,
          children: [
            if(widget.category != null)
              ElevatedButton(
                  onPressed: () => Navigator.pop(context, 'Delete'),
                  child: const Icon(
                    Icons.delete,
                    color: Colors.red,
                  )
              ),

            Row(
              children: [
                TextButton(
                  onPressed: () => Navigator.pop(context, 'Cancel'),
                  child: const Text('Cancel'),
                ),
                TextButton(
                  onPressed: () {
                    Navigator.pop(context, jsonEncode(getModifiedCategory()));
                  },
                  child: const Text('Submit'),
                ),
              ],
            )
          ],
        )
      ],
    );
  }
}