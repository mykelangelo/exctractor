package org.papenko.extractor.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class FileReaderServiceTest {
    @Spy
    private FileReaderService fileReaderService;

    @ParameterizedTest
    @ValueSource(strings = {"",
            """
            
            """,
            """
            "bob", "vance", "vance refrigeration"
            """,
            """
            "George" , "Brown"    , "345 3rd Blvd. , Apt. 200" , "Seattle" , "WA" , "18"
             "Helen" , "Brown"    , "345 3rd Blvd. Apt. 200"   , "Seattle" , "WA" , "18"
            "Ian"    , "Smith"    , "123 main st "             , "Seattle" , "Wa" , "18"
            "Jane"   , "Smith"    , "123 Main St."             , "Seattle" , "WA" , "13"
            """
    })
    void readLines_shouldReadFromMultipartFile(String multiline) {
        MockMultipartFile source = new MockMultipartFile("tempFileName", multiline.getBytes());
        String readLines;
        try {
            readLines = fileReaderService.readLines(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(multiline, readLines);
    }
}