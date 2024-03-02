package com.eshope.admin;

import com.eshope.admin.Utility.GoogleCloudStorageUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class GoogleCloudStorageUtilTest {
    @Test
    public void testListFolder() {
        String folderName = "category-photos/1/";
        List<String> listKeys = GoogleCloudStorageUtil.listObjects(folderName);
        listKeys.forEach(System.out::println);
    }

    @Test
    public void testUploadFile() throws FileNotFoundException {
        String folderName = "test-upload";
        String fileName = "Tushar.jpg";
        String filePath = "D:\\docs\\" + fileName;

        InputStream inputStream = new FileInputStream(filePath);

        GoogleCloudStorageUtil.uploadFile(folderName, fileName, inputStream);
    }


    @Test
    public void testDeleteFile() {
        String fileName = "test-upload/119411.jpg";
        GoogleCloudStorageUtil.deleteFile(fileName);
    }

    @Test
    public void testRemoveFolder() {
        String folderName = "test-upload";
        GoogleCloudStorageUtil.deleteFolder(folderName);
    }
}
