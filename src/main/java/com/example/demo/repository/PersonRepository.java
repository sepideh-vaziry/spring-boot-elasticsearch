package com.example.demo.repository;

import com.example.demo.document.Person;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {

  Optional<Person> findByName(String name);

}
