package com.youssef.webscrapper_backend.restController;

import com.youssef.webscrapper_backend.service.facade.JsoupService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@Controller
@RequestMapping("/api/scrape")
public class JsoupController {

    private final JsoupService jsoupService;

    public JsoupController(JsoupService jsoupService) {
        this.jsoupService = jsoupService;
    }

    // 1. Generate .txt file from URL (GET Request)
    @GetMapping("/url/txt")
    public ResponseEntity<Resource> scrapeUrlTxt(@RequestParam String url) throws IOException {
        // Scrape data
        Map<String, Object> scrapedData = jsoupService.scrapeFromUrl(url);

        // Create a .txt file with scraped data
        File tempFile = createTxtFile(scrapedData);

        // Prepare file as Resource for downloading
        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

        // Set the file name and content type for download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    // 2. Generate .xml file from URL (GET Request)
    @GetMapping("/url/xml")
    public ResponseEntity<Resource> scrapeUrlXml(@RequestParam String url) throws IOException {
        // Scrape data
        Map<String, Object> scrapedData = jsoupService.scrapeFromUrl(url);

        // Create a .xml file with scraped data
        File tempFile = createXmlFile(scrapedData);

        // Prepare file as Resource for downloading
        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

        // Set the file name and content type for download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);
    }

    // 3. Generate .txt file from HTML file (POST Request)
    @PostMapping("/file/txt")
    public ResponseEntity<Resource> scrapeFileTxt(@RequestParam MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload-", ".html");
        file.transferTo(tempFile);

        // Scrape data from HTML file
        Map<String, Object> scrapedData = jsoupService.scrapeFromHtmlFile(tempFile);

        // Create a .txt file with scraped data
        File outputFile = createTxtFile(scrapedData);

        // Prepare file as Resource for downloading
        InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));

        // Set the file name and content type for download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    // 4. Generate .xml file from HTML file (POST Request)
    @PostMapping("/file/xml")
    public ResponseEntity<Resource> scrapeFileXml(@RequestParam MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload-", ".html");
        file.transferTo(tempFile);

        // Scrape data from HTML file
        Map<String, Object> scrapedData = jsoupService.scrapeFromHtmlFile(tempFile);

        // Create a .xml file with scraped data
        File outputFile = createXmlFile(scrapedData);

        // Prepare file as Resource for downloading
        InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));

        // Set the file name and content type for download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);
    }

    // Utility method to create a .txt file from scraped data
    private File createTxtFile(Map<String, Object> scrapedData) throws IOException {
        File file = new File("scrapedData.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Object> entry : scrapedData.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue().toString());
                writer.newLine();
            }
        }
        return file;
    }

    // Utility method to create an .xml file from scraped data
    private File createXmlFile(Map<String, Object> scrapedData) throws IOException {
        File file = new File("scrapedData.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<scrapedData>\n");
            for (Map.Entry<String, Object> entry : scrapedData.entrySet()) {
                writer.write("\t<" + entry.getKey() + ">" + entry.getValue().toString() + "</" + entry.getKey() + ">\n");
            }
            writer.write("</scrapedData>");
        }
        return file;
    }
}
