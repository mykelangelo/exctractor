package org.papenko.extractor.dto;

public record Person(String firstName, String lastName, FullAddress fullAddress, Integer age) {
}
