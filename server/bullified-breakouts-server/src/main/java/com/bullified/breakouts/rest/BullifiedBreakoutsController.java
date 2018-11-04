package com.bullified.breakouts.rest;

import com.bullified.breakouts.domain.UploadFileResponse;
import com.bullified.breakouts.service.StorageService;
import com.bullified.breakouts.service.exception.FileCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/rest/1.0")
public class BullifiedBreakoutsController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/health-check")
    @ResponseBody
    public String healthCheck() {
        return "\"status\":\"success\"";
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public UploadFileResponse uploadFile(@RequestParam("fileName")MultipartFile file) {
        UploadFileResponse response = new UploadFileResponse();
        try {
            storageService.store(file);
        } catch (FileCreationException e) {
            response.setStatus("Failure");
            response.setMessage(e.getMessage());
            return response;
        }
        response.setStatus("Success");
        response.setMessage("File Uploaded Successfully");
        return response;
    }
}
