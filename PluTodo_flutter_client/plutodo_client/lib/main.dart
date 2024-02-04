import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/authentication/login_interface.dart';
import 'package:plutodo_client/interfaces/authentication/register_interface.dart';
import 'package:plutodo_client/interfaces/main_interface.dart';

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
        '/Plutodo': (context) => MainInterface(),
      },
    );
  }
}
