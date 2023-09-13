package de.tum.in.ase.eist;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import de.tum.in.ase.eist.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonServiceTest {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void testAddPerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        personService.save(person);

        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testAddParent() {
        int current=personRepository.findAll().size();
        var person = new Person();
        person.setFirstName("Maximilian");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());
        person.setId(1L);
        personService.save(person);
        assertEquals(current+1, personRepository.findAll().size());

        var person2 = new Person();
        person2.setFirstName("laila");
        person2.setLastName("Mustermann");
        person2.setBirthday(LocalDate.now());
        person2.setId(2L);
        personService.save(person2);
        assertEquals(current+2, personRepository.findAll().size());
        personService.addChild(person2,person);

        Person result=personService.addParent(person,person2);
        assertEquals(true,result.getParents().contains(person2));
        assertEquals(person,result);

    }

    @Test
    void testAddThreeParents() {
        int current=personRepository.findAll().size();
        var person = new Person();
        person.setFirstName("Maximilian");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());
        person.setId(1L);
        personService.save(person);
        assertEquals(current+1, personRepository.findAll().size());

        var person2 = new Person();
        person2.setFirstName("laila");
        person2.setLastName("Mustermann");
        person2.setBirthday(LocalDate.now());
        person2.setId(2L);
        personService.save(person2);
        assertEquals(current+2, personRepository.findAll().size());

        var person3 = new Person();
        person3.setFirstName("jeni");
        person3.setLastName("Mustermann");
        person3.setBirthday(LocalDate.now());
        person3.setId(3L);
        personService.save(person3);
        assertEquals(current+3, personRepository.findAll().size());

        var person4 = new Person();
        person4.setFirstName("keyyli");
        person4.setLastName("Mustermann");
        person4.setBirthday(LocalDate.now());
        person4.setId(4L);
        personService.save(person4);
        assertEquals(current+4, personRepository.findAll().size());

        Person result1=personService.addParent(person,person2);
        assertEquals(true,result1.getParents().contains(person2));
        assertEquals(person,result1);

        Person result2=personService.addParent(person,person3);
        assertEquals(true,result2.getParents().contains(person2));
        assertEquals(person,result2);

        assertThrows(ResponseStatusException.class, ()-> {
            personService.addParent(person,person3);
            });
    }

    @Test
    void testDeletePerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        personService.delete(person);

        assertTrue(personRepository.findAll().isEmpty());
    }
}
