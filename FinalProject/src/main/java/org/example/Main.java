package org.example;

/**
 * @author tippy091
 * @created 13/07/2025
 * @project Default (Template) Project
 **/
public class Main {
    public static void main(String[] args) {
            PersonalTaskManagerViolations manager = new PersonalTaskManagerViolations();

            System.out.println("\n  Thêm nhiệm vụ hợp lệ:");
            manager.addNewTask("Mua sách", "Sách Công nghệ phần mềm.", "2025-07-20", "Cao");

            System.out.println("\n  Thêm nhiệm vụ trùng lặp:");
            manager.addNewTask("Mua sách", "Sách Công nghệ phần mềm.", "2025-07-20", "Cao");

            System.out.println("\n  Thêm nhiệm vụ với tiêu đề rỗng:");
            manager.addNewTask("", "Nhiệm vụ không có tiêu đề.", "2025-07-22", "Thấp");

            System.out.println("\n  Thêm nhiệm vụ với mức độ ưu tiên không hợp lệ:");
            manager.addNewTask("Làm bài tập", "Ôn thi cuối kỳ.", "2025-07-25", "Rất cao");
    }
}