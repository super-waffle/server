package com.gongsp.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${spring.servlet.multipart.location}")
    private String basePath;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> showImage(@PathVariable String filename) throws IOException {

        Path fullPath = Paths.get(basePath + "/" + filename);
        System.out.println("filePath" + fullPath.toString());
        String contentType = Files.probeContentType(fullPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(filename, StandardCharsets.UTF_8)
                        .build()
        );
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(fullPath));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
