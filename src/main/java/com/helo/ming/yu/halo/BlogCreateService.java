package com.helo.ming.yu.halo;

import com.alibaba.fastjson.TypeReference;
import com.helo.ming.yu.halo.dto.HaloCreateResult;
import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BlogCreateService {

    String url = "http://192.168.61.130:8090/apis/api.console.halo.run/v1alpha1/posts";

    String publishUrl = "http://192.168.61.130:8090/apis/api.console.halo.run/v1alpha1/posts/%s/publish";

    public String createAndPublish(HaloBlog blog) {
        try {
            File file = new File("D:\\DEV\\ideaproject\\yu\\src\\main\\resources\\haloparam\\create.json");
            String body = FileUtils.readFileToString(file, "utf-8");
            body = body.replaceAll("#title#",blog.getTitle());
            body = body.replaceAll("#slug#",blog.getSlug());
            body = body.replaceAll("#cover#",blog.getCover());
            body = body.replaceAll("#excerpt#",blog.getExcerpt());
            body = body.replaceAll("#name#",blog.getName());
            String content = blog.getContent();
            content = content.replaceAll("\\\"","\\\\\"");
            body = body.replace("#content#",content);
            Map<String, String> headrs = new HashMap<>();
            headrs.put("Cookie", "XSRF-TOKEN=45ec5f3a-00ca-4df6-b2a5-a6d5197df026; SESSION=eb46ba35-50be-4fe1-a01c-8e3dc28feb28");
            headrs.put("X-Xsrf-Token", "45ec5f3a-00ca-4df6-b2a5-a6d5197df026");
            HaloCreateResult res = GoodHttpClient.doPostJson(url, body,headrs, new TypeReference<HaloCreateResult>(){});

            File pfile = new File("D:\\DEV\\ideaproject\\yu\\src\\main\\resources\\haloparam\\publish.json");
            body = FileUtils.readFileToString(pfile, "utf-8");
            body = body.replaceAll("#title#",blog.getTitle());
            body = body.replaceAll("#slug#",blog.getSlug());
            body = body.replaceAll("#cover#",blog.getCover());
            body = body.replaceAll("#excerpt#",blog.getExcerpt());
            body = body.replaceAll("#releaseSnapshot#",res.getSpec().getBaseSnapshot());
            body = body.replaceAll("#name#",blog.getName());
            publishUrl = String.format(publishUrl,blog.getName());
            String result = GoodHttpClient.doPutJson(publishUrl, body,headrs, new TypeReference<String>(){});
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
