package com.helo.ming.yu.crawler;

import com.helo.ming.yu.halo.ImageService;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public class AhhhhfsPageDetailParse {

    public String execute(String url) {

        String reqUrl = url;
        String page = GoodHttpClient.get(reqUrl);
        Document doc = Jsoup.parse(page);
        Element ele = doc.select("div[class=card]").get(0);
        ele = ele.select("article").get(0);

        Elements eles = ele.select("article>p,article>h1,article>h2,article>h3");
        //去掉本文链接
        eles.remove(eles.size() - 1);
        System.out.println(eles.outerHtml());

        for (Element e : eles) {
            Elements as = e.select("a");
            if (as.size() == 0) {
                continue;
            }
            Element aTag = as.get(0);
            String atagUrl = aTag.attr("href");
            if (!atagUrl.endsWith(".jpg")) {
                if (atagUrl.contains("ahhhhfs.com")) {
                    aTag.remove();
                }
                continue;
            }
            atagUrl = convertToMyImage(atagUrl);
            aTag.remove();
            e.insertChildren(0, getImgElement(atagUrl));
        }
        String content = eles.outerHtml();
        content = content.replaceAll("\n   ","");
        content = content.replaceAll("\n  ","");
        content = content.replaceAll("\n ","");
        content = content.replaceAll("\n","");
        System.out.println(content);
        return content;
    }

    public String convertToMyImage(String atagUrl) {
        try {
            InputStream in = GoodHttpClient.getImage(atagUrl);
            String fileName = "C:\\Users\\mingdong\\Desktop\\apic\\" + UUID.randomUUID().toString() + ".jpg";
            File file = new File(fileName);
            FileUtils.copyInputStreamToFile(in, file);
            ImageService imageService = new ImageService();
            imageService.upload(file);
            return "http://www.baidu.com/a.jpg";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Element getImgElement(String url) {
        Element ele = new Element("img");
        ele.attr("src", url);
        ele.attr("width", "794.375px");
        ele.attr("height", "507.5625px");
        ele.attr("style", "display: inline-block");
        ele.attr("loading", "lazy");
        return ele;
    }

}
