package org.papenko.extractor.constant;

public interface Messages {

    String NOT_SURROUNDED_BY_QUOTES = "Not all lines are starting and ending with double quotes";

    String NOT_CONTAINING_CORRECT_NUMBER_OF_SEPARATORS = "Each non-blank line in file must contain " + Csv.VALID_SEPARATORS_COUNT +
            " valid separators (of the following pattern: " + Csv.SEPARATOR_PATTERN + ")";
}
