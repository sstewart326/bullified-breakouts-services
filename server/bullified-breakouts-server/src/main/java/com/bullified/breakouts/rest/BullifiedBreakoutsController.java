package com.bullified.breakouts.rest;

import com.bullified.breakouts.domain.BullifiedBreakoutsResponse;
import com.bullified.breakouts.domain.GetImageLocationsResponse;
import com.bullified.breakouts.domain.ImageMetaData;
import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Controller
@RequestMapping("/rest/1.0")
public class BullifiedBreakoutsController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BullifiedBreakoutsController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Autowired
    private StorageService storageService;

    @GetMapping("/health-check")
    @ResponseBody
    public String healthCheck() {
        return "\"status\":\"success\"";
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public BullifiedBreakoutsResponse uploadFile(@RequestParam("fileName") MultipartFile file) {
        BullifiedBreakoutsResponse response = new BullifiedBreakoutsResponse();
        try {
            storageService.store(file);
        } catch (FileCreationException e) {
            LOGGER.error("Error when trying to store file with name " + file.getOriginalFilename(), e);
            response.setStatus(FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setStatus(SUCCESS);
        response.setMessage("File Uploaded Successfully");
        return response;
    }

    @GetMapping(value = "/zip-images", produces = "application/zip")
    @ResponseBody
    public byte[] downloadImagesZip(HttpServletResponse response) throws FileRetrievalException {
        response.addHeader("Content-Disposition", "attachment; filename=\"images.zip\"");
        try {
            return storageService.zipFiles();
        } catch (FileRetrievalException e) {
            LOGGER.error("Error when attempting to load files", e);
            throw e;
        }
    }

    @GetMapping("/get-image-locations")
    @ResponseBody
    public GetImageLocationsResponse getImageLocations() {
        GetImageLocationsResponse response = new GetImageLocationsResponse();
        try {
            Set<ImageMetaData> imagesMetaData = storageService.getImageLocations();
            response.setStatus(SUCCESS);
            response.setMessage("Successfully retrieved image meta data");
            response.setImageSet(imagesMetaData);
            return response;
        } catch (FileRetrievalException e) {
            LOGGER.error("Error when attempting to load files", e);
            response.setMessage(e.getMessage());
            response.setStatus(FAILURE);
            return response;
        }
    }

    @GetMapping(value = "/download-file", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] downloadFile(@RequestParam("fileName") String fileName) throws FileRetrievalException {
        try {
            return storageService.loadFile(fileName);
        } catch (FileRetrievalException e) {
            LOGGER.error("Error when attempting to load file name " + fileName, e);
            throw e;
        }
    }

}
