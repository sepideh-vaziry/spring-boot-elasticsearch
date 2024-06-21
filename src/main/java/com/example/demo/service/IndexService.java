package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.example.demo.helper.Indices;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final List<String> indicesToCreate = List.of(Indices.VEHICLE_INDEX);
    private final ElasticsearchClient elasticsearchClient;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void tryToCreateIndices() {
        for (String indexName: indicesToCreate) {

            try {
                BooleanResponse response = elasticsearchClient.indices().exists(
                    new ExistsRequest.Builder().index(indexName).build()
                );

                if (response.value()) {
                    continue;
                }
                createIndex(indexName);

            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }

    }

    private void createIndex(String indexName) {
        Resource resource = resourceLoader.getResource("classpath:static/mappings/" + indexName + ".json");
        try(InputStream input = resource.getInputStream()) {
            CreateIndexRequest request = CreateIndexRequest.of(b -> b
                .index(indexName)
                .withJson(input)
            );

            boolean created = elasticsearchClient.indices().create(request).acknowledged();
            log.info("Index with name: {} created: {}", indexName, created);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
