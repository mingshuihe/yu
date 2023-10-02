package com.helo.ming.yu.halo;

import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BlogCreateService {

    String url = "http://192.168.61.130:8090/apis/api.console.halo.run/v1alpha1/posts";

    public String create(HaloBlog blog) {
        try {
            File file = new File("D:\\DEV\\ideaproject\\yu\\src\\main\\resources\\haloparam\\create.json");
            String body = FileUtils.readFileToString(file, "utf-8");
            body = body.replaceAll("#title#",blog.getTitle());
            body = body.replaceAll("#slug#",blog.getSlug());
            body = body.replaceAll("#cover#",blog.getCover());
            body = body.replaceAll("#name#",blog.getName());
            String content = blog.getContent();
            content = content.replaceAll("\\\"","\\\\\"");
            body = body.replace("#content#",content);
            Map<String, String> headrs = new HashMap<>();
            headrs.put("Cookie", "XSRF-TOKEN=45ec5f3a-00ca-4df6-b2a5-a6d5197df026; SESSION=72d659f4-2aed-4d52-83a9-8a3ee8982396");
            headrs.put("X-Xsrf-Token", "45ec5f3a-00ca-4df6-b2a5-a6d5197df026");
            String res = GoodHttpClient.doPostJson(url, body,headrs);
            return res;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
