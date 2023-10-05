package com.helo.ming.yu.halo;

import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService  extends HaloBaseService{

    String url = "/apis/api.console.halo.run/v1alpha1/attachments/upload";

    public String upload(File file) {
        Map<String, String> headrs = new HashMap<>();
        headrs.put("Cookie", COOKIE);
        headrs.put("X-Xsrf-Token", XSRF);
        GoodHttpClient.uploadImage(HALO_HOST+url, file, headrs);
        return "/upload/"+file.getName();
    }

    public String upload(String sourceUrl) {
        try {
            InputStream in = GoodHttpClient.getImage(sourceUrl);
            String fileName = IMG_DIR + UUID.randomUUID() + ".jpg";
            File file = new File(fileName);
            FileUtils.copyInputStreamToFile(in, file);
            return upload(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
