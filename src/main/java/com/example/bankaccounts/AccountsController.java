package com.example.bankaccounts;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AccountsController {

    @Autowired
    private TransactionRepository transactionRepository;

    private final CSVService CSVService = new CSVService();

    @PostMapping("/import-bank-statement")
    public String importBankStatement(@RequestParam("file") MultipartFile file) throws Exception {

        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(Transaction.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<Transaction> transactions = csvToBean.parse();

        reader.close();

        transactionRepository.saveAll(transactions);

        return "successful " + transactions.size();
    }

    @GetMapping(
            value = "/export-bank-statement",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    ResponseEntity<StringBuffer> exportBankStatement(
            @RequestParam(name = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<Date> dateFrom,
            @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<Date> dateTo) throws Exception {

        final var transactions = transactionRepository.findAll();

        List<Transaction> result = StreamSupport.stream(transactions.spliterator(), false)
                .sorted(Comparator.comparing(a -> a.operationDate))
                .filter(t -> {
                    System.out.println(dateFrom);
                    System.out.println("date to = " +dateTo);
                    if (dateFrom.isPresent() && dateTo.isPresent()) {
                        return t.operationDate.after(dateFrom.get()) && t.operationDate.before(dateTo.get());
                    } else return true;
                })
                .collect(Collectors.toList());

        var writer = new StringWriter();

        StatefulBeanToCsv<Transaction> csvBuilder = new StatefulBeanToCsvBuilder<Transaction>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        csvBuilder.write(result);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(writer.getBuffer().length())
                .body(writer.getBuffer());
    }

    @GetMapping("/get-account-balance")
    public double getAccountBalance(
            @RequestParam(name = "accountNumber") long accountNumber,
            @RequestParam(name = "dateFrom", required = false) Date dateFrom,
            @RequestParam(name = "dateTo", required = false) Date dateTo) {

        MultipartFile loadedFiles = CSVService.load(accountNumber, dateFrom, dateTo);

        double balance = calculateAccountBalance(loadedFiles);

        return balance;
    }

    private double calculateAccountBalance(MultipartFile loadedFiles) {
        throw new RuntimeException();
    }
}
