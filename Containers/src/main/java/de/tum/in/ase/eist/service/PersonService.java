package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person save(Person person) {
        if (person.getBirthday().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday may not be in the future");
        }
        return personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public Optional<Person> getById(Long id) {
        return personRepository.findWithParentsAndChildrenById(id);
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person addParent(Person person, Person parent) {
        // TODO: Implement
        Set<Person> temp=person.getParents();
        if(temp.size()>=2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            temp.add(parent);
        }
        person.setParents(temp);
        return save(person);
    }

    public Person addChild(Person person, Person child) {
        // TODO: Implement
        Set<Person> temp=child.getParents();
        if(temp.size()>=2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            Set<Person> temp2=person.getChildren();
            temp2.add(child);
            person.setChildren(temp2);
        }
        return save(person);
        //my comment
    }

    public Person removeParent(Person person, Person parent) {
        // TODO: Implement
        Set<Person> temp=person.getParents();
        if(temp.size()<=1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            temp.remove(parent);
            person.setParents(temp);
        }
        return save(person);
    }

    public Person removeChild(Person person, Person child) {
        // TODO: Implement
        Set<Person> temp=child.getParents();
        if(temp.size()<=1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            Set<Person> temp2=person.getChildren();
            temp2.remove(child);
            person.setChildren(temp2);
        }
        return save(person);
    }
    //my comment
}
