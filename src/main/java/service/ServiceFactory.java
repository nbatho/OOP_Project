package main.java.service;

import main.java.service.impl.ProjectMemberServiceImpl;
import main.java.service.impl.ProjectServiceImpl;

public class ServiceFactory {
    public static void init() {
        ProjectServiceImpl projectService = ProjectServiceImpl.getInstance();
        ProjectMemberServiceImpl projectMemberService = ProjectMemberServiceImpl.getInstance();

        projectService.setProjectMemberService(projectMemberService);
        projectMemberService.setProjectService(projectService);
    }
}
