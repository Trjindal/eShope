package com.eShope.common.entity.Setting;

public class Constants {
    public static final String S3_BASE_URI;

    static {
        String bucketName = "eshope-images-1/";
//        String region = System.getenv("AWS_REGION");
        String pattern = "https://storage.googleapis.com/";
//        eshope-images/user-photos/4/Muhammad%20Evran.png

        S3_BASE_URI = pattern+bucketName;
    }

    public static String changeName(String name){
        return name.replace(" ","%20");
    }


}