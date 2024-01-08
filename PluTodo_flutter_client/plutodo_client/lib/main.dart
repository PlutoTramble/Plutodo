import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/authentication/login_interface.dart';
import 'package:plutodo_client/interfaces/authentication/register_interface.dart';
import 'package:plutodo_client/interfaces/task/main_task_interface.dart';
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
      darkTheme: ThemeData.dark(
        //colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      initialRoute: '/login',
      routes: {
        '/login': (context) => LoginInterface(title: 'Login'),
        '/register': (context) => RegisterInterface(title: 'Register'),
        '/tasks': (context) => MainTaskInterface(title: 'Tasks'),
      },
    );
  }
}
