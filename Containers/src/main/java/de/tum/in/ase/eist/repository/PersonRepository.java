package de.tum.in.ase.eist.repository;

import de.tum.in.ase.eist.model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @EntityGraph(type = LOAD, attributePaths = {"parents"})
    Optional<Person> findWithParentsById(Long personId);

    @EntityGraph(type = LOAD, attributePaths = {"children"})
    Optional<Person> findWithChildrenById(Long personId);

    @EntityGraph(type = LOAD, attributePaths = {"parents", "children"})
    Optional<Person> findWithParentsAndChildrenById(Long personId);
}
