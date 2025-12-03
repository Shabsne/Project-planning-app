package org.example.projectplanningapp.utils.taskComparators;

import org.example.projectplanningapp.models.Task;
import java.util.Comparator;

public class DeadlineComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        if (t1.getDeadline() == null && t2.getDeadline() == null) return 0;
        if (t1.getDeadline() == null) return 1;
        if (t2.getDeadline() == null) return -1;

        return t1.getDeadline().compareTo(t2.getDeadline());
    }
}
