package com.bullified.breakouts.service;

import com.bullified.breakouts.domain.ImageMetaData;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface StorageService {

    void store(MultipartFile file) throws FileCreationException;

    byte[] zipFiles() throws FileRetrievalException;

    byte[] loadFile(String filePattern) throws FileRetrievalException;

    Set<ImageMetaData> getImageLocations() throws FileRetrievalException;
}
