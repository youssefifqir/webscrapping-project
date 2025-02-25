package com.youssef.webscrapper_backend.service.facade;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jsoup.nodes.Document;

public interface JsoupService {
    Map<String, Object> scrapeFromUrl(String url) throws IOException;
    Map<String, Object> scrapeFromHtmlFile(File file) throws IOException;
    Map<String, Object> extractDetails(Document doc);
}
