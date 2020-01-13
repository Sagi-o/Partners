package com.app.partners.models;

public class Task {
    public String description;
    public String partnerId;
    public String partnerName;
    public String taskId;
    public boolean isDone;
    public String lastCommitId;
    public long timestampPublish;

    public Task() {}

    public Task(String description, String partnerId, String taskId, String lastCommitId, long timestampPublish, String partnerName) {
        this.partnerName = partnerName;
        this.description = description;
        this.partnerId = partnerId;
        this.taskId = taskId;
        this.lastCommitId = lastCommitId;
        this.timestampPublish = timestampPublish;
        this.isDone = false;
    }
}
