package view;

import java.util.Scanner;

import model.User;
import model.UserDao;


public class UserView {
	
	private static Scanner sc = new Scanner(System.in);
	private UserDao dao = UserDao.getInstance();
	private static Boolean admin=false;
	private ShowMainMenuView show = new ShowMainMenuView();
	
	
	public void RegisterUser() {
		dao.createConnection();
		dao.createUserTable();
		  System.out.println("Register a New User");
	      System.out.print("Enter your name: ");
	      String name = sc.nextLine();
	      System.out.print("Enter your email: ");
	      String email = sc.nextLine();
	      System.out.println("Enter your password:");
	      String password = sc.nextLine();
	      
	      //check user is already present or not
	      User user = dao.findUser(email,password);
	      //System.out.println(user.getName());
	      if(user!=null) {
	    	  System.out.println("User is already exist, please Login.");
	    	  LoginUser();
	    	  
	      }
	      //add user
	      int newUser = dao.addUser(name, email, password);
	      if(newUser>0) {
	    	  System.out.println("User Add Successfully.");
	    	  System.out.println("Please Loagin, now...");
//	    	  show.ShowMainMenu(admin);
	    	  LoginUser();
	      }
	      dao.closeConnection();
	}
	

	public void LoginUser() {
		dao.createConnection();
		System.out.println("Enter your email");
		String email = sc.nextLine();
		System.out.println("Enter your password");
		String password = sc.nextLine();
		
		User user = dao.findUser(email, password);
		if(user==null){
			System.out.println("User Email or Password is wrong.");
			return;
		}
		if(user.isAdmin()) {
			admin = true;
		}
		System.out.println("Login successful! Welcome, " +user.getName());
		show.ShowMainMenu(user);
		dao.closeConnection();
	}
}
