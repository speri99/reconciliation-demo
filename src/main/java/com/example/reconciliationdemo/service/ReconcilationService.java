package com.example.reconciliationdemo.service;

import com.example.reconciliationdemo.model.TransactionalInfo;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReconcilationService {

    public List<TransactionalInfo> reconcileTransactionalData(MultipartFile sourceFile, MultipartFile targetFile) {

        List<TransactionalInfo> sourceList = convertCSVtoBean(sourceFile);
        List<TransactionalInfo> destinationList = convertCSVtoBean(targetFile);

        List<TransactionalInfo> finalList = new ArrayList<>();
        List<TransactionalInfo> reconciledListForIraq = reconcileSourceAndDestination(sourceList, destinationList,"Iraq");
        List<TransactionalInfo> reconciledListFromMaster = reconcileSourceAndDestination(destinationList, sourceList,"MasterCard");
        finalList.addAll(reconciledListFromMaster);
        finalList.addAll(reconciledListForIraq);

        return finalList;
    }

    private List<TransactionalInfo> reconcileSourceAndDestination(List<TransactionalInfo> sourceList, List<TransactionalInfo> destinationList,String source) {
        List<TransactionalInfo> finalList = new ArrayList<>();
        for (TransactionalInfo transactionalInfo : sourceList) {
            boolean matched = false;
            for (TransactionalInfo transactionalInfo1 : destinationList) {
                if (transactionalInfo.getTransactionId().equalsIgnoreCase(transactionalInfo1.getTransactionId())
                        && transactionalInfo.getAccountNumber().equalsIgnoreCase(transactionalInfo1.getAccountNumber())
                        && transactionalInfo.getTransactionAmount() == transactionalInfo1.getTransactionAmount()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                transactionalInfo.setSource(source);
                finalList.add(transactionalInfo);
            }
        }
        return finalList;
    }

    private List<TransactionalInfo> convertCSVtoBean(MultipartFile sourceFile) {
        List<TransactionalInfo> transactionalInfos = null;
        try (Reader reader = new BufferedReader(new InputStreamReader(sourceFile.getInputStream(), StandardCharsets.UTF_8))) {
            CsvToBean<TransactionalInfo> csvToBean = new CsvToBeanBuilder<TransactionalInfo>(reader)
                    .withType(TransactionalInfo.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            transactionalInfos = csvToBean.parse();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return transactionalInfos;
    }

}
