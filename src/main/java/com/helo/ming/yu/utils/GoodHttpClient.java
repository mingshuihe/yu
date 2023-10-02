package com.helo.ming.yu.utils;

import okhttp3.*;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class GoodHttpClient {
    static OkHttpClient client = new OkHttpClient.Builder().build();

    public static String get(String url, Headers headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getImage(String url) {
        Request request = new Request.Builder()
                .url(url)
                .headers(getDefaultHeader(null))
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().byteStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String uploadImage(String url, File file, Map<String, String> headrs) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("policyName", "default-policy")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        Request request = new Request.Builder()
                .url(url).headers(getDefaultHeader(headrs))
                .post(multipartBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String url) {
        return get(url, getDefaultHeader(null));
    }

    public static Headers getDefaultHeader(Map<String, String> headrs) {
        Headers.Builder builder = new Headers.Builder();
        builder.set("'Referer'", "https://www.ahhhhfs.com/");
        builder.set("Sec-Ch-Ua", "\"Google Chrome\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"");
        builder.set("Sec-Ch-Ua-Mobile", "\"Windows\"");
        builder.set("Upgrade-Insecure-Requests", "1");
        builder.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
        if (headrs != null) {
            for (Map.Entry<String, String> entry : headrs.entrySet()) {
                builder.set(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

}
