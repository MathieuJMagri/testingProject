package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class VetsTests {

	@Test
	void testGetVetList() {
		Vets vets = new Vets();
		List<Vet> list = vets.getVetList();
		assertTrue(list.isEmpty());
	}

}
