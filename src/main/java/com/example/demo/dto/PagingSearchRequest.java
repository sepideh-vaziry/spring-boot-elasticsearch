package com.example.demo.dto;

import co.elastic.clients.elasticsearch._types.SortOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingSearchRequest {

  private List<String> fields;
  private String searchTerm;
  private String sortBy;
  private SortOrder order;
  private int size;
  private int page;

}
