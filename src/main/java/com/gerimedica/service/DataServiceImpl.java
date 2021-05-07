package com.gerimedica.service;

import com.gerimedica.domain.Data;
import com.gerimedica.repository.DataRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void uploadData(MultipartFile file) {
        List<Data> dataList = convertCsvToData(file);

        dataRepository.saveAll(dataList);
    }

    @Override
    public ByteArrayInputStream getAllData() {
        List<Data> dataList = dataRepository.findAll();

        return convertDataToCsv(dataList);
    }

    @Override
    public ByteArrayInputStream getDataByCode(String code) {
        Optional<Data> data = dataRepository.findById(code);

        return data.map(dataOutput -> convertDataToCsv(Collections.singletonList(dataOutput))).orElse(null);
    }

    @Override
    public void deleteAllData() {
        dataRepository.deleteAll();
    }

    private List<Data> convertCsvToData(MultipartFile file) {
        InputStream inputStream = getInputStream(file);

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                                                                                     StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(bufferedReader,
                                                CSVFormat
                                                        .DEFAULT
                                                        .withFirstRecordAsHeader()
                                                        .withIgnoreHeaderCase()
                                                        .withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            return parseDataList(csvRecords);
        } catch (IOException exception) {
            throw new RuntimeException("Cannot save CSV: " + exception.getMessage());
        }
    }

    private InputStream getInputStream(MultipartFile file) {
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (IOException exception) {
            throw new RuntimeException("Cannot save CSV: " + exception.getMessage());
        }

        return inputStream;
    }

    private List<Data> parseDataList(Iterable<CSVRecord> csvRecords) {
        List<Data> dataList = new ArrayList<>();

        try {
            for (CSVRecord csvRecord : csvRecords) {
                String code = csvRecord.get("code");
                String source = csvRecord.get("source");
                String codeListCode = csvRecord.get("codeListCode");
                String displayValue = csvRecord.get("displayValue");
                String longDescription = csvRecord.get("longDescription");
                String fromDate = csvRecord.get("fromDate");
                String toDate = csvRecord.get("toDate");
                String sortingPriority = csvRecord.get("sortingPriority");
                Date fromDateParsed = null;
                Date toDateParsed = null;
                Integer sortingPriorityParsed = null;

                if (longDescription.isEmpty()) {
                    throw new RuntimeException("A long description needs to be provided.");
                }

                if (!fromDate.isEmpty()) {
                    fromDateParsed = new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
                }

                if (!toDate.isEmpty()) {
                    toDateParsed = new SimpleDateFormat("dd-MM-yyyy").parse(toDate);
                }

                if (!sortingPriority.isEmpty()) {
                    sortingPriorityParsed = Integer.parseInt(sortingPriority);
                }

                Data data = new Data(code,
                                     source,
                                     codeListCode,
                                     displayValue,
                                     longDescription,
                                     fromDateParsed,
                                     toDateParsed,
                                     sortingPriorityParsed);

                dataList.add(data);
            }
        } catch (ParseException exception) {
            throw new RuntimeException("Date in wrong format. Use dd-MM-yyyy: " + exception.getMessage());
        }

        return dataList;
    }

    private ByteArrayInputStream convertDataToCsv(List<Data> dataList) {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(byteArrayOutputStream), csvFormat);

            for (Data data : dataList) {
                String code = data.getCode();
                String source = data.getSource() == null ? null : data.getSource();
                String codeListCode = data.getCodeListCode() == null ? null : data.getCodeListCode();
                String displayValue = data.getDisplayValue() == null ? null : data.getDisplayValue();
                String longDescription = data.getLongDescription();
                String fromDate = data.getFromDate() == null ? null : data.getFromDate().toString();
                String toDate = data.getToDate() == null ? null : data.getToDate().toString();
                String sortingPriority =
                        data.getSortingPriority() == null ? null : data.getSortingPriority().toString();

                List<String> dataOutput = Arrays.asList(code,
                                                        source,
                                                        codeListCode,
                                                        displayValue,
                                                        longDescription,
                                                        fromDate,
                                                        toDate,
                                                        sortingPriority);

                csvPrinter.printRecord(dataOutput);
            }

            csvPrinter.flush();

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException exception) {
            throw new RuntimeException("Cannot import data to a CSV: " + exception.getMessage());
        }
    }
}
