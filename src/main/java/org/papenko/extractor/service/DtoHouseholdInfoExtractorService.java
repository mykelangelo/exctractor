package org.papenko.extractor.service;

import org.papenko.extractor.dto.FullAddress;
import org.papenko.extractor.dto.HouseholdInfo;
import org.papenko.extractor.dto.Person;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DtoHouseholdInfoExtractorService implements HouseholdInfoExtractorService<List<HouseholdInfo>>{
    /**
     * @param people input data in {@link Person} records
     * @return List of {@link HouseholdInfo} sorted by Last Name then First Name where the occupant(s) is 18 or older
     */
    public List<HouseholdInfo> prepareResult(List<Person> people) {
        Map<FullAddress, List<Person>> households = people.stream()
                .collect(Collectors.groupingBy(Person::fullAddress));
        return households.entrySet().stream()
                .map(this::prepareHousehold)
                .collect(Collectors.toList());
    }

    private HouseholdInfo prepareHousehold(Map.Entry<FullAddress, List<Person>> entry) {
        FullAddress fullAddress = entry.getKey();
        List<Person> people = entry.getValue();
        return new HouseholdInfo(people.size(), new FullAddress(fullAddress.address(), fullAddress.city(), fullAddress.state()), prepareAdults(people));
    }

    private List<Person> prepareAdults(List<Person> people) {
        return people.stream()
                .filter(person -> person.age() >= 18)
                .sorted(Comparator.comparing(Person::lastName).thenComparing(Person::firstName))
                .collect(Collectors.toList());
    }
}
