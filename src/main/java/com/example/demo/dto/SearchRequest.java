package com.example.demo.dto;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

  private List<String> fields;
  private String searchTerm;
  private String sortBy;
  private SortOrder order;

}
