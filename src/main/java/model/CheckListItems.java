package main.java.model;

public class CheckListItems {
    private String itemId;
    private String checklistId;
    private String content;
    private boolean isDone;

    public CheckListItems() {}

    public CheckListItems(String itemId, String checklistId, String content, boolean isDone) {
        this.itemId = itemId;
        this.checklistId = checklistId;
        this.content = content;
        this.isDone = isDone;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getChecklistId() {
        return checklistId;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return isDone;
    }

    // Setters
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setChecklistId(String checklistId) {
        this.checklistId = checklistId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "CheckListItems{" +
                "itemId='" + itemId + '\'' +
                ", checklistId='" + checklistId + '\'' +
                ", content='" + content + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
