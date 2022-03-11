package org.springframework.samples.petclinic.nonfunctional;

import org.springframework.samples.petclinic.model.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.persistence.*;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OwnerNonFunctionalTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private MockMvc mockMvc;

    // Create unique Owner using i loop
    private static Owner createOwner(int i) {
        Owner owner = new Owner();
        owner.setFirstName("FirstName" + i);
        owner.setLastName("LastName" + i);
        owner.setId(i);
        owner.setAddress(i + "StreetAddress");
        owner.setCity("Montreal");
        owner.setTelephone("514" + i);
        return owner;
    }

    // ********************************/ CREATE /*********************************//

    @Test
    public void testCreateOwner() {
        System.out.println("OwnerID" + "," + ("Transaction Time Of a Creation"));
        // Create Owners
        for (int i = 1; i <= 5000; i++) {

            // Start Timer
            long startingCreationTimer = System.nanoTime();
            // Print Starting Time (Not needed)
            // System.out.println("Starting time to create " + i + ": " +
            // startingCreationTimer);

            // New Owner using private method
            Owner owner = createOwner(i);
            // performing a POST of the new owner
            try {
                mockMvc.perform(post("/owners/new").flashAttr("owner", owner));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Ending time for creation
            long stoppingCreationTimer = System.nanoTime();
            // Print Ending Time (Not Needed)
            // System.out.println("Final time at creation " + i + ": " +
            // stoppingCreationTimer);
            // Print Elapsed Time of Creation (Needed)
            long transactionTimeOfCreation = stoppingCreationTimer - startingCreationTimer;
            // System.out.println("Elapsed time to create " + i + ": " +
            // (elapsedTimeOfCreation));
            System.out.println(i + "," + (transactionTimeOfCreation));
        }

    }

    // ********************************/ MODIFY /*********************************//

    @Test
    public void testUpdateOwner() {
        System.out.println("OwnerID" + "," + ("Transaction Time Of an Update"));
        // Update Owners
        for (int i = 1; i <= 5000; i++) {

            // New Owner using private method
            Owner owner = createOwner(i);

            // Start Timer
            long startingUpdateTimer = System.nanoTime();
            // Print Starting Time (Not needed)
            // System.out.println("Starting time to update " + i + ": " +
            // startingUpdateTimer);

            // Performing a PUT for the update
            try {
                mockMvc.perform(post("/owners/" + i + "/edit").flashAttr("owner", owner));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Ending time for updating an owner
            long stoppingUpdateTimer = System.nanoTime();

            // Print Ending Time (Not Needed)
            // System.out.println("Final millis to update " + i + ": " +
            // stoppingUpdateTimer);

            // Elapsed time of an update
            long transactionTimeOfUpdate = stoppingUpdateTimer - startingUpdateTimer;
            // Print Elapsed Time of Creation (Needed)
            System.out.println(i + "," + (transactionTimeOfUpdate));
            // System.out.println("Elapsed millis to update " + i + ": " +
            // (elapsedTimeOfUpdate));

        }
    }
}
