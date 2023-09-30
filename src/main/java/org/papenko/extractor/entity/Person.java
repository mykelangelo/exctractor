package org.papenko.extractor.entity;

public record Person(String firstName, String lastName, FullAddress fullAddress, Integer age) {
}
