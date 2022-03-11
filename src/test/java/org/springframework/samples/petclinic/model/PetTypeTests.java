package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class PetTypeTests {
	@Test
	void testSerialization() {
		PetType type = new PetType();
		type.setName("Cat");
		type.setId(123);
		PetType other = (PetType) SerializationUtils.deserialize(SerializationUtils.serialize(type));
		assertThat(other.getName()).isEqualTo(type.getName());
		assertThat(other.getId()).isEqualTo(type.getId());
	}
}
