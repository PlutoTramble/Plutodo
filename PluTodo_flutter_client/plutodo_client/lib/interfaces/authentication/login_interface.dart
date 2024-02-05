import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/models/authentication/login_dto.dart';
import 'package:plutodo_client/services/authentication_service.dart';

class LoginInterface extends StatefulWidget {
  LoginInterface({super.key, required this.title}) {}
  final String title;
  final AuthenticationService _authenticationService = getIt<AuthenticationService>();

  @override
  State<LoginInterface> createState() => _LoginInterface();
}

class _LoginInterface extends State<LoginInterface> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();


  Future<void> logIn() async {
    LoginDto userInformation = LoginDto(
        usernameController.value.text,
        passwordController.value.text);

    try{
      await widget._authenticationService.logInUser(userInformation);

      _popAndGo('/Plutodo');
    }
    on DioException catch(e){
      showMessage(e.toString());
    }
    catch(e){
      showMessage(e.toString());
    }
  }
  
  void showMessage(String message){
    SnackBar snackBar = SnackBar(
        content: Text(message)
    );

    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }
  
  void _popAndGo(String route) {
    Navigator.popAndPushNamed(context, route);
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Container(
          constraints: const BoxConstraints(minWidth: 300, maxWidth: 600, maxHeight: 500),
          padding: const EdgeInsets.only(top: 16, right: 25, left: 25, bottom: 25),

          alignment: Alignment.center,
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.inversePrimary,
            borderRadius: BorderRadius.circular(30)
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [

              Text(
                widget.title,
                textAlign: TextAlign.center,
                textScaler: const TextScaler.linear(2),
              ),

              TextField(
                controller: usernameController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Username"
                ),
              ),

              TextField(
                controller: passwordController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Password",
                ),
                obscureText: true,
              ),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ElevatedButton(
                    onPressed: () => logIn(),
                    child: const Text(
                      "Login",
                      textScaler: TextScaler.linear(1.3),
                    ),
                  ),

                  ElevatedButton(
                    onPressed: () => {
                      Navigator.pushNamed(context, '/register')
                    },
                    child: const Text(
                      "Register",
                      textScaler: TextScaler.linear(1.3),
                    ),
                  ),
                ],
              ),

            ],
          ),
        ),
      ),
    );
  }
}