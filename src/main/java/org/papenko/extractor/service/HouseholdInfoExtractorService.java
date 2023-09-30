package org.papenko.extractor.service;

import org.papenko.extractor.entity.FullAddress;
import org.papenko.extractor.entity.Person;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HouseholdInfoExtractorService {
    /**
     * @param people input data in {@link Person} records
     * @return String with Household info in one line (number of occupants and address) and in next lines:
     * Each First Name, Last Name and Age sorted by Last Name then First Name where the occupant(s) is 18 or older
     */
    public String prepareResult(List<Person> people) {
        Map<FullAddress, List<Person>> households = people.stream()
                .collect(Collectors.groupingBy(Person::fullAddress));
        return households.entrySet().stream()
                .map(this::prepareBlock)
                .collect(Collectors.joining("\n\n"));
    }

    private String prepareBlock(Map.Entry<FullAddress, List<Person>> entry) {
        FullAddress fullAddress = entry.getKey();
        List<Person> people = entry.getValue();
        return MessageFormat.format("{0} people live in household @ {1}, {2}, {3}\nAdults:\n{4}", people.size(),
                fullAddress.address(), fullAddress.city(), fullAddress.state(), prepareAdultsInfo(people));
    }

    private String prepareAdultsInfo(List<Person> people) {
        return people.stream()
                .filter(person -> person.age() >= 18)
                .sorted(Comparator.comparing(Person::lastName).thenComparing(Person::firstName))
                .map(person -> MessageFormat.format("{0} {1}, age {2}", person.firstName(), person.lastName(), person.age()))
                .collect(Collectors.joining("\n"));
    }
}
