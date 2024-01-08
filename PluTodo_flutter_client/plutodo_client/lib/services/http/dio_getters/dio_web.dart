import 'package:dio/browser.dart';
import 'package:dio/dio.dart';

Dio getDio() => Dio()
  ..httpClientAdapter = BrowserHttpClientAdapter(withCredentials: true);
