package com.bullified.breakouts.service;

import com.bullified.breakouts.service.exception.FileCreationException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file) throws FileCreationException;
}
