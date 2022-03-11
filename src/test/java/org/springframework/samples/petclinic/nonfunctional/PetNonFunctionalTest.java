package org.springframework.samples.petclinic.nonfunctional;

import org.springframework.samples.petclinic.model.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.persistence.*;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PetNonFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    // Create unique Owner using i loop
    private static Owner createOwner(int i) {
        Owner owner = new Owner();
        owner.setFirstName("FirstName" + i);
        owner.setLastName("LastName" + i);
        owner.setId(i);
        owner.setAddress(i + "Street");
        owner.setCity("Montreal");
        owner.setTelephone("514" + i);
        return owner;
    }

    // Create unique Pet using i loop
    private static Pet createPet(int i) {
        Pet pet = new Pet();
        pet.setName("Name" + i);
        pet.setId(i);
        pet.setBirthDate(LocalDate.of(2008, 8, 8));
        pet.setOwner(createOwner(i));
        PetType petType = new PetType();
        petType.setName("Cat");
        pet.setType(petType);
        return pet;
    }

    // ********************************/ CREATE /*********************************//

    @Test
    public void testCreatePet() {
        System.out.println("PetID" + "," + ("Transaction Time Of a Creation"));
        // Create Pets
        for (int i = 1; i <= 5000; i++) {

            // Start Timer
            long startingCreationTimer = System.nanoTime();
            // Print Starting Time (Not needed)
            // System.out.println("Starting time to create" + i + ": " +
            // startingCreationTimer);

            // New Pet using private method
            Pet pet = createPet(i);
            // performing a POST of the new pet
            try {
                mockMvc.perform(post("/pets/new").flashAttr("pet", pet));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Print Ending Time (Not Needed)
            long stoppingCreationTimer = System.nanoTime();
            // System.out.println("Final time at creation" + i + ": " +
            // stoppingCreationTimer);
            // Print Elapsed Time of Creation (Needed)
            long transactionTimeOfCreation = stoppingCreationTimer - startingCreationTimer;
            // System.out.println("Elapsed time to create" + i + ": " +
            // (elapsedTimeOfCreation));
            System.out.println(i + "," + (transactionTimeOfCreation));
        }

    }

    // ********************************/ MODIFY /*********************************//

    @Test
    public void testUpdatePet() {
        System.out.println("PetID" + "," + ("Transaction Time Of an Update"));
        // Update Pets
        for (int i = 1; i <= 5000; i++) {

            // New Owner using private method
            Pet pet = createPet(i);

            // Start Timer
            long startingUpdateTimer = System.nanoTime();
            // Log Start Time
            // System.out.println("Starting timer to update" + i + ": " +
            // startingUpdateTimer);

            // Performing a PUT for the update
            try {
                mockMvc.perform(post("/pet/" + i + "/edit").flashAttr("pet", pet));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Ending time for updating a pet
            long stoppingUpdateTimer = System.nanoTime();
            // System.out.println("Final millis" + i + ": " + stoppingUpdateTimer);

            // Elapsed time of an update
            long transactionTimeOfUpdate = stoppingUpdateTimer - startingUpdateTimer;
            // Print Elapsed Time of Creation (Needed)
            System.out.println(i + "," + (transactionTimeOfUpdate));
            // System.out.println("Elapsed millis" + i + ": " + (elapsedTimeOfUpdate));
        }
    }

}
