package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.example.demo.document.Vehicle;
import com.example.demo.dto.SearchRequest;
import com.example.demo.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

  private final ElasticsearchClient elasticsearchClient;

  public void create(Vehicle vehicle) {
    try {
      IndexResponse response = elasticsearchClient.index(i -> i
          .index(Indices.VEHICLE_INDEX)
          .id(vehicle.getId())
          .document(vehicle)
      );

      log.info("Indexed with version " + response.version());
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public Vehicle findById(String id) {
    try {
      GetResponse<Vehicle> response = elasticsearchClient.get(builder ->
          builder.index(Indices.VEHICLE_INDEX).id(id),
          Vehicle.class
      );

      return response.source();
    } catch (IOException e) {
      log.error(e.getMessage());

      throw new ResourceNotFoundException("Vehicle not found.");
    }
  }

  public List<Vehicle> searchForVehicle(SearchRequest searchRequest) {
    if (searchRequest == null) {
      return null;
    }

    if (searchRequest.getFields() == null || searchRequest.getFields().isEmpty()) {
      return null;
    }

    ArrayList<Vehicle> vehicles = new ArrayList<>();

    searchRequest.getFields().forEach(field -> {

      try {
        SearchResponse<Vehicle> response;

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isBlank()) {
          response = elasticsearchClient.search(
              search -> search
                  .index(Indices.VEHICLE_INDEX)
                  .query(createMatchQuery(field, searchRequest.getSearchTerm()))
                  .sort(builder -> createSort(builder, searchRequest.getSortBy(), searchRequest.getOrder())),
              Vehicle.class
          );
        } else {
          response = elasticsearchClient.search(
              search -> search
                  .index(Indices.VEHICLE_INDEX)
                  .query(createMatchQuery(field, searchRequest.getSearchTerm())),
              Vehicle.class
          );
        }

        vehicles.addAll(getVehicles(response));

      } catch (IOException e) {
        log.error(e.getMessage());
      }

    });

    return vehicles;
  }

  public List<Vehicle> getVehicleInRange(Date fromDate, Date toDate) {
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    try {
      SearchResponse<Vehicle> response = elasticsearchClient.search(
          search -> search
              .index(Indices.VEHICLE_INDEX)
              .query(createRangeQuery("created", fromDate, toDate)),
          Vehicle.class
      );

      vehicles.addAll(getVehicles(response));

    } catch (IOException e) {
      log.error(e.getMessage());
    }

    return vehicles;
  }

  private Query createMatchQuery(String field, String searchTerm) {
    return MatchQuery.of(builder -> builder
        .field(field)
        .query(searchTerm)
        .operator(Operator.And)
    )._toQuery();
  }

  private Query createRangeQuery(String field, Date fromDate , Date toDate) {
    return RangeQuery.of(builder -> builder
        .field(field)
        .gte(JsonData.of(fromDate))
        .lte(JsonData.of(toDate))
    )._toQuery();
  }

  private List<Vehicle> getVehicles(SearchResponse<Vehicle> response) {
    List<Hit<Vehicle>> hits = response.hits().hits();
    ArrayList<Vehicle> vehicles = new ArrayList<>(hits.size());

    for (Hit<Vehicle> hit: hits) {
      if (hit.source() == null) {
        continue;
      }

      Vehicle vehicle = hit.source();
      vehicles.add(vehicle);

      log.info("Found vehicle " + vehicle.getName() + ", score " + hit.source());
    }

    return vehicles;
  }

  private ObjectBuilder<SortOptions> createSort(SortOptions.Builder sortBuilder, String sortBy, SortOrder sortOrder) {
    return sortBuilder.field(f -> f
        .field(sortBy)
        .order(sortOrder == null ? SortOrder.Asc : sortOrder)
    );
  }

}
