package org.papenko.extractor.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.papenko.extractor.constant.Messages;
import org.papenko.extractor.constant.OutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExtractorControllerIntegrationTest {
    private static final String RESOURCES = "src/test/resources/";
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("filenamesAndMessagesProvider")
    void extractFromFile_shouldReturnCorrectErrorMessage_whenFileContentsAreMalformed(String filename, String message) throws Exception {
        MvcResult mvcResult = mockMvc.perform(multipart("/extract/file")
                        .file("file", readBytesFromFile("%s%s".formatted(RESOURCES, filename))))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(message));
    }

    static Stream<Arguments> filenamesAndMessagesProvider() {
        return Stream.of(
                Arguments.of("blanks.txt", Messages.SOME_FIELDS_ARE_BLANK),
                Arguments.of("no-quotes.txt", Messages.NOT_SURROUNDED_BY_QUOTES),
                Arguments.of("not-enough-separators.txt", Messages.NOT_CONTAINING_CORRECT_NUMBER_OF_SEPARATORS)
        );
    }

    @ParameterizedTest
    @MethodSource("formatsAndOutputProvider")
    void extractFromFile_shouldReturnCorrectOutput_whenFileContentsAreValid(OutputFormat format, String output) throws Exception {
        MvcResult mvcResult = mockMvc.perform(multipart("/extract/file?outputFormat=%s".formatted(format))
                        .file("file", readBytesFromFile("%s%s".formatted(RESOURCES, "valid-input.txt"))))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(output, mvcResult.getResponse().getContentAsString());
    }

    static Stream<Arguments> formatsAndOutputProvider() {
        return Stream.of(
                Arguments.of(OutputFormat.JSON, """
                        [{"occupantsNumber":1,"address":{"address":"234 2ND AVE","city":"TACOMA","state":"FL"},"adults":[{"firstName":"Frank","lastName":"Jones","fullAddress":{"address":"234 2ND AVE","city":"TACOMA","state":"FL"},"age":23}]},{"occupantsNumber":2,"address":{"address":"234 2ND AVE","city":"TACOMA","state":"WA"},"adults":[{"firstName":"Eve","lastName":"Smith","fullAddress":{"address":"234 2ND AVE","city":"TACOMA","state":"WA"},"age":25},{"firstName":"Bob","lastName":"Williams","fullAddress":{"address":"234 2ND AVE","city":"TACOMA","state":"WA"},"age":26}]},{"occupantsNumber":1,"address":{"address":"234 2ND AVE","city":"SEATTLE","state":"WA"},"adults":[{"firstName":"Carol","lastName":"Johnson","fullAddress":{"address":"234 2ND AVE","city":"SEATTLE","state":"WA"},"age":67}]},{"occupantsNumber":2,"address":{"address":"345 3RD BLVD APT 200","city":"SEATTLE","state":"WA"},"adults":[{"firstName":"George","lastName":"Brown","fullAddress":{"address":"345 3RD BLVD APT 200","city":"SEATTLE","state":"WA"},"age":18},{"firstName":"Helen","lastName":"Brown","fullAddress":{"address":"345 3RD BLVD APT 200","city":"SEATTLE","state":"WA"},"age":18}]},{"occupantsNumber":4,"address":{"address":"123 MAIN ST","city":"SEATTLE","state":"WA"},"adults":[{"firstName":"Alice","lastName":"Smith","fullAddress":{"address":"123 MAIN ST","city":"SEATTLE","state":"WA"},"age":45},{"firstName":"Dave","lastName":"Smith","fullAddress":{"address":"123 MAIN ST","city":"SEATTLE","state":"WA"},"age":43},{"firstName":"Ian","lastName":"Smith","fullAddress":{"address":"123 MAIN ST","city":"SEATTLE","state":"WA"},"age":18}]}]"""),
                Arguments.of(OutputFormat.PLAIN, """
                        1 people live in household @ 234 2ND AVE, TACOMA, FL
                        Adults:
                        Frank Jones, age 23

                        2 people live in household @ 234 2ND AVE, TACOMA, WA
                        Adults:
                        Eve Smith, age 25
                        Bob Williams, age 26

                        1 people live in household @ 234 2ND AVE, SEATTLE, WA
                        Adults:
                        Carol Johnson, age 67

                        2 people live in household @ 345 3RD BLVD APT 200, SEATTLE, WA
                        Adults:
                        George Brown, age 18
                        Helen Brown, age 18

                        4 people live in household @ 123 MAIN ST, SEATTLE, WA
                        Adults:
                        Alice Smith, age 45
                        Dave Smith, age 43
                        Ian Smith, age 18"""),
                Arguments.of(OutputFormat.CSV, """
                        1, 234 2ND AVE, TACOMA, FL
                        Frank, Jones, 23

                        2, 234 2ND AVE, TACOMA, WA
                        Eve, Smith, 25
                        Bob, Williams, 26

                        1, 234 2ND AVE, SEATTLE, WA
                        Carol, Johnson, 67

                        2, 345 3RD BLVD APT 200, SEATTLE, WA
                        George, Brown, 18
                        Helen, Brown, 18

                        4, 123 MAIN ST, SEATTLE, WA
                        Alice, Smith, 45
                        Dave, Smith, 43
                        Ian, Smith, 18""")
        );
    }

    private byte[] readBytesFromFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}