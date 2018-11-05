package com.bullified.breakouts.service;

import com.bullified.breakouts.domain.GetImageLocationsResponse;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file) throws FileCreationException;

    byte[] loadFiles() throws FileRetrievalException;

    GetImageLocationsResponse getImageLocations() throws FileRetrievalException;
}
