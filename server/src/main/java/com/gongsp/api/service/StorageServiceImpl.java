package com.gongsp.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service("storageService")
public class StorageServiceImpl implements StorageService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder");
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR: File is empty.");
            }
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }

            try (InputStream inputStream = file.getInputStream()) {
                // 파일명 충돌방지
                UUID uuid = UUID.randomUUID();
                String uuidFilename = uuid + "_" + file.getOriginalFilename();
                Files.copy(inputStream, root.resolve(uuidFilename), StandardCopyOption.REPLACE_EXISTING);
                return uuidFilename;
            }
            }  catch (Exception e) {
            throw new RuntimeException("Could not store file. ERROR: " + e.getMessage());
        }

    }

    @Override
    public Stream<Path> loadAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path load(String filename) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String uuidFilename) {
        Path root = Paths.get(uploadPath);
        try {
            Files.deleteIfExists(root.resolve(uuidFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
