package org.papenko.extractor.service;

import org.papenko.extractor.dto.Person;

import java.util.List;

public interface HouseholdInfoExtractorService<T> {
    T prepareResult(List<Person> people);
}
