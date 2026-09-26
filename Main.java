//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
           //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void backwardPass(List<Task> tasks, int projectDuration) {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            Task t = tasks.get(i);

            int minLateStart = projectDuration;
            boolean hasSuccessor = false;

            for (Task other : tasks) {
                if (other.dependencies.contains(t)) {
                    hasSuccessor = true;
                    if (other.lateStart < minLateStart) {
                        minLateStart = other.lateStart;
                    }
                }
            }
            if (!hasSuccessor) {
                t.lateFinish = projectDuration;
            } else {
                t.lateFinish = minLateStart;
            }
            t.lateStart = t.lateFinish - t.duration;
        }
    }

    public static void forwardPass(List<Task> tasks) {
        for (Task t : tasks) {
            if (t.dependencies.isEmpty()) {
                t.earlyStart = 0;
            } else {
                int maxFinish = 0;
                for (Task dep : t.dependencies) {
                    if (dep.earlyFinish > maxFinish) {
                        maxFinish = dep.earlyFinish;
                    }
                }
                t.earlyStart = maxFinish;
            }
            t.earlyFinish = t.earlyStart + t.duration;
        }
    }

    public static void main(String[] args) {
        Task design = new Task("Design", 3);
        Task procurement = new Task("Procurement", 5);
        Task build = new Task("Build", 7);
        Task test = new Task("Test", 4);
        Task deploy = new Task("Deploy", 2);

        procurement.addDependency(design);
        build.addDependency(procurement);
        test.addDependency(build);
        deploy.addDependency(test);

        List<Task> allTasks = new ArrayList<>();
        allTasks.add(design);
        allTasks.add(procurement);
        allTasks.add(build);
        allTasks.add(test);
        allTasks.add(deploy);
        forwardPass(allTasks);

        for (Task t : allTasks) {
            System.out.println(t.name + ": ES=" + t.earlyStart + " EF=" + t.earlyFinish);
        }

        int projectDuration = 0;
        for (Task t : allTasks) {
            if (t.earlyFinish > projectDuration) {
                projectDuration = t.earlyFinish;
            }
        }
        backwardPass(allTasks, projectDuration);
        for (Task t : allTasks) {
            t.slack = t.lateStart - t.earlyStart;
            System.out.println(t.name + ": LS=" + t.lateStart + " LF=" + t.lateFinish + " Slack=" + t.slack);
        }
        System.out.println("\n--- CRITICAL PATH ---");
        System.out.println("Total project duration: " + projectDuration + " days");
        for (Task t : allTasks) {
            if (t.slack == 0) {
                System.out.println(t.name + " (duration: " + t.duration + " days)");
            }
        }
    }
}
