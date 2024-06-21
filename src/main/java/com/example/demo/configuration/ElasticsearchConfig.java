package com.example.demo.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

  @Value("${spring.elasticsearch.uris}")
  private String elasticsearchUrl;

  @Value("${spring.elasticsearch.username}")
  private String elasticsearchUsername;

  @Value("${spring.elasticsearch.password}")
  private String elasticsearchPassword;

  @Bean
  public RestClient restClient() {
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword)
    );

    return RestClient
        .builder(HttpHost.create(elasticsearchUrl))
        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
            .setDefaultCredentialsProvider(credentialsProvider)
        )
        .build();
  }

  @Bean
  public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
    return new RestClientTransport(restClient, new JacksonJsonpMapper());
  }

  @Bean
  public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
    return new ElasticsearchClient(elasticsearchTransport);
  }

}
