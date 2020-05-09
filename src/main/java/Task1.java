//1) Вывести список файлов заданной директории рекурсивно

import java.io.File;

public class Task1 {
    private static final String ROOT = "C:\\";

    public static void main(String[] args) {
        File root = new File(ROOT);
        getFileTree(root, "");
    }

    public static void getFileTree(File root, String padding) {
        try {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    File tmp = new File(file.getAbsolutePath());
                    getFileTree(tmp, "  " + padding);
                } else {
                    System.out.println(padding + file.getName());
                }
            }
        } catch (Exception e) {

        }
    }
}
