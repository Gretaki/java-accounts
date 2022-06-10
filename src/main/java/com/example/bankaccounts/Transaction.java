package com.example.bankaccounts;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Currency;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @CsvBindByName
    public Long id;
    @CsvBindByName
    public long accountNumber;
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @CsvBindByName
    public Date operationDate;
    @CsvBindByName
    public String beneficiary;
    @CsvBindByName
    public String comment;
    @CsvBindByName
    public double amount;
    @CsvBindByName
    public String currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
