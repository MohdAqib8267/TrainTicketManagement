
import java.util.*;

import model.UserDao;
import view.UserView;

public class IRCTC {

	
	private static Scanner sc=new Scanner(System.in);
	private static UserView userView = new UserView();
	

	public static void main(String a[]){
		
		
		while(true){
			System.out.println("Welcome to the Train Reservation System");
			System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");
           
            int option = sc.nextInt();
            sc.nextLine();
	        switch(option){
	           case 1:
	        	   userView.RegisterUser();
	        	   break;
	           case 2:
	        	   userView.LoginUser();
	        	   break;
	           case 3:
	        	   System.out.println("Exiting from System.");
	        	   UserDao.getInstance().closeConnection();
	        	   break;
	           default:
	        	   System.out.println("Invalid option, Please try again...");
	        }
		}
	}
	


	
//	private static void ReservationSystem(){
//		while(true){
//			System.out.println("Main Menu:");
//            System.out.println("1. Search for Trains");
//            System.out.println("2. Book a Ticket");
//            System.out.println("3. View Booking History");
//            System.out.println("4. Sort Trains");
////            System.out.println("5. Configure System Settings");
//            System.out.println("7. View Booking Details");
//            System.out.println("8. Cancel a Booking");
//            if(admin){
//            	System.out.println("9. View all users with Ticket History.");
//            	System.out.println("10. View All available Trains");
//            	System.out.println("11. Add a Trains");
//            }
//            System.out.println("12. Logout");
//            int option = sc.nextInt(); sc.nextLine();
//            switch(option){
//            	case 1:
//            		searchTrains();
//            		break;
//            	case 2:
//            		bookTickets();
//            		break;
//            	case 3:
//            		ViewBookings();
//            		break;
//            	case 4:
//            		SortTrains();
//            		break;	
//            	case 7:
//            		viewBookingDetails();
//            		break;	
//            	case 8:
//            		cancelBooking();
//            		break;
//            		
//            	case 9:
//            		if(admin){
//            			viewAllUsersWithHistory();
//                		break;
//            		}
//            		else{
//            			System.out.println("Invalid Response, please choose correct keys");
//            		}
//            	case 10:
//            		if(admin){
//            			viewAllAvailableTrains();
//            			break;
//            		}
//            		else{
//            			System.out.println("Invalid Response, please choose correct keys");
//            		}
//            	case 11:
//            		if(admin){
//            			addTrains();
//            			break;
//            		}
//            		else{
//            			System.out.println("Invalid Response, please choose correct keys");
//            		}
//            	case 12:
//            		userLogout();
//                    return;
//            	default:
//            		System.out.println("Invalid Response, please choose correct keys");
//		}
//		
//	}
}

