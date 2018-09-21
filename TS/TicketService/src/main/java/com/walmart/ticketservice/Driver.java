package com.walmart.ticketservice;

import java.util.List;

/*
/// Driver program to execute start-to-end flow of TicketService
*/

public class Driver {
    public static void main(String args[]) throws Exception{

        String customerEmail = "abc@gmail.com";
        int noOfTickets = 7;
        String confirmationCode = "";

        long DELAY_IN_SECONDS = 1;

        Venue venue = new Venue()
                .name("AMC")
                .seats(5,5)
                .expirationDelay(5)
                .build();

        TicketServiceImpl myTicketService = new TicketServiceImpl(venue);
        SeatHold seatHold = null;

        //Find and Hold seats
        try{
            seatHold = myTicketService.findAndHoldSeats(noOfTickets, customerEmail);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // User Deciding Delay
        Thread.sleep( DELAY_IN_SECONDS * 1000);

        //If seats are successfully hold
        if(seatHold!=null){
            List<Seat> seats = seatHold.getSeats();
            try{
                confirmationCode = myTicketService.reserveSeats(seatHold.getSeatHoldId(), customerEmail);
                //To check if we have successfully reserved the tickets
                if(confirmationCode!=null){
                    System.out.println("ConfirmationCode = " + confirmationCode);
                    System.out.println("Confirmed Seats:");
                    for(Seat seat: seats){
                        System.out.println(seat.seatNumber.toString() + " " + seat.getStatus());
                    }
                }
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
