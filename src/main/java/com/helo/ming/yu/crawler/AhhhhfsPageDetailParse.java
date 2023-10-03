package com.helo.ming.yu.crawler;

import com.helo.ming.yu.halo.ImageService;
import com.helo.ming.yu.utils.GoodHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AhhhhfsPageDetailParse {

    @Autowired
    private ImageService imageService;

    public String execute(String url) {
        String page = GoodHttpClient.get(url);
        Document doc = Jsoup.parse(page);
        Element ele = doc.select("div[class=card]").get(0);
        ele = ele.select("article").get(0);
        Elements eles = ele.select("article>p,article>h1,article>h2,article>h3");
        //去掉本文链接
        eles.remove(eles.size() - 1);
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
            atagUrl = imageService.upload(atagUrl);
            aTag.remove();
            e.insertChildren(0, getImgElement(atagUrl));
        }
        String content = eles.outerHtml();
        content = content.replaceAll("\n   ", "");
        content = content.replaceAll("\n  ", "");
        content = content.replaceAll("\n ", "");
        content = content.replaceAll("\n", "");
        return content;
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
