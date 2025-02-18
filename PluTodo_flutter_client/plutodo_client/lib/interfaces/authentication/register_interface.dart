import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/models/authentication/register_dto.dart';
import 'package:plutodo_client/services/authentication_service.dart';

class RegisterInterface extends StatefulWidget {
  RegisterInterface({super.key, required this.title}) {}
  final String title;
  final AuthenticationService _authenticationService = getIt<AuthenticationService>();

  @override
  State<RegisterInterface> createState() => _RegisterInterface();
}

class _RegisterInterface extends State<RegisterInterface> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController passwordConfirmController = TextEditingController();


  Future<void> signUp() async {
    RegisterDto userInformation = RegisterDto(
        usernameController.value.text,
        emailController.value.text,
        passwordController.value.text,
        passwordConfirmController.value.text);

    if(await widget._authenticationService.signUp(userInformation, context)) {
      popAndGo('/Plutodo');
    }
  }

  void popAndGo(String route) {
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
          padding: const EdgeInsets.all(16),

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
                controller: emailController,
                decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "Email"
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

              TextField(
                controller: passwordConfirmController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Confirm password",
                ),
                obscureText: true,
              ),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ElevatedButton(
                    onPressed: () => signUp(),
                    child: const Text(
                      "Register",
                      textScaler: TextScaler.linear(1.3),
                    ),
                  ),

                  ElevatedButton(
                    onPressed: () => {
                      Navigator.popAndPushNamed(context, '/login')
                    },
                    child: const Text(
                      "Go back",
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