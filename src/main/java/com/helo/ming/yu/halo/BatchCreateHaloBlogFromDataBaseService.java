package com.helo.ming.yu.halo;

import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.service.HeloService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class BatchCreateHaloBlogFromDataBaseService extends HaloBaseService{

    @Autowired
    private HeloService heloService;

    @Autowired
    private BlogCreateService blogCreateService;

    @Autowired
    private ImageService imageService;

    public void execute(){
        List<HaloBlog> list = heloService.findAll();
        for(HaloBlog haloBlog:list){
            if(haloBlog.getHasError().equals("yes")){
                continue;
            }
            create(haloBlog);
        }
    }

    public void create(HaloBlog haloBlog){
        String coverUrl = haloBlog.getCover();
        // 重新上传封面
        String newCoverPath = getNewImgPath(coverUrl);
        haloBlog.setCover(newCoverPath);
        String content = haloBlog.getContent();
        Document doc = Jsoup.parse(haloBlog.getContent());
        Elements eles = doc.select("img");
        if(eles != null || eles.size() != 0){
            for(Element ele:eles){
                //重新上传详情页里面的图片
                String attImgUrl =  ele.attr("src");
                String newAttImgUrl = getNewImgPath(attImgUrl);
                content = content.replace(attImgUrl,newAttImgUrl);
            }
        }
        haloBlog.setContent(content);
        blogCreateService.createAndPublish(haloBlog);
    }

    public String getNewImgPath(String imgUrl){
        // 不包含/
        String path = IMG_DIR + imgUrl.substring(imgUrl.lastIndexOf("/")+1);
        return imageService.upload(new File(path));
    }
}
