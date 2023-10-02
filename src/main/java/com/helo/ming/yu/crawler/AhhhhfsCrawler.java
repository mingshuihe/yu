package com.helo.ming.yu.crawler;

import com.helo.ming.yu.utils.GoodHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        eles.forEach(item -> {
            Element entryBody = item.select("div[class=entry-wrapper]").get(0);
            Element entryCatDot = entryBody.select("div[class=entry-cat-dot]").get(0);
            Element atag = entryCatDot.tagName("a");
            String tag = atag.text();
            System.out.println();
            System.out.println(tag);
            Element entryTitle = entryBody.select("h2[class=entry-title]").get(0);
            atag = entryTitle.selectFirst("a");
            String atagUrl = atag.attr("href");
            AhhhhfsPageDetailParse pageDetailParse = new AhhhhfsPageDetailParse();
            String content = pageDetailParse.execute(atagUrl);
            String atagText = atag.text();
            System.out.println(atagUrl);
            System.out.println(atagText);
            Element entryDesc = entryBody.select("div[class=entry-desc]").get(0);
            String entryDescText = entryDesc.text();
            System.out.println(entryDescText);
        });
    }

    public static void main(String[] args) {
        AhhhhfsCrawler ahhhhfsCrawler = new AhhhhfsCrawler();
        ahhhhfsCrawler.execute();
    }

}
