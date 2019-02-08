package ru.kpfu.itis.crawler;

public class Application {

    private static final String START_URL = "https://habr.com/ru/";

    public static void main(String[] args) {
        Spider spider = new Spider(START_URL);
        spider.startCrawl();
    }
}
