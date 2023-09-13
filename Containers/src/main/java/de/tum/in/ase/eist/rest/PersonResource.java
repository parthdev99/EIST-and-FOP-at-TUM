package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAll());
    }

    @GetMapping("persons/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable("personId") Long personId) {
        return ResponseEntity.ok(personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @PostMapping("persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (person.getId() != null) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return ResponseEntity.ok(personService.save(person));
    }

    @PutMapping("persons/{personId}")
    public ResponseEntity<Person> updatePerson(@RequestBody Person updatedPerson, @PathVariable("personId") Long personId) {
        if (!updatedPerson.getId().equals(personId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return ResponseEntity.ok(personService.save(updatedPerson));
    }

    @DeleteMapping("persons/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable("personId") Long personId) {
        var person = personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        personService.delete(person);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("persons/{personId}/parents")
    public ResponseEntity<Person> addParent(@RequestBody Person parent, @PathVariable("personId") Long personId) {
        var person = personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        person = personService.addParent(person, parent);
        return ResponseEntity.ok(person);
    }

    @PutMapping("persons/{personId}/children")
    public ResponseEntity<Person> addChild(@RequestBody Person child, @PathVariable("personId") Long personId) {
        var person = personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        person = personService.addChild(person, child);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("persons/{personId}/parents/{parentId}")
    public ResponseEntity<Person> removeParent(@PathVariable("personId") Long personId, @PathVariable("parentId") Long parentId) {
        var person = personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        var parent = personService.getById(parentId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        person = personService.removeParent(person, parent);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("persons/{personId}/children/{childId}")
    public ResponseEntity<Person> removeChild(@PathVariable("personId") Long personId, @PathVariable("childId") Long childId) {
        var person = personService.getById(personId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        var child = personService.getById(childId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        person = personService.removeChild(person, child);
        return ResponseEntity.ok(person);
    }
}
