package org.papenko.extractor.controller;

import org.papenko.extractor.entity.Person;
import org.papenko.extractor.exception.CsvValidationException;
import org.papenko.extractor.service.FileReaderService;
import org.papenko.extractor.service.HouseholdInfoExtractorService;
import org.papenko.extractor.service.PersonExtractorService;
import org.papenko.extractor.service.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("extract")
public class ExtractorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorController.class);
    private final HouseholdInfoExtractorService householdInfoExtractorService;
    private final PersonExtractorService personExtractorService;
    private final FileReaderService fileReaderService;
    private final ValidatorService validatorService;

    public ExtractorController(HouseholdInfoExtractorService householdInfoExtractorService, PersonExtractorService personExtractorService, FileReaderService fileReaderService, ValidatorService validatorService) {
        this.householdInfoExtractorService = householdInfoExtractorService;
        this.personExtractorService = personExtractorService;
        this.fileReaderService = fileReaderService;
        this.validatorService = validatorService;
    }

    @PostMapping("file")
    public ResponseEntity<String> extractFromFile(@RequestParam MultipartFile file) {
        String lines;
        try {
            lines = fileReaderService.readLines(file);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
            return ResponseEntity.internalServerError().body("IOException: " + e.getMessage());
        }
        try {
            validatorService.validate(lines);
        } catch (CsvValidationException e) {
            LOGGER.error("CsvValidationException", e);
            return ResponseEntity.badRequest().body("CsvValidationException: " + e.getMessage());
        }
        List<Person> rows = personExtractorService.extractRows(lines);
        String result = householdInfoExtractorService.prepareResult(rows);
        return ResponseEntity.ok(result);
    }
}
