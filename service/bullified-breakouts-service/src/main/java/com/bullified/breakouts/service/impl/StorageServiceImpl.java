package com.bullified.breakouts.service.impl;

import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Value("${storage.directory}")
    private String storageLocation;

    @Override
    public void store(MultipartFile file) throws FileCreationException {
        try {
            file.transferTo(new File(storageLocation));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileCreationException("Error creating file to path " + storageLocation, e);
        }
    }

}
