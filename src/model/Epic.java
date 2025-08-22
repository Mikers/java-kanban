package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds.isEmpty() ? Collections.emptyList() : new ArrayList<>(subtaskIds);
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        if (subtaskIds == null) {
            this.subtaskIds = new ArrayList<>();
        } else {
            if (subtaskIds.contains(this.getId())) {
                throw new IllegalArgumentException("Epic cannot contain itself as a subtask");
            }
            this.subtaskIds = new ArrayList<>(subtaskIds);
        }
    }

    public void addSubtaskId(int subtaskId) {
        if (subtaskId == this.getId()) {
            throw new IllegalArgumentException("Epic cannot add itself as a subtask");
        }
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}