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
class CsvHouseholdInfoExtractorServiceTest {
    @Spy
    private CsvHouseholdInfoExtractorService csvHouseholdInfoExtractorService;

    @Test
    void prepareResult_shouldReturnEmptyString_whenProvidedWithEmptyList() {
        assertEquals("", csvHouseholdInfoExtractorService.prepareResult(List.of()));
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
                1, 234 2ND AVE, TACOMA, WA
                Bob, Williams, 26
                                
                2, 234 2ND AVE, SEATTLE, WA
                Carol, Johnson, 67
                                
                2, 123 MAIN ST, SEATTLE, WA
                Alice, Smith, 45
                Dave, Smith, 43""";
        assertEquals(expected, csvHouseholdInfoExtractorService.prepareResult(source));
    }

    @Test
    void prepareResult_shouldReturnValidStringWithSortedValues_whenProvidedWithValidList() {
        List<Person> source = List.of(
                new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43),
                new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45),
                new Person("Carol", "Aloha", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 18));
        String expected = """
                3, 123 MAIN ST, SEATTLE, WA
                Carol, Aloha, 18
                Alice, Smith, 45
                Dave, Smith, 43""";
        assertEquals(expected, csvHouseholdInfoExtractorService.prepareResult(source));
    }
}