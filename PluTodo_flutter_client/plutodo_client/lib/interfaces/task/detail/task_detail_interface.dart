import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:plutodo_client/interfaces/task/detail/detail_interface_state.dart';
import 'package:plutodo_client/interfaces/task/detail/task_state.dart';
import 'package:plutodo_client/models/task.dart';
import 'package:intl/intl.dart';
class TaskDetailInterface extends StatefulWidget {
  TaskDetailInterface({super.key, required this.selectedTask, required this.isMobile}) {}

  final ValueListenable<Task?> selectedTask;
  final ValueNotifier<TaskState> taskDetailState =
    ValueNotifier<TaskState>(TaskState(DetailInterfaceState.opened, null));

  final bool isMobile;

  @override
  State<TaskDetailInterface> createState() => _TaskDetailInterface();
}

class _TaskDetailInterface extends State<TaskDetailInterface> {
  late TextEditingController taskNameController;
  late TextEditingController taskDescriptionController;

  bool _isNewTask = false;
  late Task _task;

  void getTodo() {
    if(widget.selectedTask.value != null) {
      _task = widget.selectedTask.value!;
    }

    if(_task.name == "") {
      _isNewTask = true;
    }

    if(mounted) {
      setState(() {
        _task;
      });
    }
  }

  String? validateTaskName(String? value) {
    if(value == null || value.isEmpty){
      return "Task name needs to be filled";
    }
    if(value.length > 250){
      return "Task name needs to be under 250 characters";
    }
    return null;
  }

  String? validateDescription(String? value) {
    if(value != null && value.length > 8000){
      return "Task name needs to be under 8000 characters";
    }
    return null;
  }

  void changeTaskState(DetailInterfaceState state, bool includeTask) {
    if(includeTask){
      _task.name = taskNameController.value.text;
      _task.description = taskDescriptionController.value.text;
    }
    widget.taskDetailState.value =
        TaskState(state, includeTask ? _task : null);

    if(widget.isMobile){
      Navigator.pop(context);
    }
  }

  @override
  void initState() {
    getTodo();
    widget.selectedTask.addListener(() {
      getTodo();
    });

    taskDescriptionController =
        TextEditingController(text: _task.description ?? "");
    taskNameController =
        TextEditingController(text: _task.name);

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: widget.selectedTask.value != null ? Container(
        width: double.infinity,
        height: double.infinity,
        padding: const EdgeInsets.all(20),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            TextFormField(
              controller: taskNameController,
              decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Task name"
              ),
              validator: (value) => validateTaskName(value),
            ),

            TextFormField(
              controller: taskDescriptionController,
              decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Description"
              ),
              minLines: 3,
              maxLines: null,
              validator: (value) => validateDescription(value)
            ),

            CalendarDatePicker(
                initialDate: _task.dateDue != null
                    ? DateFormat('yyyy-MM-dd HH:mm:ss').parse(_task.dateDue!)
                    : DateTime.now(),
                firstDate: DateTime(1999),
                lastDate: DateTime(2900),
                onDateChanged: (dateTime) => _task.dateDue =
                    DateFormat('yyyy-MM-dd HH:mm:ss').format(dateTime)
            ),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: () {
                    changeTaskState(DetailInterfaceState.closed, false);
                  },
                  child: const Text(
                    "Cancel",
                    textScaler: TextScaler.linear(1.3),
                  ),
                ),

                ElevatedButton(
                  onPressed: () {
                    _isNewTask
                      ? changeTaskState(DetailInterfaceState.newAddition, true)
                      : changeTaskState(DetailInterfaceState.modified, true);
                  },
                  child: const Text(
                    "Submit",
                    textScaler: TextScaler.linear(1.3),
                  ),
                ),
              ],
            ),
          ],
        ),
      ) : SizedBox()
    );
  }
}