package com.youssef.webscrapper_backend.service.impl;

import com.youssef.webscrapper_backend.service.facade.JsoupService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class JsoupServiceImpl implements JsoupService {
    /*@Override
    public Map<String, Object> scrapeFromUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return extractDetails(doc);
    }

    @Override
    public Map<String, Object> scrapeFromHtmlFile(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        return extractDetails(doc);..6
    }*/
    @Override
    public Map<String, Object> scrapeFromUrl(String url) throws IOException {
        long startTime = System.currentTimeMillis(); // Start timing

        Document doc = Jsoup.connect(url).get();
        Map<String, Object> data = extractDetails(doc);

        long endTime = System.currentTimeMillis(); // End timing
        data.put("parsing_time_ms", endTime - startTime); // Store performance data

        return data;
    }

    @Override
    public Map<String, Object> scrapeFromHtmlFile(File file) throws IOException {
        long startTime = System.currentTimeMillis(); // Start timing

        Document doc = Jsoup.parse(file, "UTF-8");
        Map<String, Object> data = extractDetails(doc);

        long endTime = System.currentTimeMillis(); // End timing
        data.put("parsing_time_ms", endTime - startTime); // Store performance data

        return data;
    }



    @Override
    public Map<String, Object> extractDetails(Document doc) {
        Map<String, Object> data = new HashMap<>();


        // Meta Information
        data.put("title", doc.title());

        // Open Graph / Social Media Meta Tags
        Elements ogTags = doc.select("meta[property^=og:]");
        Map<String, String> ogData = new HashMap<>();
        for (Element tag : ogTags) {
            ogData.put(tag.attr("property"), tag.attr("content"));
        }
        data.put("open_graph", ogData);

        // Headings (h1, h2, h3, etc.)
        Map<String, String> headings = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            Elements hTags = doc.select("h" + i);
            headings.put("h" + i, hTags.text());
        }
        data.put("headings", headings);

        // Paragraphs & Lists
        data.put("paragraphs", doc.select("p").text());
        data.put("lists", doc.select("ul, ol").text());

        // Links
        Elements links = doc.select("a[href]");
        Map<String, String> linkData = new HashMap<>();
        for (Element link : links) {
            linkData.put(link.text(), link.absUrl("href"));
        }
        data.put("links", linkData);

        // Images
        Elements images = doc.select("img");
        Map<String, String> imageData = new HashMap<>();
        for (Element img : images) {
            imageData.put(img.attr("alt"), img.absUrl("src"));
        }
        data.put("images", imageData);

        // Extract Additional Attributes
        String[] keywords = {"seller", "price", "location", "category", "product", "brand", "discount", "item"};
        for (String keyword : keywords) {
            Elements elements = doc.select("[class*=" + keyword + "], [id*=" + keyword + "]");
            data.put(keyword, elements.text());
        }

        return data;
    }

}
