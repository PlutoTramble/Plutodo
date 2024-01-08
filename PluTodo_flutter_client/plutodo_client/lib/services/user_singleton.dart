class User {
  static final User _user = User._internal();
  factory User() {
    return _user;
  }
  User._internal();

  String? username;
}