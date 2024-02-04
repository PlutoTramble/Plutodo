

import 'package:flutter/material.dart';
import 'package:plutodo_client/interfaces/task/main_task_interface.dart';

class MainInterface extends StatefulWidget {
  MainInterface({super.key});

  @override
  State<MainInterface> createState() => _MainInterface();

}

class _MainInterface extends State<MainInterface> {
  int selectedInterfaceIndex = 0;
  Widget? currentInterface;
  String currentTitle = "";

  bool isMobile(){
    MediaQueryData mediaQuery = MediaQuery.of(context);
    return mediaQuery.size.width < 1070;
  }

  void _changeInterface(int index) {
    if(index == selectedInterfaceIndex){
      return;
    }

    selectedInterfaceIndex = index;

    switch(index){
      case 0:
        // TODO : Implement home
        break;
      case 1:
        currentInterface = MainTaskInterface(title: "Tasks");
        break;
      case 2:
        // TODO : Implement calendar
        break;
      case 3:
        // TODO : Implement settings
        break;
    }

    setState(() {
      currentInterface;
      selectedInterfaceIndex;
    });
  }

  @override
  void initState() {
    currentInterface = MainTaskInterface(title: "Tasks");

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: currentInterface,
      appBar: AppBar(
        title: Text(currentTitle),
        backgroundColor: Theme.of(context).appBarTheme.surfaceTintColor,
      ),
      drawer: NavigationDrawer(
        onDestinationSelected: (index) => _changeInterface(index),
        selectedIndex: selectedInterfaceIndex,
        children:[
          Padding(
            padding: const EdgeInsets.fromLTRB(28, 16, 16, 10),
            child: Text(
              'Header',
              style: Theme.of(context).textTheme.titleSmall,
            ),
          ),
          const NavigationDrawerDestination(
            icon: Icon(Icons.home),
            label: Text("Home")
          ),
          const NavigationDrawerDestination(
              icon: Icon(Icons.task_alt),
              label: Text("Tasks")
          ),
          const NavigationDrawerDestination(
              icon: Icon(Icons.calendar_month),
              label: Text("Calendar")
          ),
          const NavigationDrawerDestination(
              icon: Icon(Icons.settings),
              label: Text("Settings")
          ),
          const Padding(
            padding: EdgeInsets.fromLTRB(28, 16, 28, 10),
            child: Divider(),
          ),
          const Padding(
            padding: EdgeInsets.fromLTRB(28, 16, 28, 10),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text(
                    "Plutodo\nCopyright (C) 2024  PlutoTramble"
                )
              ],
            ),
          ),
        ],
      ),
      bottomNavigationBar: isMobile() ? BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.task),
            label: 'Tasks',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.calendar_month),
            label: 'Calendar',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.settings),
            label: 'Settings',
          ),
        ],
        currentIndex: selectedInterfaceIndex,
        selectedItemColor: Theme.of(context).colorScheme.inversePrimary,
        unselectedIconTheme: IconThemeData(
          color: Theme.of(context).iconTheme.color
        ),
        onTap: (index) => _changeInterface(index),
      ) : null,
    );
  }
}