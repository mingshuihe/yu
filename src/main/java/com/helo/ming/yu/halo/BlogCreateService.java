package com.helo.ming.yu.halo;

import com.alibaba.fastjson.TypeReference;
import com.helo.ming.yu.halo.dto.HaloCreateResult;
import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class BlogCreateService extends HaloBaseService{

    String createUrl = "/apis/api.console.halo.run/v1alpha1/posts";

    String publishUrl = "/apis/api.console.halo.run/v1alpha1/posts/%s/publish";

    public String createAndPublish(HaloBlog blog) {
        try {
            //File file = new File("D:\\DEV\\ideaproject\\yu\\src\\main\\resources\\haloparam\\create.json");
            File file = ResourceUtils.getFile("classpath:haloparam\\create.json");
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
            headrs.put("Cookie", COOKIE);
            headrs.put("X-Xsrf-Token", XSRF);
            // 这里只是保存,并未发布
            HaloCreateResult res = GoodHttpClient.doPostJson(HALO_HOST+createUrl, body,headrs, new TypeReference<HaloCreateResult>(){});

            blog.setCreateStatus("success");

            //File pfile = new File("D:\\DEV\\ideaproject\\yu\\src\\main\\resources\\haloparam\\publish.json");
            File pfile = ResourceUtils.getFile("classpath:haloparam\\publish.json");
            body = FileUtils.readFileToString(pfile, "utf-8");
            body = body.replaceAll("#title#",blog.getTitle());
            body = body.replaceAll("#slug#",blog.getSlug());
            body = body.replaceAll("#cover#",blog.getCover());
            body = body.replaceAll("#excerpt#",blog.getExcerpt());
            body = body.replaceAll("#releaseSnapshot#",res.getSpec().getBaseSnapshot());
            body = body.replaceAll("#name#",blog.getName());
            body = body.replaceAll("#checksum#",blog.getCheckSum());
            publishUrl = String.format(publishUrl,blog.getName());
            // 这里只是发布
            String result = GoodHttpClient.doPutJson(HALO_HOST+publishUrl, body,headrs, new TypeReference<String>(){});
            blog.setPublishStatus("success");
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
