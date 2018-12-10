package com.bullified.breakouts.service.impl;

import com.bullified.breakouts.domain.ImageMetaData;
import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.apache.commons.io.IOUtils;
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
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
    private static final String ALL_FILES = "**";
    private static final String FILE_PREFIX = "file:";

    @Value("${storage.directory.windows}")
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
    public byte[] zipFiles() throws FileRetrievalException {
        Resource[] resources = getFilesAsResource(storageLocation + ALL_FILES);
        try {
            return zipFIles(resources);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileRetrievalException("Error zipping files", e);
        }
    }

    @Override
    public byte[] loadFile(String filePattern) throws FileRetrievalException {
        Resource[] resources = getFilesAsResource(storageLocation + filePattern);
        if (resources.length != 1) {
            String error = "Could not load image with pattern " + filePattern;
            LOGGER.error(error);
            throw new FileRetrievalException(error);
        }
        Resource resource = resources[0];

        try {
            if (!resource.getFile().exists()) {
                throw new FileRetrievalException("File " + resource.getFile().getName() + " doesn't exist");
            }
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            String error = "Error converting resource to byte array";
            LOGGER.error(error);
            throw new FileRetrievalException(error);
        }
    }

    @Override
    public Set<ImageMetaData> getImageLocations() throws FileRetrievalException {
        Resource[] resources = getFilesAsResource(storageLocation + ALL_FILES);
        Set<ImageMetaData> imagesData = new HashSet<>();
        for (Resource resource : resources) {
            ImageMetaData metaData = new ImageMetaData();
            try {
                metaData.setName(resource.getFilename());
                metaData.setUrl(resource.getFile().getAbsolutePath());
                imagesData.add(metaData);
            } catch (IOException e) {
                String errorMessage = "Couldn't find file location";
                LOGGER.error(errorMessage);
                throw new FileRetrievalException(errorMessage);
            }
        }
        return imagesData;
    }

    private Resource[] getFilesAsResource(String filePattern) throws FileRetrievalException {
        Resource[] resources;
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
        return resources;
    }

    private byte[] zipFIles(Resource[] resources) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = null;
        zipOutputStream = new ZipOutputStream(bufferedOutputStream);
        for (Resource resource : resources) {
            File fileToZip = resource.getFile();
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream inputStream = new FileInputStream(fileToZip);
            IOUtils.copy(inputStream, zipOutputStream);
            inputStream.close();
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            zipOutputStream.flush();
            zipOutputStream.close();
            bufferedOutputStream.close();
            byteArrayOutputStream.close();
        }
        return byteArrayOutputStream.toByteArray();
    }

}
