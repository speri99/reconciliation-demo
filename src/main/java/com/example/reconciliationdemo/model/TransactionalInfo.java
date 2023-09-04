package com.example.reconciliationdemo.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionalInfo extends CsvToBean {
    @CsvBindByName(column = "Account Number")
    private String accountNumber;
    @CsvBindByName(column = "Transaction Id")
    private String transactionId;
    @CsvBindByName(column = "Amount")
    private double transactionAmount;
    private String source;
}
