package com.walmart.ticketservice;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;


/**
 * Unit test for TicketServiceImpl
 */

public class TicketServiceImplTest {

    Venue venue;
    TicketServiceImpl ticketService;
    static final Logger LOG = Logger.getLogger(TicketService.class.getName());

    //Setup required for tests
    @Before
    public void setUp() throws Exception {

        this.venue = new Venue()
                .name("AMC")
                .seats(5,5)
                .expirationDelay(120)
                .build();

        this.ticketService = new TicketServiceImpl(venue);
    }

    //Test for start-to-end flow
    @Test
    public void testSuccessFlow() throws InterruptedException{

        SeatHold seatHold = this.ticketService.findAndHoldSeats(5, "abc@xyz.com");

        //Wait for sometime
        userDeciding(2);

        //Try reserving seats using SeatHoldId
        String confirmationCode = ticketService.reserveSeats(seatHold.getSeatHoldId(), "abc@xyz.com");

        String expectedConfirmationNumber = "AMC00000001";
        assertEquals(expectedConfirmationNumber, confirmationCode);
    }


    //Test for a failure flow - reserving after the hold expiry
    @Test
    public void testFailureFlow() throws InterruptedException{

        this.venue.setExpirationDelay(2);
        SeatHold seatHold = this.ticketService.findAndHoldSeats(5, "abc@xyz.com");

        //Wait for sometime
        userDeciding(3);

        //Try reserving seats using SeatHoldId
        String confirmationCode = ticketService.reserveSeats(seatHold.getSeatHoldId(), "abc@xyz.com");

        String expectedConfirmationNumber = null;
        assertEquals(expectedConfirmationNumber, confirmationCode);
    }

    //Test for multiple customers
    @Test
    public void testMultipleUserFlowSeatHoldNull() throws InterruptedException{

        //total seats = 25
        this.venue.setExpirationDelay(4);

        SeatHold seatHold_customer1 = this.ticketService.findAndHoldSeats(5, "abc@xyz.com");

        userDeciding(2);
        SeatHold seatHold_customer2 = this.ticketService.findAndHoldSeats(25, "pqr@xyz.com");


        assertNull(seatHold_customer2);
    }

    //Test for multiple customers
    @Test
    public void testMultipleUserFlowSeatHold() throws InterruptedException{

        //total seats = 25
        this.venue.setExpirationDelay(3);

        SeatHold seatHold_customer1 = this.ticketService.findAndHoldSeats(5, "abc@xyz.com");

        userDeciding(4);
        SeatHold seatHold_customer2 = this.ticketService.findAndHoldSeats(25, "pqr@xyz.com");

        assertNotNull(seatHold_customer2);
    }


    //Method to introduce delay in reserving seats after holding
    private void userDeciding(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

}
