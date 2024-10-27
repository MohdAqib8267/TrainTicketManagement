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
}
