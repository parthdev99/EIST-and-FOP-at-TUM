package de.tum.in.ase.eist;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EistHerokuConfiguration {
    @Bean
    public Hibernate5JakartaModule hibernate5Module() {
        return new Hibernate5JakartaModule();
    }
}
