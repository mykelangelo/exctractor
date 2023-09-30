package org.papenko.extractor.service;

import org.papenko.extractor.entity.FullAddress;
import org.papenko.extractor.entity.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.papenko.extractor.constant.Csv.INVALID_ADDRESS_SYMBOLS_PATTERN;
import static org.papenko.extractor.constant.Csv.SEPARATOR_PATTERN;

@Service
public class PersonExtractorService {

    /**
     * @param multiline input data
     * @return normalized data in the java.util.List of {@link Person} records
     * @implNote for normalization, all address elements need to be capitalized
     * and stripped of punctuation or leading and trailing whitespaces, plus consecutive spaces are replaced by one space
     */
    public List<Person> extractRows(String multiline) {
        return multiline.lines()
                .filter(e -> !e.isBlank())
                .map(this::extractPerson)
                .collect(Collectors.toList());
    }

    private Person extractPerson(String row) {
        String trimmed = row.substring(1, row.length() - 1);
        String[] columns = trimmed.split(SEPARATOR_PATTERN);

        return new Person(columns[0], columns[1], normalizeAddress(columns[2], columns[3], columns[4]), Integer.valueOf(columns[5]));
    }

    private FullAddress normalizeAddress(String address, String city, String state) {
        return new FullAddress(address.toUpperCase().replaceAll(INVALID_ADDRESS_SYMBOLS_PATTERN, "").replaceAll("\\s+", " ").strip(),
                city.toUpperCase(), state.toUpperCase());
    }
}
