import 'package:get_it/get_it.dart';
import 'package:plutodo_client/services/authentication_service.dart';
import 'package:plutodo_client/services/http/http_service.dart';
import 'package:plutodo_client/services/task_service.dart';

final getIt = GetIt.instance;


void setupDependencies() {
  getIt.registerSingleton<HttpService>(HttpService());
  getIt.registerSingleton<AuthenticationService>(AuthenticationService());
  getIt.registerSingleton<TaskService>(TaskService());
}