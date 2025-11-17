package model;

public class CheckListItems {
    private String item_id;
    private String checklist_id;
    private String content;
    private boolean is_done;

    public CheckListItems() {}

    public CheckListItems(String item_id, String checklist_id, String content, boolean is_done) {
        this.item_id = item_id;
        this.checklist_id = checklist_id;
        this.content = content;
        this.is_done = is_done;
    }

    //Getter
    public String getItem_id() {
        return item_id;
    }

    public String getChecklist_id() {
        return checklist_id;
    }

    public String getContent() {
        return content;
    }

    public boolean isIs_done() {
        return is_done;
    }

    //Setter
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    @Override
    public String toString() {
        return "CheckListItems{" +
                "item_id='" + item_id + '\'' +
                ", checklist_id='" + checklist_id + '\'' +
                ", content='" + content + '\'' +
                ", is_done=" + is_done +
                '}';
    }
}
