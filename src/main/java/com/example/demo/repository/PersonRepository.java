package com.example.demo.repository;

import com.example.demo.document.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {

  Optional<Person> findByName(String name);

}
