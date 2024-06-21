package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.demo.document.Vehicle;
import com.example.demo.dto.SearchRequest;
import com.example.demo.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public Set<Vehicle> searchForVehicle(SearchRequest searchRequest) {
    if (searchRequest == null) {
      return null;
    }

    if (searchRequest.getFields() == null || searchRequest.getFields().isEmpty()) {
      return null;
    }

    Set<Vehicle> vehicles = new HashSet<>();

    searchRequest.getFields().forEach(field -> {

      try {
        Query query = createMatchQuery(field, searchRequest.getSearchTerm());

        SearchResponse<Vehicle> response = elasticsearchClient.search(search -> search
                .index(Indices.VEHICLE_INDEX)
                .query(query),
            Vehicle.class
        );

        vehicles.addAll(getVehicles(response, vehicles));

      } catch (IOException e) {
        log.error(e.getMessage());
      }

    });

    return vehicles;
  }

  private Query createMatchQuery(String field, String searchTerm) {
    return MatchQuery.of(builder -> builder
        .field(field)
        .query(searchTerm)
        .operator(Operator.And)
    )._toQuery();
  }

  private static Set<Vehicle> getVehicles(SearchResponse<Vehicle> response, Set<Vehicle> vehicles) {
    List<Hit<Vehicle>> hits = response.hits().hits();

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

}
