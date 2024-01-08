import 'package:cookie_jar/cookie_jar.dart';
import 'package:dio/dio.dart';
import 'package:dio_cookie_manager/dio_cookie_manager.dart';

Dio getDio() {
  Dio dio = Dio();
  dio.interceptors.add(CookieManager(CookieJar()));
  return dio;
}
