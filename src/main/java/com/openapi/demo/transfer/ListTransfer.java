package com.openapi.demo.transfer;

import com.openapi.demo.common.KisConstant;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListTransfer {

    private List<String> itemCodeList;

    public List<String> getItemCodeList() {

        if (itemCodeList == null) {
            csvExtractor();
        }

        return itemCodeList;
    }

    private void csvExtractor() {
        itemCodeList = new ArrayList<>();

        try (Reader in = new InputStreamReader(Files.newInputStream(Paths.get(KisConstant.CSV_PATH)), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

            int batchSize = 0;
            for (CSVRecord record : records) {
                if (batchSize == 35) break;
                String value = record.get(1);
                itemCodeList.add(value);
                batchSize++;
            }

        } catch (Exception e) {
            System.out.println("csv error : " + e.getMessage());
        }
    }
}
