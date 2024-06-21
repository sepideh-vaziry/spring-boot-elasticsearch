package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.demo.document.Vehicle;
import com.example.demo.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

}
