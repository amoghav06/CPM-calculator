import java.util.ArrayList;
import java.util.List;

public class Task {
    String name;
    int duration;
    List <Task> dependencies;

    int earlyStart;
    int earlyFinish;
    int lateStart;
    int lateFinish;
    int slack;
    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.dependencies = new ArrayList<>();
    }
    public void addDependency(Task dependsOn) {
        this.dependencies.add(dependsOn);
    }

}

