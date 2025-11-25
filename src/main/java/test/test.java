package main.java.test;

import main.java.model.TaskAssignees;
import main.java.model.User;
import main.java.service.impl.TaskAssigneesServiceImpl;
import main.java.service.impl.UserServiceImpl;

public class test {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        User x = userService.getUserById("a53508eb-813c-4f67-bde7-de0eaef69bcb");
        System.out.println(x);
//        TaskAssigneesServiceImpl taskService = new TaskAssigneesServiceImpl();
    }
}
