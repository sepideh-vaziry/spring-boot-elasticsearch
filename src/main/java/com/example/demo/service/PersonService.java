package com.example.demo.service;

import com.example.demo.document.Person;
import com.example.demo.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;

  public Person create(Person person) {
    return personRepository.save(person);
  }

  public Person findById(String id) {
    return personRepository.findById(id)
        .orElse(null);
  }

  public Person findByName(String name) {
    return personRepository.findByName(name)
        .orElse(null);
  }

}
