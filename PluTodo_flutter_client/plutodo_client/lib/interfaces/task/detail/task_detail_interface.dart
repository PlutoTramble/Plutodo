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

  String? _validateTaskName(String? value) {
    if(value == null || value.isEmpty){
      return "Task name needs to be filled";
    }
    if(value.length > 250){
      return "Task name needs to be under 250 characters";
    }
    return null;
  }

  String? _validateDescription(String? value) {
    if(value != null && value.length > 8000){
      return "Task name needs to be under 8000 characters";
    }
    return null;
  }

  void _changeTaskState(DetailInterfaceState state, bool includeTask) {
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

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
        context: context,
        initialDate: _task.dateDue != null
            ? DateFormat('yyyy-MM-dd HH:mm:ss').parse(_task.dateDue!)
            : DateTime.now(),
        firstDate: DateTime(2010, 8),
        lastDate: DateTime(2101));
    if (picked != null && picked != DateFormat('yyyy-MM-dd HH:mm:ss').parse(_task.dateDue!)) {
      setState(() {
        _task.dateDue = DateFormat('yyyy-MM-dd HH:mm:ss').format(picked);
      });
    }
  }

  @override
  void initState() {
    getTodo();

    taskDescriptionController =
        TextEditingController(text: _task.description ?? "");
    taskNameController =
        TextEditingController(text: _task.name);

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      //backgroundColor: Colors.transparent,
      body: widget.selectedTask.value != null ? Container(
        padding: EdgeInsets.all(20),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [

            TextFormField(
              controller: taskNameController,
              decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Task name"
              ),
              validator: (value) => _validateTaskName(value),
            ),

            TextFormField(
                controller: taskDescriptionController,
                decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "Description"
                ),
                minLines: 13,
                maxLines: 13,
                validator: (value) => _validateDescription(value)
            ),

            Text(
                "Date due : ${_task.dateDue ?? "None"}",
                textScaler: const TextScaler.linear(1.3),
            ),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                ElevatedButton(
                  onPressed: () => _selectDate(context),
                  child: const Text(
                    "Select\nDate",
                    textScaler: TextScaler.linear(1.3),
                    textAlign: TextAlign.center,
                  ),
                ),

                ElevatedButton(
                  onPressed: () {
                    setState(() => _task.dateDue = null);
                  },
                  child: const Text(
                    "Remove\nDate",
                    textScaler: TextScaler.linear(1.3),
                    textAlign: TextAlign.center,
                  ),
                ),
              ],
            ),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: () {
                    _changeTaskState(DetailInterfaceState.closed, false);
                  },
                  child: const Text(
                    "Cancel",
                    textScaler: TextScaler.linear(1.3),
                  ),
                ),

                ElevatedButton(
                  onPressed: () {
                    _isNewTask
                        ? _changeTaskState(DetailInterfaceState.newAddition, true)
                        : _changeTaskState(DetailInterfaceState.modified, true);
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
      ) : const SizedBox()
    );
  }
}