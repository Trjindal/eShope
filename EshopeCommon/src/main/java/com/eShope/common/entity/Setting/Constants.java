package com.eShope.common.entity.Setting;

public class Constants {
    public static final String S3_BASE_URI;

    static {
        String bucketName = "eshope-images/";
//        String region = System.getenv("AWS_REGION");
        String pattern = "https://storage.googleapis.com/";


        S3_BASE_URI = pattern+bucketName;
    }

    public static String changeName(String name){
        return name.replace(" ","%20");
    }


}