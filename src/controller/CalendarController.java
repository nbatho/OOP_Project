package controller;

import model.Task;
import view.CalendarView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class CalendarController {

    private final CalendarView view;
    private List<Task> tasks = new ArrayList<>();

    public CalendarController(CalendarView view) {
        this.view = view;
        view.setController(this);
    }


    public void setTasks(List<Task> taskList) {
        if (taskList != null) {
            this.tasks = new ArrayList<>(taskList);
        } else {
            this.tasks.clear();
        }
        updateView(LocalDate.now());
    }


    public void onDateSelected(LocalDate date) {
        updateView(date);
    }


    private void updateView(LocalDate date) {
        List<Task> todayTasks = getTasksByDate(date);
        List<Task> upcomingTasks = getUpcomingTasks();
        view.updateTaskList(todayTasks, date);
        view.updateUpcomingList(upcomingTasks);
    }


    private List<Task> getTasksByDate(LocalDate date) {
//        return tasks.stream()
//                .filter(t -> t.getDeadline() != null && t.getDeadline().isEqual(date))
//                .collect(Collectors.toList());
        return tasks;
    }


    private List<Task> getUpcomingTasks() {
//        LocalDate today = LocalDate.now();
//        return tasks.stream()
//                .filter(t -> t.getDeadline() != null && !t.getDeadline().isBefore(today))
//                .sorted(Comparator.comparing(Task::getDeadline))
//                .limit(5)
//                .collect(Collectors.toList());
        return tasks;
    }
}
