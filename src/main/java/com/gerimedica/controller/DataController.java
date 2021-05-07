package com.gerimedica.controller;

import com.gerimedica.helper.CsvHelper;
import com.gerimedica.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) {
        String responseMessage;

        if (CsvHelper.hasCSVFormat(file)) {
            dataService.uploadData(file);

            responseMessage = "File " + file.getOriginalFilename() + " uploaded";
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        }

        responseMessage = "Invalid file format!";
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> getAllData() {
        InputStreamResource file = new InputStreamResource(dataService.getAllData());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/csv")).body(file);
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> getDataByCode(@PathVariable String code) {
        InputStreamResource file = new InputStreamResource(dataService.getDataByCode(code));

        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/csv")).body(file);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteAllData() {
        dataService.deleteAllData();

        return ResponseEntity.ok().body("All data was deleted.");
    }
}
