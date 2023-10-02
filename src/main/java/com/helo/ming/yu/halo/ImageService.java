package com.helo.ming.yu.halo;

import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageService {

    String url = "http://192.168.61.130:8090/apis/api.console.halo.run/v1alpha1/attachments/upload";

    public String upload(File file) {
        Map<String, String> headrs = new HashMap<>();
        headrs.put("Cookie", "XSRF-TOKEN=45ec5f3a-00ca-4df6-b2a5-a6d5197df026; SESSION=72d659f4-2aed-4d52-83a9-8a3ee8982396");
        headrs.put("X-Xsrf-Token", "45ec5f3a-00ca-4df6-b2a5-a6d5197df026");
        String res = GoodHttpClient.uploadImage(url, file, headrs);

        return "/upload/"+file.getName();
    }

    public String upload(String sourceUrl) {
        try {
            InputStream in = GoodHttpClient.getImage(sourceUrl);
            String fileName = "C:\\Users\\mingdong\\Desktop\\apic\\" + UUID.randomUUID() + ".jpg";
            File file = new File(fileName);
            FileUtils.copyInputStreamToFile(in, file);
            return upload(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
