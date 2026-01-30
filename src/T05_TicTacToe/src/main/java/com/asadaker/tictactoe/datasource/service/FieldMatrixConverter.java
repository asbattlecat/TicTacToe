package com.asadaker.tictactoe.datasource.service;

import com.asadaker.tictactoe.domain.other.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FieldMatrixConverter implements AttributeConverter<int[][], String> {

  @Override
  public String convertToDatabaseColumn(int[][] field) {
    if (field == null) return null;

    try {
      return new ObjectMapper().writeValueAsString(field);
    } catch (Exception e) {
      throw new RuntimeException("Cannot convert int[][] to JSON: " + e.getMessage());
    }
  }

  @Override
  public int[][] convertToEntityAttribute(String json) {
    if (json == null || json.isEmpty()) return new int[Constants.FIELD_ROWS][Constants.FIELD_COLUMNS];

    try {
      return new ObjectMapper().readValue(json, int[][].class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Cannot convert JSON to int[][], json and exception message: " + json + e.getMessage());
    }
  }
}
