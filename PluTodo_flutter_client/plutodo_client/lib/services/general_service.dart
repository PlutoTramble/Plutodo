import 'package:flutter/material.dart';

class GeneralService {

  void showSnackBarError(String message, BuildContext context) {
    SnackBar snackBar = SnackBar(
        content: Text(message)
    );

    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
}