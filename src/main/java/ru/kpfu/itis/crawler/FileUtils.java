package ru.kpfu.itis.crawler;

import java.io.*;
import java.util.logging.Logger;

public class FileUtils {

    private static final String LINK_STORAGE_PATH = "data/crawl/index.txt";
    private static final String TEXT_STORAGE_PATH = "data/crawl/texts/";

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    private static int linksCount = 0;

    public static void savePageText(String linkUrl, String text) {

        try (BufferedWriter lsw = new BufferedWriter(new FileWriter(new File(LINK_STORAGE_PATH), true))) {
            lsw.write(linkUrl + "\n");
            linksCount++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter tsw = new BufferedWriter(new FileWriter(new File(TEXT_STORAGE_PATH + linksCount + ".txt")))) {
            tsw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
