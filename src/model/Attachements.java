package model;

public class Attachements {
    private String file_path;
    private String uploaded_at;
    private int task_id;
    private int user_id;

    public void uploadFile() {}
    public void deleteFile() {}

    public int getTask_id() {
        return task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getUploaded_at() {
        return uploaded_at;
    }
}
