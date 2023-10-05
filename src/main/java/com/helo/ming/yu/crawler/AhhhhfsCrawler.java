package com.helo.ming.yu.crawler;

import com.helo.ming.yu.halo.BlogCreateService;
import com.helo.ming.yu.halo.ImageService;
import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.service.HeloService;
import com.helo.ming.yu.utils.GoodHttpClient;
import com.helo.ming.yu.utils.MD5Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AhhhhfsCrawler {

    @Autowired
    private HeloService heloService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BlogCreateService blogCreateService;
    @Autowired
    private AhhhhfsPageDetailParse ahhhhfsPageDetailParse;
    String url = "https://www.ahhhhfs.com/page/";

    public void execute() {
        int startPage = 1;
        while (true) {
            try {
                String reqUrl = url + startPage;
                String page = GoodHttpClient.get(reqUrl);
                // 到了结尾
                if (page.contains("发布的内容不见了怎么办")) {
                    break;
                }
                Document doc = Jsoup.parse(page);
                parsePage(doc, startPage);
                startPage++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void parsePage(Document doc,int startPage) {
        Element ele = doc.select("#ri_home_lastpost_widget-2").get(0);
        ele = ele.select("#ri_home_lastpost_widget-2").get(0);
        ele = ele.select("div > section > div").get(1);
        Elements eles = ele.select("div[class=col]");

        for(int i=0;i<eles.size();i++){
            System.out.println("*************************第"+startPage+"轮第"+i+"次开始***************************************");
            Element item = eles.get(i);
            parseAndCreate(item);
            System.out.println("*************************第"+startPage+"轮第"+i+"次结束***************************************");
        }

    }

    public void parseAndCreate(Element item) {
        HaloBlog haloBlog = new HaloBlog();
        haloBlog.setCreateStatus("fail");
        haloBlog.setPublishStatus("fail");
        haloBlog.setHasError("no");
        try {
            Element entryMedia = item.select("div[class^=entry-media]").get(0);

            String cover = getCover(entryMedia);
            haloBlog.setCover(cover);// 设置封面图片

            Element entryWrapper = item.select("div[class=entry-wrapper]").get(0);

            String tags = getTag(entryWrapper);
            haloBlog.setTag(tags);//标签

            String title = getTitle(entryWrapper);
            haloBlog.setTitle(title);//文章标题

            String detailUrl = getPageUrl(entryWrapper);
            haloBlog.setPageUrl(detailUrl);//文章对应的链接

            String excerpt = getExcerpt(entryWrapper);
            haloBlog.setExcerpt(excerpt);//文章摘要

            String content = ahhhhfsPageDetailParse.execute(detailUrl);
            haloBlog.setContent(content); //正文内容

            String checkSum = MD5Util.getMd5Str(title + "$" + content);
            haloBlog.setCheckSum(MD5Util.getMd5Str(checkSum));// 根据标题和内容计算md5
            HaloBlog blog = heloService.findByCheckSum(checkSum);
            if (blog != null) {
                return;
            }

            String uuid = UUID.randomUUID().toString();
            haloBlog.setName(uuid);
            haloBlog.setSlug(uuid);

            blogCreateService.createAndPublish(haloBlog);

        } catch (Exception e) {
            e.printStackTrace();
            haloBlog.setHasError("yes");
            haloBlog.setErrorMsg(e.getMessage());
        }
        try {
            heloService.save(haloBlog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getTag(Element entryWrapper) {
        Element entryCatDot = entryWrapper.select("div[class=entry-cat-dot]").get(0);
        Elements tags = entryCatDot.select("a");
        if (tags.size() == 0) {
            return "";
        }
        List<String> list = new ArrayList<>();
        for (Element ele : tags) {
            list.add(ele.text());
        }
        String s = String.join(";", list);
        return s;
    }

    public String getTitle(Element entryWrapper) {
        Element entryTitle = entryWrapper.select("h2[class=entry-title]").get(0);
        Element atag = entryTitle.selectFirst("a");
        String title = atag.text();
        return title;
    }

    public String getPageUrl(Element entryWrapper) {
        Element entryTitle = entryWrapper.select("h2[class=entry-title]").get(0);
        Element atag = entryTitle.selectFirst("a");
        String url = atag.attr("href");
        return url;
    }

    public String getExcerpt(Element entryWrapper) {
        Element entryDesc = entryWrapper.select("div[class=entry-desc]").get(0);
        String excerpt = entryDesc.text();
        return excerpt;
    }

    public String getCover(Element entryMedia) {
        Element atag = entryMedia.select("a").get(0);
        String dataBgUrl = atag.attr("data-bg");
        return imageService.upload(dataBgUrl);
    }

    public static void main(String[] args) {
        AhhhhfsCrawler ahhhhfsCrawler = new AhhhhfsCrawler();
        ahhhhfsCrawler.execute();

    }

}
