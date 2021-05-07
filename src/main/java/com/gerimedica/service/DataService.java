package com.gerimedica.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface DataService {
    void uploadData(MultipartFile file);

    ByteArrayInputStream getAllData();

    ByteArrayInputStream getDataByCode(String code);

    void deleteAllData();
}
