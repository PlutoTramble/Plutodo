import 'package:dio/dio.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/models/authentication/login_dto.dart';
import 'package:plutodo_client/models/authentication/register_dto.dart';
import 'package:plutodo_client/services/http/error_message.dart';
import 'package:plutodo_client/services/http/http_service.dart';
import 'package:plutodo_client/services/user_singleton.dart';

class AuthenticationService {
  final _httpService = getIt<HttpService>();

  Future<void> signUp(RegisterDto register) async {
    if(register.password != register.passwordConfirm){
      throw "The password is the same";
    }

    try {
      await _httpService.register(register);

      User().username = register.username;
    }
    on DioException catch(e) {
      throw errorHandling(
          ErrorMessage.fromJson(e.response?.data).message!
      );
    }
    catch(e){
      rethrow;
    }
  }

  Future<void> logInUser(LoginDto login) async {
    try {
      await _httpService.login(login);

      User().username = login.username;
    }
    on DioException catch(e) {
      throw errorHandling(
          ErrorMessage.fromJson(e.response?.data).message!
      );
    }
    catch(e){
      rethrow;
    }
  }

  String errorHandling(String message) {
    switch(message){
      case "IncorrectUsernameOrPassword":
        return "Incorrect Username or password";
      default:
        return "Unknown error occured.";
    }
  }
}