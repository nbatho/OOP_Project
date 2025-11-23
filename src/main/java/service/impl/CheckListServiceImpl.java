package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.Checklist;
import main.java.repository.CheckListRepository;
import main.java.service.CheckListService;

public class CheckListServiceImpl implements CheckListService {
    private final CheckListRepository checkListRepository;

    public CheckListServiceImpl() {
        this.checkListRepository = new CheckListRepository();
    }

    @Override
    public boolean createChecklist(Checklist checklist) {
        try {
            if (checklist == null) {
                System.out.println("Checklist không được null");
                return false;
            }

            // Validate thông tin checklist
            if (!isValidChecklistData(checklist)) {
                return false;
            }

            // Tạo ID mới nếu chưa có
            if (checklist.getChecklistId() == null || checklist.getChecklistId().trim().isEmpty()) {
                checklist.setChecklistId(UUID.randomUUID().toString());
            }

            // Kiểm tra checklist đã tồn tại chưa
            if (checkListRepository.findByChecklistId(checklist.getChecklistId()) != null) {
                System.out.println("Checklist ID đã tồn tại: " + checklist.getChecklistId());
                return false;
            }

            // Kiểm tra title đã tồn tại trong task chưa
            if (isTitleExistsInTask(checklist.getTitle(), checklist.getTaskId())) {
                System.out.println("Tiêu đề checklist đã tồn tại trong task này: " + checklist.getTitle());
                return false;
            }

            return checkListRepository.createChecklist(checklist);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo checklist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Checklist> getAllChecklists() {
        try {
            return checkListRepository.findAll();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách checklists: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Checklist getChecklistById(String checklistId) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                System.out.println("Checklist ID không được null hoặc rỗng");
                return null;
            }

            return checkListRepository.findByChecklistId(checklistId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy checklist theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Checklist> getChecklistsByTaskId(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return List.of();
            }

            return checkListRepository.findAll().stream()
                    .filter(checklist -> taskId.equals(checklist.getTaskId()))
                    .toList();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy checklists theo task ID: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Checklist getChecklistByTitle(String title, String taskId) {
        try {
            if (title == null || title.trim().isEmpty()) {
                System.out.println("Tiêu đề không được null hoặc rỗng");
                return null;
            }

            return getChecklistsByTaskId(taskId).stream()
                    .filter(checklist -> title.equalsIgnoreCase(checklist.getTitle()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy checklist theo title: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateChecklist(Checklist checklist) {
        try {
            if (checklist == null) {
                System.out.println("Checklist không được null");
                return false;
            }

            if (!isValidChecklistData(checklist)) {
                return false;
            }

            // Kiểm tra checklist có tồn tại không
            if (!checklistExists(checklist.getChecklistId())) {
                System.out.println("Checklist không tồn tại");
                return false;
            }

            // Kiểm tra title có trùng với checklist khác trong cùng task không
            Checklist existingWithSameTitle = getChecklistByTitle(checklist.getTitle(), checklist.getTaskId());
            if (existingWithSameTitle != null && !existingWithSameTitle.getChecklistId().equals(checklist.getChecklistId())) {
                System.out.println("Tiêu đề checklist đã được sử dụng trong task này: " + checklist.getTitle());
                return false;
            }

            return checkListRepository.updateChecklistTitle(checklist.getChecklistId(), checklist.getTitle());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật checklist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateChecklistTitle(String checklistId, String title) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                System.out.println("Checklist ID không được null hoặc rỗng");
                return false;
            }

            if (title == null || title.trim().isEmpty()) {
                System.out.println("Tiêu đề không được null hoặc rỗng");
                return false;
            }

            if (title.length() > 255) {
                System.out.println("Tiêu đề quá dài (tối đa 255 ký tự)");
                return false;
            }

            Checklist checklist = getChecklistById(checklistId);
            if (checklist == null) {
                System.out.println("Checklist không tồn tại");
                return false;
            }

            // Kiểm tra title có trùng không
            if (isTitleExistsInTask(title.trim(), checklist.getTaskId()) && 
                !title.trim().equals(checklist.getTitle())) {
                System.out.println("Tiêu đề checklist đã tồn tại trong task này: " + title);
                return false;
            }

            checklist.setTitle(title.trim());
            return updateChecklist(checklist);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật tiêu đề checklist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteChecklist(String checklistId) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                System.out.println("Checklist ID không được null hoặc rỗng");
                return false;
            }

            if (!checklistExists(checklistId)) {
                System.out.println("Checklist không tồn tại");
                return false;
            }

            return checkListRepository.deleteByChecklistId(checklistId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa checklist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteChecklistsByTaskId(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return false;
            }

            List<Checklist> checklists = getChecklistsByTaskId(taskId);
            boolean allDeleted = true;

            for (Checklist checklist : checklists) {
                if (!checkListRepository.deleteByChecklistId(checklist.getChecklistId())) {
                    allDeleted = false;
                    System.out.println("Lỗi khi xóa checklist: " + checklist.getChecklistId());
                }
            }

            return allDeleted;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa checklists theo task ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checklistExists(String checklistId) {
        try {
            Checklist checklist = getChecklistById(checklistId);
            return checklist != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra checklist tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTitleExistsInTask(String title, String taskId) {
        try {
            if (title == null || title.trim().isEmpty() || taskId == null || taskId.trim().isEmpty()) {
                return false;
            }

            Checklist existing = getChecklistByTitle(title, taskId);
            return existing != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra title tồn tại: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu checklist
     */
    private boolean isValidChecklistData(Checklist checklist) {
        if (checklist.getTitle() == null || checklist.getTitle().trim().isEmpty()) {
            System.out.println("Tiêu đề checklist không được null hoặc rỗng");
            return false;
        }

        if (checklist.getTitle().length() > 255) {
            System.out.println("Tiêu đề checklist quá dài (tối đa 255 ký tự)");
            return false;
        }

        if (checklist.getTaskId() == null || checklist.getTaskId().trim().isEmpty()) {
            System.out.println("Task ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
