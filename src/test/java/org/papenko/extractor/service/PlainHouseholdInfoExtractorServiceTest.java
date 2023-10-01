package org.papenko.extractor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.papenko.extractor.dto.FullAddress;
import org.papenko.extractor.dto.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PlainHouseholdInfoExtractorServiceTest {
    @Spy
    private PlainHouseholdInfoExtractorService plainHouseholdInfoExtractorService;

    @Test
    void prepareResult_shouldReturnEmptyString_whenProvidedWithEmptyList() {
        assertEquals("", plainHouseholdInfoExtractorService.prepareResult(List.of()));
    }

    @Test
    void prepareResult_shouldReturnValidStringDisplayingOnlyAdults_whenProvidedWithValidList() {
        List<Person> source = List.of(
                new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43),
                new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45),
                new Person("Bob", "Williams", new FullAddress("234 2ND AVE", "TACOMA", "WA"), 26),
                new Person("Carol", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 67),
                new Person("Caroline", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 17));
        String expected = """
                1 people live in household @ 234 2ND AVE, TACOMA, WA
                Adults:
                Bob Williams, age 26
                                
                2 people live in household @ 234 2ND AVE, SEATTLE, WA
                Adults:
                Carol Johnson, age 67
                                
                2 people live in household @ 123 MAIN ST, SEATTLE, WA
                Adults:
                Alice Smith, age 45
                Dave Smith, age 43""";
        assertEquals(expected, plainHouseholdInfoExtractorService.prepareResult(source));
    }

    @Test
    void prepareResult_shouldReturnValidStringWithSortedValues_whenProvidedWithValidList() {
        List<Person> source = List.of(
        new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43),
        new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45),
        new Person("Carol", "Aloha", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 18));
        String expected = """
                3 people live in household @ 123 MAIN ST, SEATTLE, WA
                Adults:
                Carol Aloha, age 18
                Alice Smith, age 45
                Dave Smith, age 43""";
        assertEquals(expected, plainHouseholdInfoExtractorService.prepareResult(source));
    }
}