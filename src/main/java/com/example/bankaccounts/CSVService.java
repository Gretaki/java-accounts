package com.example.bankaccounts;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVService {

    private List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    public String readAllExample(MultipartFile file) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(
                ClassLoader.getSystemResource("blaa.csv").toURI()));
        return this.readAll(reader).toString();
    }

    List<Transaction> parse(MultipartFile file) throws Exception {
        final var transaction = new Transaction();

        transaction.setComment(this.readAllExample(file));

        return List.of(transaction);
    }

    MultipartFile loadAll(Date dateFrom, Date dateTo) {
        throw new RuntimeException();
    }

    MultipartFile load(long accountNumber, Date dateFrom, Date dateTo) {
        throw new RuntimeException();
    }
}
