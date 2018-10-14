package com.bullified.breakouts.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/rest/1.0")
public class BullifiedBreakoutsController {

    @GetMapping("/health-check")
    @ResponseBody
    public String healthCheck() {
        return "\"status\":\"success\"";
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public String uploadFile(@RequestParam("fileName")MultipartFile file) {
        return "\"status\":\"success\"";
    }
}
