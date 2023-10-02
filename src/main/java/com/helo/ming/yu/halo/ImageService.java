package com.helo.ming.yu.halo;

import com.helo.ming.yu.utils.GoodHttpClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageService {

    String url = "http://192.168.61.130:8090/apis/api.console.halo.run/v1alpha1/attachments/upload";

    public String upload(File file) {
        Map<String, String> headrs = new HashMap<>();
        headrs.put("Cookie", "XSRF-TOKEN=98e71aae-8673-4176-9b71-71a4e2f02b5b; SESSION=10615505-83ab-4e27-a5e1-dca70faf8619");
        headrs.put("X-Xsrf-Token", "98e71aae-8673-4176-9b71-71a4e2f02b5b");
        String res = GoodHttpClient.uploadImage(url, file, headrs);

        return "/upload/"+file.getName();
    }
}
