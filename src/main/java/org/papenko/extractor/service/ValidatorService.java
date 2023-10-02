package org.papenko.extractor.service;

import org.papenko.extractor.constant.Csv;
import org.papenko.extractor.constant.Messages;
import org.papenko.extractor.exception.CsvValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.papenko.extractor.constant.Csv.INVALID_ADDRESS_SYMBOLS_PATTERN;
import static org.papenko.extractor.constant.Csv.SEPARATOR_PATTERN;

@Service
public class ValidatorService {
    /**
     * @param multiline csv file's lines to be validated
     * @throws CsvValidationException when failed validation: all not blank lines after stripping leading and trailing spaces
     *                                should start and end with double quotes " and each such line
     *                                should have {@link Csv#VALID_SEPARATORS_COUNT} valid separators ({@link Csv#SEPARATOR_PATTERN})
     */
    public void validate(String multiline) throws CsvValidationException {
        String[] lines = multiline.split("\\r?\\n");
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String stripped = line.strip();
            if (!stripped.startsWith("\"") || !stripped.endsWith("\"")) {
                throw new CsvValidationException(Messages.NOT_SURROUNDED_BY_QUOTES);
            }
            if (countCsvSeparators(stripped) != Csv.VALID_SEPARATORS_COUNT) {
                throw new CsvValidationException(Messages.NOT_CONTAINING_CORRECT_NUMBER_OF_SEPARATORS);
            }
            String trimmed = stripped.substring(1, stripped.length() - 1);
            String[] columns = trimmed.split(SEPARATOR_PATTERN);
            for (String column : columns) {
                if (column.replaceAll(INVALID_ADDRESS_SYMBOLS_PATTERN, "").isBlank()) {
                    throw new CsvValidationException(Messages.SOME_FIELDS_ARE_BLANK);
                }
            }
        }
    }

    private int countCsvSeparators(String line) {
        Pattern pattern = Pattern.compile(Csv.SEPARATOR_PATTERN);

        // Create a Matcher object to search for the pattern in the input string
        Matcher matcher = pattern.matcher(line);

        int count = 0;

        // Use a while loop to find and count each occurrence of the pattern
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}