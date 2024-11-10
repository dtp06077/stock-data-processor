package com.openapi.demo.transfer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ListTransfer {

    private static List<String> itemCodeList;

    public static List<String> getItemCodeList() {

        if (itemCodeList == null) {
            csvExtractor();
        }

        return itemCodeList;
    }


    private static void csvExtractor() {
        itemCodeList = new ArrayList<>();
        String csvFilePath = "src/main/resources/data.csv";

        try (Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvFilePath)), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

            for (CSVRecord record : records) {
                String value = record.get(1);
                if (value.length() == 6) {
                    itemCodeList.add(value);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
