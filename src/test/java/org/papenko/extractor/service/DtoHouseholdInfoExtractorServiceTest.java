package org.papenko.extractor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.papenko.extractor.dto.FullAddress;
import org.papenko.extractor.dto.HouseholdInfo;
import org.papenko.extractor.dto.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DtoHouseholdInfoExtractorServiceTest {
    @Spy
    private DtoHouseholdInfoExtractorService dtoHouseholdInfoExtractorService;

    @Test
    void prepareResult_shouldReturnEmptyList_whenProvidedWithEmptyList() {
        assertEquals(List.of(), dtoHouseholdInfoExtractorService.prepareResult(List.of()));
    }

    @Test
    void prepareResult_shouldReturnValidListDisplayingOnlyAdults_whenProvidedWithValidList() {
        Person personA = new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43);
        Person personB = new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45);
        Person personC = new Person("Bob", "Williams", new FullAddress("234 2ND AVE", "TACOMA", "WA"), 26);
        Person personD = new Person("Carol", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 67);
        Person personE = new Person("Caroline", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 17);
        HouseholdInfo household1 = new HouseholdInfo(1, personC.fullAddress(), List.of(personC));
        HouseholdInfo household2 = new HouseholdInfo(2, personD.fullAddress(), List.of(personD));
        HouseholdInfo household3 = new HouseholdInfo(2, personA.fullAddress(), List.of(personB, personA));
        assertEquals(List.of(household1, household2, household3), dtoHouseholdInfoExtractorService.prepareResult(List.of(personA, personB, personC, personD, personE)));
    }
    @Test
    void prepareResult_shouldReturnValidListWithSortedValues_whenProvidedWithValidList() {
        Person personA = new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43);
        Person personB = new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45);
        Person personD = new Person("Carol", "Aloha", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 18);
        HouseholdInfo household1 = new HouseholdInfo(3, personA.fullAddress(), List.of(personD, personB, personA));
        assertEquals(List.of(household1), dtoHouseholdInfoExtractorService.prepareResult(List.of(personA, personB, personD)));
    }
}