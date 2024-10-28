package view;

import java.util.Scanner;
import controller.TrainController;
import model.User;

public class ShowMainMenuView {
	
	private Scanner sc = new Scanner(System.in);
	private TrainController tc = new TrainController();
	
	public void ShowMainMenu(User user) {
		while(true){
			System.out.println("Main Menu:");
            System.out.println("1. Search for Trains");
            System.out.println("2. Book a Ticket");
            System.out.println("3. View Booking History");
            System.out.println("4. Sort Trains");
//            System.out.println("5. Configure System Settings");
            System.out.println("7. View Booking Details");
            System.out.println("8. Cancel a Booking");
            if(user.isAdmin()){
            	System.out.println("9. View all users with Ticket History.");
            	System.out.println("10. View All available Trains");
            	System.out.println("11. Add a Trains");
            }
            System.out.println("12. Logout");
            int option = sc.nextInt(); sc.nextLine();
            
            switch(option){
            	case 1:
            		tc.searchTrains();
            		break;
            	case 2:
            		
            		tc.bookTickets(user.getId());
            		break;
            	case 3:
            		tc.ViewBookings(user.getId());
            		break;
            	case 4:
            		tc.SortTrains(user.getId());
            		break;		
            	case 8:
            		tc.cancelBooking(user.getId());
            		break;
            		
            	case 9:
            		if(user.isAdmin()){
//            			viewAllUsersWithHistory();
                		break;
            		}
            		else{
            			System.out.println("Invalid Response, please choose correct keys");
            		}
            	case 10:
            		if(user.isAdmin()){
            			tc.viewAllAvailableTrains(user.isAdmin());
            			break;
            		}
            		else{
            			System.out.println("Invalid Response, please choose correct keys");
            		}
            	case 11:
            		if(user.isAdmin()){
            			tc.addTrain(user.isAdmin());
            			break;
            		}
            		else{
            			System.out.println("Invalid Response, please choose correct keys");
            		}
            	case 12:
//            		userLogout();
                    return;
            	default:
            		System.out.println("Invalid Response, please choose correct keys");
		}
	}
  }
}
