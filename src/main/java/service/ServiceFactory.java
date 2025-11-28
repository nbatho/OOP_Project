package main.java.service;

import main.java.service.impl.ProjectMemberServiceImpl;
import main.java.service.impl.ProjectServiceImpl;
import main.java.service.impl.UserServiceImpl;

public class ServiceFactory {
    public static void init() {
        ProjectServiceImpl projectService = ProjectServiceImpl.getInstance();
        ProjectMemberServiceImpl projectMemberService = ProjectMemberServiceImpl.getInstance();
        UserServiceImpl userService = UserServiceImpl.getInstance();

        userService.setUserService(userService);
        projectService.setProjectMemberService(projectMemberService);
        projectMemberService.setProjectService(projectService);
    }
}
