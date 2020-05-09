
//2*) Скачать файл src.zip (программно), разархивировать его и вывести имена всех файлов в которых встречается строка @FunctionalInterface
//https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Task2 {

    private static final String SRC_URL = "https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip";
    private static final String ZIP_PATH = System.getProperty("user.dir") + SRC_URL.substring(SRC_URL.lastIndexOf("/"));
    private static final String KEY = "@FunctionalInterface";

    public static void main(String[] args) throws Exception {
        download(SRC_URL, ZIP_PATH);
        System.out.println("Initialize unzip:\n");
        String destinationDir = System.getProperty("user.dir") + "\\Output";
        unzip(ZIP_PATH, destinationDir, "");
        System.out.println("\nUnzip complete\n");
        System.out.println("Looking for files with " + "\"" + KEY + "\" :\n");
        getFilesWithKey(new File(destinationDir), KEY);
    }

    public static void download(String url, String savePath) throws Exception {
        File file = new File(savePath);
        if (!file.exists()) {
            download(url, savePath);
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client
                .newCall(request)
                .execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }
        FileOutputStream fos = new FileOutputStream(savePath);
        fos.write(Objects.requireNonNull(response.body()).bytes());
        fos.close();
    }

    private static void unzip(String zipFilePath, String destDir, String lifeSign) {
        long start = System.currentTimeMillis();
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                if (System.currentTimeMillis() >= start + 500) {
                    System.out.print(".");
                    start = System.currentTimeMillis();
                }
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();

            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getFilesWithKey(File folder, String key) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                getFilesWithKey(new File(file.getAbsolutePath()), key);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(key)) {
                            System.out.println(file.getParent() + file.getName().substring(file.getName().lastIndexOf("/") + 1));
                        }
                    }
                } catch (IOException e) {

                }
            }
        }
    }

}
