package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class VisitTests {

	@Test
	void testSerialization() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2000, 1, 1));
		visit.setDescription("Checkup");
		visit.setId(555);
		visit.setPetId(123);
		Visit other = (Visit) SerializationUtils.deserialize(SerializationUtils.serialize(visit));
		assertThat(other.getDate()).isEqualTo(visit.getDate());
		assertThat(other.getDescription()).isEqualTo(visit.getDescription());
		assertThat(other.getId()).isEqualTo(visit.getId());
		assertThat(other.getPetId()).isEqualTo(visit.getPetId());
	}
}
