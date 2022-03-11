package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class PersonTests {

	@Test
	void testSerialization() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");
		person.setId(123);
		Person other = (Person) SerializationUtils.deserialize(SerializationUtils.serialize(person));
		assertThat(other.getFirstName()).isEqualTo(person.getFirstName());
		assertThat(other.getLastName()).isEqualTo(person.getLastName());
		assertThat(other.getId()).isEqualTo(person.getId());
	}

}
