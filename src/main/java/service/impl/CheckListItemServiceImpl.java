package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.CheckListItems;
import main.java.repository.CheckListItemRepository;
import main.java.service.CheckListItemService;

public class CheckListItemServiceImpl implements CheckListItemService {
    private final CheckListItemRepository checkListItemRepository;

    public CheckListItemServiceImpl() {
        this.checkListItemRepository = new CheckListItemRepository();
    }

    @Override
    public boolean createCheckListItem(CheckListItems checklistItem) {
        try {
            if (checklistItem == null) {
                System.out.println("ChecklistItem không được null");
                return false;
            }

            // Validate thông tin checklist item
            if (!isValidCheckListItemData(checklistItem)) {
                return false;
            }

            // Tạo ID mới nếu chưa có
            if (checklistItem.getItemId() == null || checklistItem.getItemId().trim().isEmpty()) {
                checklistItem.setItemId(UUID.randomUUID().toString());
            }

            // Kiểm tra item đã tồn tại chưa
            if (checkListItemRepository.findByItemId(checklistItem.getItemId()) != null) {
                System.out.println("CheckList Item ID đã tồn tại: " + checklistItem.getItemId());
                return false;
            }

            // Set default status nếu chưa có
            // isDone là primitive boolean nên không cần kiểm tra null

            return checkListItemRepository.createChecklistItem(checklistItem);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo checklist item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<CheckListItems> getAllCheckListItems() {
        try {
            return checkListItemRepository.findAll();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách checklist items: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public CheckListItems getCheckListItemById(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                System.out.println("Item ID không được null hoặc rỗng");
                return null;
            }

            return checkListItemRepository.findByItemId(itemId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy checklist item theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CheckListItems> getCheckListItemsByChecklistId(String checklistId) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                System.out.println("Checklist ID không được null hoặc rỗng");
                return List.of();
            }

            return checkListItemRepository.findByChecklistId(checklistId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy checklist items theo checklist ID: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean updateCheckListItem(CheckListItems checklistItem) {
        try {
            if (checklistItem == null) {
                System.out.println("ChecklistItem không được null");
                return false;
            }

            if (!isValidCheckListItemData(checklistItem)) {
                return false;
            }

            // Kiểm tra item có tồn tại không
            if (!checkListItemExists(checklistItem.getItemId())) {
                System.out.println("CheckList Item không tồn tại");
                return false;
            }

            return checkListItemRepository.updateChecklistItem(
                checklistItem.getItemId(),
                checklistItem.getContent(),
                checklistItem.isDone()
            );
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật checklist item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCheckListItemContent(String itemId, String content) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                System.out.println("Item ID không được null hoặc rỗng");
                return false;
            }

            if (content == null || content.trim().isEmpty()) {
                System.out.println("Nội dung không được null hoặc rỗng");
                return false;
            }

            if (content.length() > 255) {
                System.out.println("Nội dung quá dài (tối đa 255 ký tự)");
                return false;
            }

            if (!checkListItemExists(itemId)) {
                System.out.println("CheckList Item không tồn tại");
                return false;
            }

            return checkListItemRepository.updateChecklistItemContent(itemId, content.trim());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật nội dung checklist item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCheckListItemStatus(String itemId, boolean isDone) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                System.out.println("Item ID không được null hoặc rỗng");
                return false;
            }

            if (!checkListItemExists(itemId)) {
                System.out.println("CheckList Item không tồn tại");
                return false;
            }

            return checkListItemRepository.updateChecklistItemStatus(itemId, isDone);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái checklist item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCheckListItem(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                System.out.println("Item ID không được null hoặc rỗng");
                return false;
            }

            if (!checkListItemExists(itemId)) {
                System.out.println("CheckList Item không tồn tại");
                return false;
            }

            return checkListItemRepository.deleteByItemId(itemId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa checklist item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCheckListItemsByChecklistId(String checklistId) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                System.out.println("Checklist ID không được null hoặc rỗng");
                return false;
            }

            return checkListItemRepository.deleteByChecklistId(checklistId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa checklist items theo checklist ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkListItemExists(String itemId) {
        try {
            CheckListItems item = getCheckListItemById(itemId);
            return item != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra checklist item tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public double getCompletionPercentage(String checklistId) {
        try {
            if (checklistId == null || checklistId.trim().isEmpty()) {
                return 0.0;
            }

            List<CheckListItems> items = getCheckListItemsByChecklistId(checklistId);
            if (items.isEmpty()) {
                return 0.0;
            }

            long completedCount = items.stream()
                    .mapToLong(item -> item.isDone() ? 1 : 0)
                    .sum();

            return (double) completedCount / items.size() * 100.0;
        } catch (Exception e) {
            System.out.println("Lỗi khi tính phần trăm hoàn thành: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Validate dữ liệu checklist item
     */
    private boolean isValidCheckListItemData(CheckListItems checklistItem) {
        if (checklistItem.getContent() == null || checklistItem.getContent().trim().isEmpty()) {
            System.out.println("Nội dung checklist item không được null hoặc rỗng");
            return false;
        }

        if (checklistItem.getContent().length() > 255) {
            System.out.println("Nội dung checklist item quá dài (tối đa 255 ký tự)");
            return false;
        }

        if (checklistItem.getChecklistId() == null || checklistItem.getChecklistId().trim().isEmpty()) {
            System.out.println("Checklist ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
