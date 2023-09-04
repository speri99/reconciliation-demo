package com.example.reconciliationdemo.controller;

import com.example.reconciliationdemo.model.TransactionalInfo;
import com.example.reconciliationdemo.service.ReconcilationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin(allowedHeaders = "*")
public class ReconcilationController {

    @Autowired
    private ReconcilationService  reconcilationService;

    @PostMapping("/reconcile")
    public ResponseEntity<Object> reconcileData(@RequestParam("sourceFile")MultipartFile  sourceFile,@RequestParam("targetFile") MultipartFile targetFile) throws IOException {

        List<TransactionalInfo> transactionalInfoList=reconcilationService.reconcileTransactionalData(sourceFile,targetFile);
        return new ResponseEntity<>(transactionalInfoList,HttpStatus.OK);
    }

}
