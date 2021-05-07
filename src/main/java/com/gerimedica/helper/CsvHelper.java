package com.gerimedica.helper;

import org.springframework.web.multipart.MultipartFile;

public class CsvHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
}
