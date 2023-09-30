package org.papenko.extractor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.papenko.extractor.entity.FullAddress;
import org.papenko.extractor.entity.Person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HouseholdInfoExtractorServiceTest {
    @Spy
    private HouseholdInfoExtractorService householdInfoExtractorService;

    @Test
    void prepareResult_shouldReturnEmptyString_whenProvidedWithEmptyList() {
        assertEquals("", householdInfoExtractorService.prepareResult(List.of()));
    }

    @Test
    void prepareResult_shouldReturnValidString_whenProvidedWithValidList() {
        List<Person> source = new ArrayList<>();
        source.add(new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43));
        source.add(new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45));
        source.add(new Person("Bob", "Williams", new FullAddress("234 2ND AVE", "TACOMA", "WA"), 26));
        source.add(new Person("Carol", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 67));
        String expected = """
                1 people live in household @ 234 2ND AVE, TACOMA, WA
                Adults:
                Bob Williams, age 26
                                
                1 people live in household @ 234 2ND AVE, SEATTLE, WA
                Adults:
                Carol Johnson, age 67
                                
                2 people live in household @ 123 MAIN ST, SEATTLE, WA
                Adults:
                Alice Smith, age 45
                Dave Smith, age 43""";
        assertEquals(expected, householdInfoExtractorService.prepareResult(source));
    }
}