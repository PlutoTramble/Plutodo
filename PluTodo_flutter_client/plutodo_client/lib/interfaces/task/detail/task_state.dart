import 'package:plutodo_client/interfaces/task/detail/detail_interface_state.dart';
import 'package:plutodo_client/models/task.dart';

class TaskState {
  TaskState(this.detailInterfaceState, this.task);
  
  final DetailInterfaceState detailInterfaceState;
  final Task? task;
}