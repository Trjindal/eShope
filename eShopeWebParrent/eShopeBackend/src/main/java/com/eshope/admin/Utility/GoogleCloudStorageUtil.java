package com.eshope.admin.Utility;

import com.google.api.gax.paging.Page;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GoogleCloudStorageUtil {
    private static final String BUCKET_NAME = "eshope-images";
    private static final Storage storage = StorageOptions.getDefaultInstance().getService();;


    public static List<String> listObjects(String folderName) {
        List<String> listKeys = new ArrayList<>();
        Page<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(folderName));
        System.out.println(blobs);
        for (Blob blob :  blobs.iterateAll()) {
            System.out.println(blob.toString());
            System.out.println(blobs.getValues());
            listKeys.add(blob.getName());
        }

        return listKeys;
    }

    public static void uploadFile(String folderName, String fileName, InputStream content) {
        String objectName = folderName + "/" + fileName;
        Storage storage = StorageOptions.getDefaultInstance().getService();
        System.out.println(storage.toString());
        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        System.out.println(blobId.toString());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        System.out.println(blobInfo.toString());
        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] buffer = new byte[1024];
            int limit;
            while ((limit = content.read(buffer)) >= 0 ) {
                writer.write(ByteBuffer.wrap(buffer, 0, limit));
            }
        } catch (IOException ex) {
            // Handle exception
            log.error("Could not upload file to Google cloud S3", ex);
        }
    }
    public static void deleteFile( String fileName) {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            System.out.println("File deleted: " + fileName);
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

    public static void deleteFolder( String folderName) {
        // List all objects in the folder

        Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(folderName)).iterateAll();

        // Delete each object
        for (Blob blob : blobs) {
            storage.delete(blob.getBlobId());
            System.out.println("Deleted " + blob.getName());
        }
    }

//    public static void deleteObject(String objectName) {
//        BlobId.of(BUCKET_NAME, objectName);
//        boolean deleted = storage.delete(blobId);
//
//        if (deleted) {
//            System.out.println("Deleted: " + objectName);
//        } else {
//            System.out.println("Error: Object " + objectName + " not found");
//        }
//    }
}