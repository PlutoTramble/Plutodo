import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/task/detail/task_detail_interface.dart';
import 'package:plutodo_client/models/task.dart';
import 'package:plutodo_client/services/task_service.dart';

class TaskListInterface extends StatefulWidget {
  TaskListInterface({super.key, required this.selectedCategoryId}) {}
  final TaskService _taskService = getIt<TaskService>();

  final ValueListenable<int> selectedCategoryId;
  final ValueNotifier<Task?> selectedTask = ValueNotifier<Task?>(null);

  @override
  State<TaskListInterface> createState() => _TaskListInterface();
}

class _TaskListInterface extends State<TaskListInterface> {
  List<Task> _tasks = [];
  bool isRealCategory = true;

  Future<void> getFromCategory() async {
    isRealCategory = true;
    _tasks = await widget._taskService
        .getTodosFromCategory(widget.selectedCategoryId.value);
  }

  Future<void> getFromBasic(int id) async {
    isRealCategory = false;
    switch(id){
      case(-3):
        _tasks = await widget._taskService.getAllTodos();
        break;
      case(-1):
        _tasks = await widget._taskService.getAllFinishedTodos();
        break;
    }
  }

  void setupNewTask() {
    widget.selectedTask.value =
        Task.init(
            0,
            "",
            null,
            false,
            DateFormat("yyyy-MM-dd HH:mm:ss").format(DateTime.now()),
            _tasks.length + 1,
            widget.selectedCategoryId.value
        );

    if(widget._taskService.isMobile(context)){
      _goToTaskDetail();
      return;
    }

    _tasks.forEach((element) {
      element.selected = false;
    });
  }

  Future<void> getTodos() async {
    int categoryId = widget.selectedCategoryId.value;

    bool isFromCategory = categoryId >= 0;
    bool isFromBasic = categoryId <= -1 && categoryId >=-3;

    if(isFromCategory) {
      await getFromCategory();
    }
    else if(isFromBasic) {
      await getFromBasic(categoryId);
    }
    else{
      _tasks = [];
    }

    widget.selectedTask.value = null;

    setState(() {
      _tasks;
      isRealCategory;
    });
  }

  void selectTask(int index) {
    int id = _tasks[index].id;

    if(widget.selectedTask.value?.id == _tasks[index].id ||
        widget.selectedTask.value != null){
      widget.selectedTask.value = null;

      _tasks.forEach((element) {
        element.selected = false;
      });
    }
    else {
      widget.selectedTask.value = _tasks[index];

      if(widget._taskService.isMobile(context)){
        _goToTaskDetail();
        return;
      }

      _tasks.forEach((element) {
        element.id == id && !element.selected
            ? element.selected = true
            : element.selected = false;
      });
    }

    setState(() {
      _tasks;
    });
  }

  void _goToTaskDetail(){
    if(widget.selectedTask.value == null) {
      throw Exception("Task needs to be selected");
    }

    Navigator.push(context,
        MaterialPageRoute(builder: (context) =>
            TaskDetailInterface(
              selectedTask: widget.selectedTask,
              isMobile: true,
            )
        )
    ).whenComplete(() {
      // Unselect task once changing screen
      widget.selectedTask.value = null;
    });
  }

  void _updateTaskList() {
    bool selectedTaskChanged =
      !_tasks.any((element) => element == widget.selectedTask.value);
    bool selectedTaskIdIsIncludeInTaskList = false;
    Task? task = widget.selectedTask.value;

    if(selectedTaskChanged && task != null && task.name != "") {
      _tasks.forEach((element) {
        if(element.id == task.id){
          element = task;
          selectedTaskIdIsIncludeInTaskList = true;
        }
      });

      if(!selectedTaskIdIsIncludeInTaskList){
        _tasks.add(task);
      }
    }
    else if(selectedTaskChanged && task == null) {
      _tasks.forEach((element) {
        element.selected = false;
      });
    }

    setState(() => _tasks);
  }

  void _setTaskAsFinished(int index, bool value) async {
    try{
      _tasks[index].finished = value;
      await widget._taskService.editTask(_tasks[index]);

      setState(() => _tasks);
    }
    catch(e) {
      rethrow;
    }
  }

  void _deleteSelectedTask() async {
    try{
      await widget._taskService.deleteTask(widget.selectedTask.value!.id);

      _tasks.removeWhere((element) => element.id == widget.selectedTask.value!.id);
      widget.selectedTask.value = null;

      setState(() => _tasks);
    }
    catch(e) {
      rethrow;
    }
  }

  @override
  void initState() {
    super.initState();
    widget.selectedCategoryId.addListener(() {
      getTodos();
    });

    widget.selectedTask.addListener(() {
      _updateTaskList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Column(
        children: [
          Container(
            padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: () => isRealCategory ? setupNewTask() : null,
                  child: const Text(
                    "Add\nnew task",
                    textAlign: TextAlign.center,
                    textScaler: TextScaler.linear(1.3),
                  ),
                ),

                OutlinedButton(
                  onPressed: () =>
                    widget.selectedTask.value != null
                      ? _deleteSelectedTask()
                      : null,
                  child: Text(
                    "Delete\nselected task",
                    textAlign: TextAlign.center,
                    textScaler: const TextScaler.linear(1.3),
                    style: TextStyle(
                      color: Theme.of(context).cardColor
                    ),
                  ),
                ),
              ],
            )
          ),

          Expanded(
            child: ListView.builder(
              itemCount: _tasks.length,
              shrinkWrap: true,
              itemBuilder: (context, index) => Container(
                width: double.infinity,
                margin: const EdgeInsets.all(10),
                padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
                decoration: BoxDecoration(
                  color: !_tasks[index].selected ?
                  Theme.of(context).colorScheme.inversePrimary:
                  Theme.of(context).colorScheme.surface,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: InkWell(
                  onTap: () => selectTask(index),

                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      Checkbox(
                          value: _tasks[index].finished,
                          onChanged: (bool) {
                            _setTaskAsFinished(index, bool!);
                          }
                      ),

                      Expanded(
                        child: Text(
                          "${_tasks[index].name}"
                              "${_tasks[index].dateDue != null
                                ? '\nDate due : ${_tasks[index].dateDue!}'
                                : ""}",
                          textScaler: const TextScaler.linear(1.2),
                          textAlign: TextAlign.start,
                          style: const TextStyle(
                              fontWeight: FontWeight.bold
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}