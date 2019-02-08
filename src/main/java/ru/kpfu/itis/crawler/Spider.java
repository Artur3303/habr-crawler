package ru.kpfu.itis.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Spider {
    private static final int MAX_PAGES_TO_FETCH = 100;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;
    private String startUrl;

    private static final Logger LOGGER = Logger.getLogger(Spider.class.getName());

    public Spider(String startUrl) {
        pagesVisited = new HashSet<>();
        pagesToVisit = new LinkedList<>();
        pagesToVisit.add(startUrl);
        this.startUrl = startUrl;
        LOGGER.info("Spider initialized. Start url is <" + startUrl + ">");
    }

    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = pagesToVisit.remove(0);
        } while (pagesVisited.contains(nextUrl));
        return nextUrl;
    }

    private void crawl(String url) {
        try {
            LOGGER.info("Connecting to <" + url + ">");
            Document page = Jsoup.connect(url).get();
            LOGGER.info("Page content successfully fetched");
            String pageText = page.body().text();
            FileUtils.savePageText(url, pageText);
            List<String> urls = page.body()
                                    .select("a[href]")
                                    .stream()
                                    .map(link -> link.attr("href"))
                                    .filter(link -> link.startsWith(startUrl))
                                    .filter(link -> !link.matches(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$"))
                                    .collect(Collectors.toList());
            LOGGER.info("Number of links on the current page: " + urls.size());
            if (!urls.isEmpty()) {
                pagesToVisit.addAll(urls);
                LOGGER.info("New links added to queue");
            }
            pagesVisited.add(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCrawl() {
        LOGGER.info("Crawl starting...");
        while (pagesVisited.size() < MAX_PAGES_TO_FETCH) {
            String url = nextUrl();
            crawl(url);
        }
    }
}
