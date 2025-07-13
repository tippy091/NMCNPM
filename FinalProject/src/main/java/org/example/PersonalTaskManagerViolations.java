package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.UUID;

public class PersonalTaskManagerViolations {
    private static final String DB_FILE_PATH = "tasks_database.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Phương thức trợ giúp để tải dữ liệu (sẽ được gọi lặp lại)
    private static JSONArray loadTasksFromDb() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.err.println("Lỗi khi đọc file database: " + e.getMessage());
        }
        return new JSONArray();
    }

    private boolean isValidPriority(String level) {
        return Arrays.asList("Thấp", "Trung Bình", "Cao").contains(level);
    }

    private boolean isDuplicate(JSONArray tasks, String title, String dueDate) {
        for(Object obj : tasks) {
            JSONObject task = (JSONObject) obj;
            if (task.get("title").toString().equalsIgnoreCase(title) &&
                    task.get("due_date").toString().equals(dueDate)) {
                return true;
            }
        }
        return false;
    }

    // Phương thức trợ giúp để lưu dữ liệu
    private static void saveTasksToDb(JSONArray tasksData) {
        try (FileWriter file = new FileWriter(DB_FILE_PATH)) {
            file.write(tasksData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file database: " + e.getMessage());
        }
    }
    /**
     * Chức năng thêm nhiệm vụ mới
     * @param title Tiêu đề nhiệm vụ.
     * @param description Mô tả nhiệm vụ.
     * @param dueDateStr Ngày đến hạn (định dạng YYYY-MM-DD).
     * @param priorityLevel Mức độ ưu tiên ("Thấp", "Trung bình", "Cao").
     * @return JSONObject của nhiệm vụ đã thêm, hoặc null nếu có lỗi.
     */

    public JSONObject addNewTask(String title, String description,
                                               String dueDateStr, String priorityLevel) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Lỗi: Tiêu đề không được để trống.");
            return null;
        }
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            System.out.println("Lỗi: Ngày đến hạn không được để trống.");
            return null;
        }

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi: Ngày đến hạn không hợp lệ. Vui lòng dùng định dạng YYYY-MM-DD.");
            return null;
        }

        if (!isValidPriority(priorityLevel)) {
            System.out.println("Lỗi: Mức độ ưu tiên không hợp lệ. Chọn một trong: Thấp, Trung bình, Cao.");
            return null;
        }

        JSONArray tasks = loadTasksFromDb();
        if (isDuplicate(tasks, title, dueDateStr)) {
            System.out.printf("Lỗi: Nhiệm vụ '%s' đã tồn tại với cùng ngày đến hạn.%n", title);
            return null;
        }

        // Create new task
        JSONObject newTask = new JSONObject();
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        newTask.put("id", UUID.randomUUID().toString());
        newTask.put("title", title);
        newTask.put("description", description);
        newTask.put("due_date", dueDate.format(DATE_FORMATTER));
        newTask.put("priority", priorityLevel);
        newTask.put("status", "Chưa hoàn thành");
        newTask.put("created_at", now);
        newTask.put("last_updated_at", now);

        tasks.add(newTask);
        saveTasksToDb(tasks);

        System.out.printf("Đã thêm nhiệm vụ mới với ID: %s%n", newTask.get("id"));
        return newTask;
    }
}
