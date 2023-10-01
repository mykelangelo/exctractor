package org.papenko.extractor.controller;

import org.papenko.extractor.constant.OutputFormat;
import org.papenko.extractor.dto.Person;
import org.papenko.extractor.exception.CsvValidationException;
import org.papenko.extractor.service.*;
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
    private final PlainHouseholdInfoExtractorService plainHouseholdInfoExtractorService;
    private final PersonExtractorService personExtractorService;
    private final FileReaderService fileReaderService;
    private final ValidatorService validatorService;
    private final DtoHouseholdInfoExtractorService dtoHouseholdInfoExtractorService;
    private final CsvHouseholdInfoExtractorService csvHouseholdInfoExtractorService;

    public ExtractorController(PlainHouseholdInfoExtractorService plainHouseholdInfoExtractorService, PersonExtractorService personExtractorService, FileReaderService fileReaderService, ValidatorService validatorService, DtoHouseholdInfoExtractorService dtoHouseholdInfoExtractorService, CsvHouseholdInfoExtractorService csvHouseholdInfoExtractorService) {
        this.plainHouseholdInfoExtractorService = plainHouseholdInfoExtractorService;
        this.personExtractorService = personExtractorService;
        this.fileReaderService = fileReaderService;
        this.validatorService = validatorService;
        this.dtoHouseholdInfoExtractorService = dtoHouseholdInfoExtractorService;
        this.csvHouseholdInfoExtractorService = csvHouseholdInfoExtractorService;
    }

    @PostMapping("file")
    public ResponseEntity<Object> extractFromFile(@RequestParam MultipartFile file,
                                                  @RequestParam(defaultValue = "PLAIN") OutputFormat outputFormat) {
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
        HouseholdInfoExtractorService<?> householdInfoExtractorService = switch (outputFormat) {
            case PLAIN -> plainHouseholdInfoExtractorService;
            case CSV -> csvHouseholdInfoExtractorService;
            case JSON -> dtoHouseholdInfoExtractorService;
        };
        Object households = householdInfoExtractorService.prepareResult(rows);
        return ResponseEntity.ok(households);
    }
}
