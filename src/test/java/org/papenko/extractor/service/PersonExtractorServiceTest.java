package org.papenko.extractor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.papenko.extractor.entity.FullAddress;
import org.papenko.extractor.entity.Person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonExtractorServiceTest {
    @Spy
    private PersonExtractorService personExtractorService;

    @ParameterizedTest
    @ValueSource(strings = {"",
            " ",
            """
            """,
            """
                    
            """
    })
    void extractRows_shouldExtractEmptyList_whenGivenBlankInput(String blank) {
        assertEquals(List.of(), personExtractorService.extractRows(blank));
    }

    @Test
    void extractRows_shouldExtractValidList_whenGivenValidInput() {
        String validInput = """
                "Dave"   , "Smith"    , "123   main st."           , "seattle" , "wa" , "43"
                 "Alice"  , "Smith"    , "123 Main St."             , "Seattle" , "WA" , "45"
                "Bob"    , "Williams" , "234    2nd Ave."          , "Tacoma"  , "WA" , "26" 
                
                "Carol"  , "Johnson"  , "234 2nd Ave"              , "Seattle" , "WA" , "67"
                """;
        List<Person> expected = new ArrayList<>();
        expected.add(new Person("Dave", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 43));
        expected.add(new Person("Alice", "Smith", new FullAddress("123 MAIN ST", "SEATTLE", "WA"), 45));
        expected.add(new Person("Bob", "Williams", new FullAddress("234 2ND AVE", "TACOMA", "WA"), 26));
        expected.add(new Person("Carol", "Johnson", new FullAddress("234 2ND AVE", "SEATTLE", "WA"), 67));
        assertEquals(expected, personExtractorService.extractRows(validInput));
    }
}