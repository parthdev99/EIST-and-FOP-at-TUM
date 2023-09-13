package de.tum.in.ase.eist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testAddPerson() throws Exception {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        var response = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json")
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testAddParent() throws Exception {
        int current=personRepository.findAll().size();
        var person = new Person();
        person.setFirstName("jenish");
        person.setLastName("levis");
        person.setBirthday(LocalDate.now());
        //person.setId(1L);
        var response = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person=objectMapper.readValue(response,Person.class);
        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(current+1, personRepository.findAll().size());


        var person2 = new Person();
        person2.setFirstName("lovely");
        person2.setLastName("patel");
        person2.setBirthday(LocalDate.now());
        //person2.setId(2L);
        var response2 = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person2))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person2=objectMapper.readValue(response2,Person.class);
        //assertEquals(HttpStatus.OK.value(), response2.getStatus());
        //my comment
        assertEquals(current+2, personRepository.findAll().size());

        var response3 = this.mvc.perform(
                put("/persons/{personId}/parents",2)
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person2=objectMapper.readValue(response3,Person.class);

        assertEquals(true,person2.getParents().contains(person));
        //assertEquals(HttpStatus.OK.value(), response3.getStatus());
        //assertEquals(person,personRepository.findWithParentsAndChildrenById(1L).get());


    }

    @Test
    void testAddThreeParents() throws Exception{
        int current=personRepository.findAll().size();
        var person = new Person();
        person.setFirstName("Maximilian");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());
        var response = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person=objectMapper.readValue(response,Person.class);
        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(current+1, personRepository.findAll().size());


        var person2 = new Person();
        person2.setFirstName("keyly");
        person2.setLastName("Mustermann");
        person2.setBirthday(LocalDate.now());
        var response2 = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person2))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person2=objectMapper.readValue(response2,Person.class);
        //assertEquals(HttpStatus.OK.value(), response2.getStatus());
        assertEquals(current+2, personRepository.findAll().size());

        var person3 = new Person();
        person3.setFirstName("lilly");
        person3.setLastName("Mustermann");
        person3.setBirthday(LocalDate.now());
        var response3 = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person3))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person3=objectMapper.readValue(response3,Person.class);
        //assertEquals(HttpStatus.OK.value(), response3.getStatus());
        assertEquals(current+3, personRepository.findAll().size());

        var person4 = new Person();
        person4.setFirstName("jenny");
        person4.setLastName("Mustermann");
        person4.setBirthday(LocalDate.now());
        var response4 = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person4))
                        .contentType("application/json")
        ).andReturn().getResponse().getContentAsString();
        person4=objectMapper.readValue(response4,Person.class);
        //assertEquals(HttpStatus.OK.value(), response4.getStatus());
        assertEquals(current+4, personRepository.findAll().size());

        var response5 = this.mvc.perform(
                put("/persons/1/parents")
                        .content(objectMapper.writeValueAsString(person2))
                        .contentType("application/json")
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response5.getStatus());
        //assertEquals(person2,personRepository.findWithParentsAndChildrenById(1l));

        var response6 = this.mvc.perform(
                put("/persons/1/parents")
                        .content(objectMapper.writeValueAsString(person3))
                        .contentType("application/json")
        ).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response6.getStatus());
        //assertEquals(person3,person.getParents().contains(person3));

        var response7 = this.mvc.perform(
                put("/persons/1/parents")
                        .content(objectMapper.writeValueAsString(person4))
                        .contentType("application/json")
        ).andReturn().getResponse();
        assertNotEquals(HttpStatus.OK.value(), response7.getStatus());
    }

    @Test
    void testDeletePerson() throws Exception {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        var response = this.mvc.perform(
                delete("/persons/" + person.getId())
                        .contentType("application/json")
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertTrue(personRepository.findAll().isEmpty());
    }
}

