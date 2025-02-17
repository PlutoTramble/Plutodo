import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/models/authentication/login_dto.dart';
import 'package:plutodo_client/models/authentication/register_dto.dart';
import 'package:plutodo_client/services/general_service.dart';
import 'package:plutodo_client/services/http/error_message.dart';
import 'package:plutodo_client/services/http/http_service.dart';
import 'package:plutodo_client/services/user_singleton.dart';

class AuthenticationService extends GeneralService {
  final _httpService = getIt<HttpService>();

  Future<bool> signUp(RegisterDto register, BuildContext context) async {
    if(register.password != register.passwordConfirm && context.mounted){
      showSnackBarError("The passwords must be identical.", context);
      return false;
    }

    try {
      await _httpService.register(register);
      User().username = register.username;
      return true;
    }
    on DioException catch(e) {
      if (context.mounted) {
        showSnackBarError('HTTP Error ${e.response?.statusCode.toString()} : ${ErrorMessage.fromJson(e.response?.data).error}', context);
      }
    }
    catch(e){
      if (context.mounted) {
        showSnackBarError('Error : ${e.toString()}', context);
      }
    }

    return false;
  }

  Future<bool> logInUser(LoginDto login, BuildContext context) async {
    try {
      await _httpService.login(login);

      User().username = login.username;

      return true;
    }
    on DioException catch(e) {
      if (context.mounted) {
        showSnackBarError('HTTP Error ${e.response?.statusCode.toString()} : ${ErrorMessage.fromJson(e.response?.data).error}', context);
      }
    }
    catch(e){
      if (context.mounted) {
        showSnackBarError('Error : ${e.toString()}', context);
      }
    }
    return false;
  }
}