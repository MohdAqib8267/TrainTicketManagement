package controller;

import java.util.Scanner;

import model.Booking;
import model.Coach;
import model.Train;
import model.TrainDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainController {
	private Scanner sc = new Scanner(System.in);
	private TrainDao dao = TrainDao.getInstance();
	
	public void addTrain(Boolean admin) {
		
		if(!admin) {
			System.out.println("Sorry,You can't access this service");
		}
		dao.createConnection();
		
		//create table if not exists
		dao.createTables();
		
		System.out.println("Enter Train Name:");
		String name = sc.nextLine();
		System.out.println("Enter Source:");
		String source = sc.nextLine();
		System.out.println("Enter Destionation:");
		String destination = sc.nextLine();
		System.out.println("Enter arrival time:");
		String arrivalTime = sc.nextLine();
		System.out.println("Enter departure time:");
		String departureTime = sc.nextLine();
		
		Map<String, List<Coach>> coachMap = new HashMap<>();
		System.out.print("Enter the number of different coach classes for this train: ");
        int classCount = sc.nextInt();
        sc.nextLine();
        
        for (int i = 1; i <= classCount; i++) {
        	System.out.print("Enter Coach Class Type (e.g., AC Tier 1, CC, Sleeper, etc.): ");
            String coachType = sc.nextLine();
            
            System.out.print("Enter number of coaches for " + coachType + ": ");
            int coachCount = sc.nextInt();
            sc.nextLine();
            
            System.out.print("Enter number of seats in " + coachType + ": ");
            int seatCount = sc.nextInt();
            sc.nextLine();
            
            //initially all seats will be available for booking
            int availableSeats = seatCount;
            
            List<Coach> coaches = new ArrayList<>();
            
            for(int j=1;j<=coachCount;j++) {
            	String coachName = coachType.charAt(0)+Integer.toString(j);
            	coaches.add(new Coach(coachType,coachName,seatCount,availableSeats));
            }
            coachMap.put(coachType, coaches);
            
        }
        Train train = new Train(name, source, destination, arrivalTime, departureTime, coachMap);  
		dao.addTrainToDB(train);
		dao.closeConnection();
	}
	public void viewAllAvailableTrains(Boolean admin) {
		if(!admin) {
			System.out.println("Sorry,You can't access this service");
		}
		dao.createConnection();
		
		ResultSet rs = dao.findAllTrains();
		try {
			while (rs.next()) {
			    // Display Train Details Section
			    System.out.println("\n<======================= TRAIN DETAILS ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.println("<==========================================================================>");

			    // Display Coach Details Table Header
			    System.out.println("\n<======================= COACH DETAILS =============================================>");
			    System.out.printf("| %-15s | %-15s | %-15s | %-20s | %-15s |%n", 
			                      "Coach Name", "Coach Class", "Total Seats", "Available Seats", "Booked Seats");
			    System.out.println("|-----------------|-----------------|-----------------|----------------------|-----------------|");

			    // Iterate through coach details and display each row
			    ResultSet rs_coach = dao.findCoach(rs.getInt("id"));
			    while (rs_coach.next()) {
			        String coachName = rs_coach.getString("coachName");
			        String coachType = rs_coach.getString("coachType");
			        int seatCount = rs_coach.getInt("seatCount");
			        int availableSeats = rs_coach.getInt("availableSeats");
			        int bookedSeats = seatCount - availableSeats;

			        System.out.printf("| %-15s | %-15s | %-15d | %-20d | %-15d |%n", 
			                          coachName, coachType, seatCount, availableSeats, bookedSeats);
			    }
			    System.out.println("<==============================================================================>\n");
			}

		} catch (SQLException e) {
			System.out.println("Error in Fetching trains: "+e);
		}
		dao.closeConnection();
	}
	
	//Search for a train
	public void searchTrains() {
		dao.createConnection();
		System.out.println("Enter Source: ");
		String src = sc.nextLine();
		System.out.println("Enter Destionation: ");
		String dest = sc.nextLine();
		
		ResultSet rs = dao.findTrains(src,dest,null);
		try {
			while (rs.next()) {
			    // Display Train Details Section
			    System.out.println("\n<======================= TRAIN DETAILS ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.println("<==========================================================================>");

			    // Display Coach Details Table Header
			    System.out.println("\n<======================= COACH DETAILS =============================================>");
			    System.out.printf("| %-15s | %-15s | %-15s | %-20s | %-15s |%n", 
			                      "Coach Name", "Coach Class", "Total Seats", "Available Seats", "Booked Seats");
			    System.out.println("|-----------------|-----------------|-----------------|----------------------|-----------------|");

			    // Iterate through coach details and display each row
			    ResultSet rs_coach = dao.findCoach(rs.getInt("id"));
			    while (rs_coach.next()) {
			        String coachName = rs_coach.getString("coachName");
			        String coachType = rs_coach.getString("coachType");
			        int seatCount = rs_coach.getInt("seatCount");
			        int availableSeats = rs_coach.getInt("availableSeats");
			        int bookedSeats = seatCount - availableSeats;

			        System.out.printf("| %-15s | %-15s | %-15d | %-20d | %-15d |%n", 
			                          coachName, coachType, seatCount, availableSeats, bookedSeats);
			    }
			    System.out.println("<==============================================================================>\n");
			}
		} catch (SQLException e) {
			
			System.out.println("Error in Showing trains: "+e);
		}
		
		dao.closeConnection();
	}
	public void bookTickets(int userId) {
		dao.createConnection();
		dao.createBookingTable();
		System.out.println("Enter Train Number:");
		int train_id = sc.nextInt(); sc.nextLine();
		
		ResultSet rs = dao.findTrains(null,null,train_id);
		ResultSet rs_coach = dao.findCoach(train_id);
		
		Map<String,Integer>mp = new HashMap<>();
		try {
			if(rs.next()) {
				System.out.println("\n<======================= TRAIN DETAILS ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.println("<==========================================================================>");
			}
			
			
			 System.out.println("\n<======================= COACH DETAILS =============================================>");
			 System.out.printf("| %-15s | %-15s | %-15s | %-20s | %-15s |%n", 
			                      "Coach Name", "Coach Class", "Total Seats", "Available Seats", "Booked Seats");
			 System.out.println("|-----------------|-----------------|-----------------|----------------------|-----------------|");
			while(rs_coach.next()) {
				 String coachName = rs_coach.getString("coachName");
			        String coachType = rs_coach.getString("coachType");
			        int seatCount = rs_coach.getInt("seatCount");
			        int availableSeats = rs_coach.getInt("availableSeats");
			        int bookedSeats = seatCount - availableSeats;
			        
			        mp.put(coachType, mp.getOrDefault(coachType, 0)+availableSeats);
			        
			        System.out.printf("| %-15s | %-15s | %-15d | %-20d | %-15d |%n", 
			                          coachName, coachType, seatCount, availableSeats, bookedSeats);
			}
			System.out.println("<==============================================================================>\n");
		} catch (SQLException e) {
			
			System.out.println("Error in Showing Train: "+e);
		}
		System.out.println("Choose Class: ");
		String coachType = sc.nextLine();
		System.out.println("Enter number of tickets: ");
		int noOfTickets = sc.nextInt(); sc.nextLine();
		
		int SeatsAvailableForCoachType = mp.get(coachType);
		if(noOfTickets > SeatsAvailableForCoachType) {
			System.out.println("Not Enough Seats Available.");
			return;
		}
		dao.booking(userId,train_id,coachType,noOfTickets);
		dao.closeConnection();
	}
	public void ViewBookings(int userId) {
		if((userId==0)) {
			System.out.println("Something went wrong, please try again later.");
			return;
		}
		dao.createConnection();
		String query = "select t.id, t.name, t.source, t.destination,t.arrivalTime, t.departureTime,b.coachName,b.bookingDate,b.status from train as t\r\n"
				+ "inner join booking as b\r\n"
				+ "on t.id = b.train_id\r\n"
				+ "where b.userId="+userId+"\r\n"
				+ "order by t.name;";
		ResultSet rs = dao.viewTickets(userId,query);
		try {
			while(rs.next()) {
				System.out.println("\n<======================= Ticket DETAILS ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Booking Date-Time", rs.getString("bookingDate"));
			    System.out.printf("| %-15s : %-50s |%n", "Status", rs.getString("status"));
			    System.out.println("<==========================================================================>");
			}
		} catch (SQLException e) {
			System.out.println("Error in showing tickets: "+e);
		}
		
		dao.closeConnection();
		
	}
	public void cancelBooking(int userId) {
		if((userId==0)) {
			System.out.println("Something went wrong, please try again later.");
			return;
		}
		dao.createConnection();
		String query = "select t.id, t.name, t.source, t.destination,t.arrivalTime, t.departureTime,b.coachName,b.bookingDate,b.status,b.id as booking_id from train as t\r\n"
				+ "inner join booking as b\r\n"
				+ "on t.id = b.train_id\r\n"
				+ "where b.userId="+userId+" and b.status=\"ACTIVE\"\r\n"
				+ "order by t.name;";
		ResultSet rs = dao.viewTickets(userId,query);
		try {
			while(rs.next()) {
				System.out.println("\n<======================= Ticket DETAILS ==================================>");
				System.out.printf("| %-15s : %-50d |%n", "Ticket ID: ", rs.getInt("booking_id"));
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Booking Date-Time", rs.getString("bookingDate"));
			    System.out.printf("| %-15s : %-50s |%n", "Status", rs.getString("status"));
			    System.out.println("<==========================================================================>");
			}
		} catch (SQLException e) {
			System.out.println("Error in showing tickets: "+e);
		}
		System.out.println("Enter Ticket ID: ");
		int ticket_id = sc.nextInt(); sc.nextLine();
		Boolean result = dao.cancelTicket(userId,ticket_id);
		if(result) {
			System.out.println("Booking cancelled successfully for Ticket ID: "+ticket_id);
			return;
		}
		else {
			System.out.println("something went wrong");
		}
		dao.closeConnection();
		
	}
	public void SortTrains(int userId) {
		System.out.println("Enter Source: ");
		String src = sc.nextLine();
		System.out.println("Enter Destination: ");
		String dest = sc.nextLine();
		dao.createConnection();
		System.out.println("1. Sort based on Departure time.");
		
		int option = sc.nextInt(); sc.nextLine();
		String preference=null;
		
		switch(option) {
		case 1:
			preference="departureTime";
			break;
		default:
			System.out.println("Invalid option.");
		}
		ResultSet rs = dao.sortBasedOnPreference(src,dest,preference);
		try {
			while(rs.next()) {
				System.out.println("\n<======================= TRAIN DETAILS ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "Train Number", rs.getInt("id"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destination", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Arrival Time", rs.getString("arrivalTime"));
			    System.out.printf("| %-15s : %-50s |%n", "Departure Time", rs.getString("departureTime"));
			    System.out.println("<==========================================================================>");
			}
		} catch (Exception e) {
			System.out.println("Error in Showing Sorted Trains :"+e);
		}
		dao.closeConnection();
	}
	public void viewAllUsersWithHistory(boolean admin) {
		if(!admin) {
			System.out.println("You can't access this service");
			return;
		}
		dao.createConnection();
		ResultSet rs = dao.findUsersWithHistory();
		try {
			while(rs.next()) {
				System.out.println("\n<======================= User Details ==================================>");
			    System.out.printf("| %-15s : %-50d |%n", "User Id", rs.getInt("userId"));
			    System.out.printf("| %-15s : %-50s |%n", "User Name", rs.getString("name"));
			    System.out.printf("| %-15s : %-50d |%n", "Train Id", rs.getInt("trainId"));
			    System.out.printf("| %-15s : %-50s |%n", "Train Name", rs.getString("trainName"));
			    System.out.printf("| %-15s : %-50s |%n", "Seat Number", rs.getInt("seatNumber"));
			    System.out.printf("| %-15s : %-50s |%n", "Coach", rs.getString("coachName"));
			    System.out.printf("| %-15s : %-50s |%n", "Source", rs.getString("source"));
			    System.out.printf("| %-15s : %-50s |%n", "Destionation", rs.getString("destination"));
			    System.out.printf("| %-15s : %-50s |%n", "Booking Date", rs.getString("bookingDate"));
			    System.out.printf("| %-15s : %-50s |%n", "status", rs.getString("status"));
			    System.out.println("<==========================================================================>");
			}
		} catch (Exception e) {
			System.out.println("Error in Showing User Details :"+e);
		}
		dao.closeConnection();
	}
}
