package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class SpecialtyTests {

	@Test
	void testSerialization() {
		Specialty specialty = new Specialty();
		specialty.setName("Surgery");
		specialty.setId(123);
		Specialty other = (Specialty) SerializationUtils.deserialize(SerializationUtils.serialize(specialty));
		assertThat(other.getName()).isEqualTo(specialty.getName());
		assertThat(other.getId()).isEqualTo(specialty.getId());
	}
}
