
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:plutodo_client/injection.dart';
import 'package:plutodo_client/interfaces/task/category/category_interface.dart';
import 'package:plutodo_client/interfaces/task/detail/task_detail_interface.dart';
import 'package:plutodo_client/interfaces/task/detail/detail_interface_state.dart';
import 'package:plutodo_client/interfaces/task/detail/task_state.dart';
import 'package:plutodo_client/interfaces/task/tasklist_interface.dart';
import 'package:plutodo_client/models/task.dart';
import 'package:plutodo_client/services/task_service.dart';

class MainTaskInterface extends StatefulWidget {
  MainTaskInterface({super.key, required this.title}) {}
  final TaskService _taskService = getIt<TaskService>();
  final String title;

  @override
  State<MainTaskInterface> createState() => _MainTaskInterface();
}

class _MainTaskInterface extends State<MainTaskInterface> {
  late CategoryInterface categoryInterface;
  late TaskListInterface taskListInterface;
  TaskDetailInterface? taskDetailInterface;

  late ValueListenable<TaskState>? taskState;

  bool showTaskDetail = false;

  void _handleModifiedTask(Task oldTask, bool isNewTask) async {
    Task task;

    if(isNewTask) {
      task = await widget._taskService
          .addNewTask(oldTask, taskListInterface.selectedCategoryId.value);

      task.isNew = true;
      taskListInterface.selectedTask.value = task;
    }
    else {
      task = await widget._taskService
          .editTask(oldTask);
    }

    _deselectTask();
  }

  void _deselectTask() {
    taskListInterface.selectedTask.value = null;
  }

  void selectTask() {
    if(taskListInterface.selectedTask.value != null){
      if(taskListInterface.selectedTask.value!.isNew){
        return;
      }

      bool isMobile = widget._taskService.isMobile(context);

      taskDetailInterface =
          TaskDetailInterface(
            selectedTask: taskListInterface.selectedTask,
            isMobile: isMobile,
          );

      taskState = taskDetailInterface!.taskDetailState;

      taskState!.addListener(() {
        switch(taskState!.value.detailInterfaceState) {
          case(DetailInterfaceState.closed) :
            _deselectTask();
            break;
          case(DetailInterfaceState.modified) :
            _handleModifiedTask(taskState!.value.task!, false);
            _deselectTask();
            break;
          case(DetailInterfaceState.newAddition) :
            _handleModifiedTask(taskState!.value.task!, true);
            _deselectTask();
            break;
          default:
        }
      });

      if(isMobile){
        _goToTaskDetail(taskDetailInterface!);
        return;
      }

      showTaskDetail = true;
    }
    else {
      taskState = null;
      taskDetailInterface = null;
      showTaskDetail = false;
    }

    setState(() {
      showTaskDetail;
      taskDetailInterface?.selectedTask.value;
    });
  }

  void _goToTaskDetail(TaskDetailInterface interface){
    if(taskListInterface.selectedTask.value == null) {
      throw Exception("Task needs to be selected");
    }

    Navigator.push(context,
        MaterialPageRoute(builder: (context) =>
          interface
        )
    ).whenComplete(() {
      // if(taskState?.value.detailInterfaceState != DetailInterfaceState.newAddition) {
      //   _deselectTask();
      // }
    });
  }

  @override
  void initState() {
    super.initState();
    categoryInterface = CategoryInterface();

    taskListInterface =
        TaskListInterface(selectedCategoryId: categoryInterface.selectedCategoryId);

    taskListInterface.selectedTask.addListener(() {
      selectTask();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Row(
        children: [

          Expanded(
            flex: 1,
            child: categoryInterface
          ),

          Expanded(
            flex: !showTaskDetail ? 3 : 2,
            child: Container(
              height: double.infinity,
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.primary,
                borderRadius: const BorderRadius.only(
                    topLeft: Radius.zero,
                    topRight: Radius.circular(20),
                    bottomLeft: Radius.zero,
                    bottomRight: Radius.circular(20)
                ),
              ),
              child: taskListInterface
            ),
          ),

          if(showTaskDetail)
            Expanded(
              flex: 1,
              child: taskDetailInterface!,
            ),

        ],
      ),
    );
  }
}