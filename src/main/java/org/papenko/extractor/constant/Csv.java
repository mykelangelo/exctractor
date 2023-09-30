package org.papenko.extractor.constant;

public interface Csv {

    String SEPARATOR_PATTERN = "\"\\s*,\\s*\"";
    int VALID_SEPARATORS_COUNT = 5;
    String INVALID_ADDRESS_SYMBOLS_PATTERN = "[^\\sa-zA-Z0-9]";
}
