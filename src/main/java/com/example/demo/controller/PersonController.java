package com.example.demo.controller;

import com.example.demo.document.Person;
import com.example.demo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/person")
public class PersonController {

  private final PersonService personService;

  @PostMapping
  public ResponseEntity<Person> create(
      @RequestBody Person person
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(personService.create(person));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> findById(
      @PathVariable String id
  ) {
    return ResponseEntity.ok(personService.findById(id));
  }

}
