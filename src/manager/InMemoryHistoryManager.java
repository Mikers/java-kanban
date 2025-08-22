package manager;

import model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;
    private static final int HISTORY_LIMIT = 10;
    
    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }
    
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() >= HISTORY_LIMIT) {
            history.removeFirst();
        }
        history.add(task);
    }
    
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}