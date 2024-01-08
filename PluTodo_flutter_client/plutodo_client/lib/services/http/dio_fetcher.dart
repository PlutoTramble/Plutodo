import 'package:dio/dio.dart';
import 'dio_getters/get_dio.dart'
  if (dart.library.io) 'dio_getters/dio_io.dart'
  if (dart.library.html) 'dio_getters/dio_web.dart';

Dio fetchDio() => getDio();

