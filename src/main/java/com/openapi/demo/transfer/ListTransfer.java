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
        String csvFilePath = "src/main/resources/data/stock_data.csv";

        try (Reader in = new InputStreamReader(Files.newInputStream(Paths.get(csvFilePath)), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

            int count = 0;

            for (CSVRecord record : records) {

                if(count==35) break;

                String value = record.get(1);
                itemCodeList.add(value);
                count++;
            }

        } catch (Exception e) {
            System.out.println("csv error : " + e.getMessage());
        }
    }
}
