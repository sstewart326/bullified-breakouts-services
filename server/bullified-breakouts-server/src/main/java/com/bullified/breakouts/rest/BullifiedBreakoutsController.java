package com.bullified.breakouts.rest;

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
    public String uploadFile(@RequestParam("fileName")MultipartFile file) {
        try {
            storageService.store(file);
        } catch (FileCreationException e) {
            return "\"status\":\"failure\"";
        }
        return "\"status\":\"success\"";
    }
}
