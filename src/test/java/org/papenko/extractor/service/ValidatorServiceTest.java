package org.papenko.extractor.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.papenko.extractor.constant.Messages;
import org.papenko.extractor.exception.CsvValidationException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidatorServiceTest {
    @Spy
    private ValidatorService validatorService;

    @ParameterizedTest
    @ValueSource(strings = {
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25"
             "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23"
            """,
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25" 
            
            "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23"
            """
    })
    void validate_shouldNotThrow_whenAllLinesAreValid(String multiline) {
        assertDoesNotThrow(() -> validatorService.validate(multiline));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25"
            Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23"
            """,
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25"
            "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23
            """
    })
    void validate_shouldThrowValidationException_whenNotAllLinesAreSurroundedByDoubleQuotes(String multiline) {
        assertThatExceptionOfType(CsvValidationException.class)
                .isThrownBy(() -> validatorService.validate(multiline))
                .withMessageContaining(Messages.NOT_SURROUNDED_BY_QUOTES);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25"
            "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"
            """,
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"   "WA" , "25"
            "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23"
            """

    })
    void validate_shouldThrowValidationException_whenNotAllLinesContainFiveDelimiters(String multiline) {
        assertThatExceptionOfType(CsvValidationException.class)
                .isThrownBy(() -> validatorService.validate(multiline))
                .withMessageContaining(Messages.NOT_CONTAINING_CORRECT_NUMBER_OF_SEPARATORS);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "Tacoma"  , "WA" , "25"
            "Frank"  , "Jones"    , "."             , "Tacoma"  , "FL" , "23"
            """,
            """
            "Eve"    , "Smith"    , " 234 2nd   Ave.         " , "",  "WA" , "25"
            "Frank"  , "Jones"    , "234 2nd Ave."             , "Tacoma"  , "FL" , "23"
            """

    })
    void validate_shouldThrowValidationException_whenSomeFieldsAreBlank(String multiline) {
        assertThatExceptionOfType(CsvValidationException.class)
                .isThrownBy(() -> validatorService.validate(multiline))
                .withMessageContaining(Messages.SOME_FIELDS_ARE_BLANK);
    }

}