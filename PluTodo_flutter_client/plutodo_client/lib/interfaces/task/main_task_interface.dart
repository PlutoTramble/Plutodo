
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
  late TaskListInterface taskInterface;
  late TaskDetailInterface? taskDetailInterface;

  late ValueListenable<TaskState>? taskState;

  bool showTaskDetail = false;

  void _handleModifiedTask(Task oldTask, bool isNewTask) async {
    Task task;

    if(isNewTask) {
      task = await widget._taskService
          .addNewTask(oldTask, taskInterface.selectedCategoryId.value);

      taskInterface.selectedTask.value = task;
    }
    else {
      task = await widget._taskService
          .editTask(oldTask);
    }

    _deselectTask();
  }

  void _deselectTask() {
    taskInterface.selectedTask.value = null;
  }

  void selectTask() {
    if(taskInterface.selectedTask.value != null){
      taskDetailInterface =
          TaskDetailInterface(
            selectedTask: taskInterface.selectedTask,
            isMobile: false,
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

  @override
  void initState() {
    super.initState();
    categoryInterface = CategoryInterface();

    taskInterface =
        TaskListInterface(selectedCategoryId: categoryInterface.selectedCategoryId);

    taskInterface.selectedTask.addListener(() {
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
              child: taskInterface
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