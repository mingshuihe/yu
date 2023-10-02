package com.helo.ming.yu.crawler;

import com.alibaba.fastjson.JSON;
import com.helo.ming.yu.halo.BlogCreateService;
import com.helo.ming.yu.halo.ImageService;
import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AhhhhfsCrawler {
    String url = "https://www.ahhhhfs.com/page/";

    public void execute() {
        int startPage = 1;
        while (true) {
            String reqUrl = url + startPage;
            String page = GoodHttpClient.get(reqUrl);
            Document doc = Jsoup.parse(page);
            parsePage(doc);
            startPage++;
            System.out.println(page);
            break;
        }
    }

    public void parsePage(Document doc) {
        Element ele = doc.select("#ri_home_lastpost_widget-2").get(0);
        ele = ele.select("#ri_home_lastpost_widget-2").get(0);
        ele = ele.select("div > section > div").get(1);
        Elements eles = ele.select("div[class=col]");
        System.out.println(eles.outerHtml());

        for(Element item:eles){
            HaloBlog haloBlog = new HaloBlog();
            Element entryMedia = item.select("div[class^=entry-media]").get(0);

            String cover = getCover(entryMedia);
            haloBlog.setCover(cover);// 设置封面图片

            Element entryWrapper = item.select("div[class=entry-wrapper]").get(0);

            String tags = getTag(entryWrapper);
            haloBlog.setTag(tags);//标签

            String title = getTitle(entryWrapper);
            haloBlog.setTitle(title);//文章标题

            String url = getPageUrl(entryWrapper);
            haloBlog.setPageUrl(url);//文章对应的链接

            String excerpt = getExcerpt(entryWrapper);
            haloBlog.setExcerpt(excerpt);//文章摘要

            AhhhhfsPageDetailParse pageDetailParse = new AhhhhfsPageDetailParse();
            String content = pageDetailParse.execute(url);
            haloBlog.setContent(content); //正文内容

            String uuid = UUID.randomUUID().toString();
            haloBlog.setName(uuid);
            haloBlog.setSlug(uuid);

            System.out.println(JSON.toJSONString(haloBlog));
            BlogCreateService blogCreateService = new BlogCreateService();

            blogCreateService.create(haloBlog);
            break;
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
        ImageService imageService = new ImageService();
        return imageService.upload(dataBgUrl);
    }

    public static void main(String[] args) {
        AhhhhfsCrawler ahhhhfsCrawler = new AhhhhfsCrawler();
        ahhhhfsCrawler.execute();
    }

}
