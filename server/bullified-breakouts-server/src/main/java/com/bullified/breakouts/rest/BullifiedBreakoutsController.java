package com.bullified.breakouts.rest;

import com.bullified.breakouts.domain.BullifiedBreakoutsResponse;
import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import com.bullified.breakouts.service.exception.FileRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/rest/1.0")
public class BullifiedBreakoutsController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BullifiedBreakoutsController.class);

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
            response.setStatus("Failure");
            response.setMessage(e.getMessage());
            return response;
        }
        response.setStatus("Success");
        response.setMessage("File Uploaded Successfully");
        return response;
    }

    @GetMapping(value = "/download-images", produces = "application/zip")
    @ResponseBody
    public byte[] downloadImages(HttpServletResponse response) throws FileRetrievalException {
        response.addHeader("Content-Disposition", "attachment; filename=\"images.zip\"");
        try {
            return storageService.loadFiles();
        } catch (FileRetrievalException e) {
            LOGGER.error("Error when attempting to load files", e);
            return null;
        }
    }

}
