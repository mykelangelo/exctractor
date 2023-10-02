package org.papenko.extractor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FileReaderService {

    /**
     * @param file input file
     * @return multiline String with contents from file
     * @throws IOException from {@link MultipartFile#getBytes()} in case of access errors (if the temporary store fails)
     */
    public String readLines(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}
