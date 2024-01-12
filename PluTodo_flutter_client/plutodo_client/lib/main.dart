import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/authentication/login_interface.dart';
import 'package:plutodo_client/interfaces/authentication/register_interface.dart';
import 'package:plutodo_client/interfaces/task/category/category_interface.dart';
import 'package:plutodo_client/interfaces/task/main_task_interface.dart';
import 'package:plutodo_client/interfaces/task/tasklist_interface.dart';
import 'package:plutodo_client/services/authentication_service.dart';

void main() {
  setupDependencies();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blueGrey),
      ),
      darkTheme: ThemeData.dark(
        useMaterial3: true,
      ),
      themeMode: ThemeMode.dark,
      initialRoute: '/login',
      routes: {
        '/login': (context) => LoginInterface(title: 'Login'),
        '/register': (context) => RegisterInterface(title: 'Register'),
        '/tasks': (context) => MainTaskInterface(title: 'Tasks'),
      },
    );
  }
}
