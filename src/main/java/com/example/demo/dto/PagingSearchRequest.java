package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingSearchRequest {

  private List<String> fields;
  private String searchTerm;
  private int size;
  private int page;

}
