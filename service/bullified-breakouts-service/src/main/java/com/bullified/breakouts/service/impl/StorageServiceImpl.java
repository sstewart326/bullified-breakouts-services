package com.bullified.breakouts.service.impl;

import com.bullified.breakouts.domain.GetImageLocationsResponse;
import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
    private static final String ALL_FILES = "**";
    private static final String FILE_PREFIX = "file:";

    @Value("${storage.directory}")
    private String storageLocation;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void store(MultipartFile file) throws FileCreationException {
        try {
            file.transferTo(new File(storageLocation + file.getOriginalFilename()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileCreationException("Error creating file to path " + storageLocation, e);
        }
    }

    @Override
    public byte[] loadFiles() throws FileRetrievalException {
        Resource[] resources;
        String filePattern = storageLocation + ALL_FILES;
        try {
            resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(FILE_PREFIX + filePattern);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileRetrievalException("Error loading files with pattern: " + filePattern, e);
        }
        if (resources.length == 0) {
            String errorMessage = "No files to retrieve that matches pattern: " + filePattern;
            LOGGER.error(errorMessage);
            throw new FileRetrievalException(errorMessage);
        }
        try {
            return zipFIles(resources);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileRetrievalException("Error zipping files", e);
        }
    }

    @Override
    public GetImageLocationsResponse getImageLocations() throws FileRetrievalException {
        return null;
    }

    private byte[] zipFIles(Resource[] resources) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
        for (Resource resource : resources) {
            File fileToZip = resource.getFile();
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream inputStream = new FileInputStream(fileToZip);
            IOUtils.copy(inputStream, zipOutputStream);
            inputStream.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

}
