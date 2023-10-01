package org.papenko.extractor.dto;

import java.util.List;

public record HouseholdInfo(Integer occupantsNumber, FullAddress address, List<Person> adults) {
}
