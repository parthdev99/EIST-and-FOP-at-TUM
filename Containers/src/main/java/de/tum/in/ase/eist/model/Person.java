package de.tum.in.ase.eist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @JsonIgnoreProperties("children")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_parent_child",
            joinColumns = { @JoinColumn(name = "child_id") },
            inverseJoinColumns = { @JoinColumn(name = "parent_id") }
    )
    private Set<Person> parents = new HashSet<>();

    @JsonIgnoreProperties("parents")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_parent_child",
            joinColumns = { @JoinColumn(name = "parent_id") },
            inverseJoinColumns = { @JoinColumn(name = "child_id") }
    )
    private Set<Person> children = new HashSet<>();

    public Person() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Set<Person> getParents() {
        return parents;
    }

    public void setParents(Set<Person> parents) {
        this.parents = parents;
    }

    public Set<Person> getChildren() {
        return children;
    }

    public void setChildren(Set<Person> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(birthday, person.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday);
    }
}
