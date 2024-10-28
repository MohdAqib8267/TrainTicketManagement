package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TrainDao {
    static final String url = "jdbc:mysql://localhost:3306/trainTicketBooking";
    static final String user = "root";
    static final String password = "Aqib8267@";
    private static Connection conn=null;
    private static TrainDao instance;
    
    public static TrainDao getInstance() {
    	if(conn==null) {
    		instance = new TrainDao();
    	}
    	return instance;
    }
	public void createConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error in connection:"+e);
		}
	}
	public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e);
        }
    }
	public void createTables() {
		String createTrainTable = """
				CREATE TABLE IF NOT EXISTS Train(
					id INT AUTO_INCREMENT PRIMARY KEY,
					name VARCHAR(50) NOT NULL,
					source VARCHAR(50) NOT NULL,
					destination VARCHAR(50) NOT NULL,
					arrivalTime VARCHAR(50),
					departureTime VARCHAR(20)
				);
				""";
		String createCoach = """
				CREATE TABLE IF NOT EXISTS Coach(
					id INT AUTO_INCREMENT PRIMARY KEY,
					train_id INT,
					coachType VARCHAR(20),
					coachName VARCHAR(10),
					seatCount INT,
					availableSeats INT,
					FOREIGN KEY (train_id) REFERENCES Train(id) ON DELETE CASCADE
				);
				""";
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return;
			}
			Statement st = conn.createStatement();
			st.execute(createTrainTable);
			st.execute(createCoach);
			System.out.println("Table is created.");
			
		} catch (SQLException e) {
			
			System.out.println("Error creating tables: " + e);
		}
		
	}
	
	public void addTrainToDB(Train train) {
		String insertTrain = """
				insert into Train(name,source,destination,arrivalTime,departureTime)
				values (?,?,?,?,?);
				""";
		String insertCoach = """
				insert into Coach(train_id,coachType,coachName,seatCount,availableSeats)
				values (?,?,?,?,?);
				""";
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return;
			}
			PreparedStatement stTrain = conn.prepareStatement(insertTrain, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement stCoach = conn.prepareStatement(insertCoach);
			
			stTrain.setString(1, train.getName());
			stTrain.setString(2, train.getSource());
			stTrain.setString(3, train.getDestination());
			stTrain.setString(4, train.getArrivalTime());
			stTrain.setString(5, train.getDepartureTime());
			int count = stTrain.executeUpdate();
			// Get the generated train_id
			ResultSet rs = stTrain.getGeneratedKeys(); //retrieve only auto generated column and assign it to ResultSet table
			if(rs.next()) {
				int train_id = rs.getInt(1);
				
				for(Map.Entry<String,List<Coach>>entry:train.getCoachMap().entrySet()) {
					for(Coach c:entry.getValue()) {
						stCoach.setInt(1, train_id);
						stCoach.setString(2, c.getCoachType());
						stCoach.setString(3, c.getCoachName());
						stCoach.setInt(4, c.getSeatCount());
						stCoach.setInt(5, c.getAvailableSeats());
						stCoach.executeUpdate();
					}
				}
			}
			System.out.println("Train and its coaches added successfully to the System.");
			
		} catch (SQLException e) {
			System.out.println("Error creating tables: " + e);
		}
	}
	
	public ResultSet findAllTrains() {
		String query = """
					select * from Train;
				""";
		ResultSet rs = null;
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return null;
			}
			Statement st = conn.createStatement();
			rs = st.executeQuery(query);
			
		} catch (SQLException e) {
			
			System.out.println("Error in Fetching All trains "+e);
		   
		}
		return rs;
	}
	//find coaches for a train
	public ResultSet findCoach(int id) {
		String query = """
				select * from Coach 
				where train_id=(?);
				""";
		ResultSet rs = null;
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, id);
			rs = st.executeQuery();
		} catch (SQLException e) {
			System.out.println("Error in fetching coaches: "+e);
		}
		return rs;
	}
	public ResultSet findTrains(String source, String destination, Integer trainId) {
	    String query;
	    ResultSet rs = null;
	    
	    try {
	        
	        if (trainId != null) {
	            query = "SELECT * FROM Train WHERE id = ?";
	            PreparedStatement st = conn.prepareStatement(query);
	            st.setInt(1, trainId);
	            rs = st.executeQuery();
	        } else if (source != null && destination != null) {
	            query = "SELECT * FROM Train WHERE source = ? AND destination = ?";
	            PreparedStatement st = conn.prepareStatement(query);
	            st.setString(1, source);
	            st.setString(2, destination);
	            rs = st.executeQuery();
	        } else {
	            System.out.println("Error: Insufficient data to search for trains.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in fetching trains: " + e);
	    }
	    return rs;
	}

	public void createBookingTable() {
		String bookingTable = """
                CREATE TABLE IF NOT EXISTS Booking (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    train_id INT NOT NULL,
                    userId INT NOT NULL,
                    coachType VARCHAR(20) NOT NULL,
                    coachName VARCHAR(10) NOT NULL,
                    seatNumber INT NOT NULL,
                    bookingDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status ENUM('ACTIVE', 'CANCELLED', 'COMPLETED') DEFAULT 'ACTIVE',
                    FOREIGN KEY (train_id) REFERENCES Train(id) ON DELETE CASCADE,
                    FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE
                );
            """;
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return;
			}
			Statement st = conn.createStatement();
			st.execute(bookingTable);
			System.out.println("Table is created.");
			
		} catch (SQLException e) {
			
			System.out.println("Error creating tables: " + e);
		}
	}
	
	public void booking(int userId,int train_id,String coachType,int noOfTickets) {
	
		
		String queryForFindCoachTypeStatus = """
				select * from coach
				where coachType=(?) AND train_id=(?)
				order by availableSeats ASC;
				""";
		String queryForBooking = """
				insert into Booking (train_id,userId,coachType,coachName,seatNumber)
				values (?,?,?,?,?); 
				""";
		try {
			PreparedStatement st = conn.prepareStatement(queryForFindCoachTypeStatus);
			st.setString(1, coachType);
			st.setInt(2, train_id);
			ResultSet rs = st.executeQuery();
			
			int ticketBooked = 0;
			while(rs.next() && ticketBooked<noOfTickets) {
				String coachName = rs.getString("coachName");
				int seatCount = rs.getInt("seatCount");
				int availableSeats = rs.getInt("availableSeats");
				
				//now check if seat is available on that perticular coach
				if(availableSeats>0) {
					for(int i=(seatCount-availableSeats+1);i<=seatCount && ticketBooked<noOfTickets;i++) {
						//start booking
						PreparedStatement stbook = conn.prepareStatement(queryForBooking);
						stbook.setInt(1, train_id);
						stbook.setInt(2, userId);
						stbook.setString(3, coachType);
						stbook.setString(4, coachName);
						stbook.setInt(5, i);
						
						int bookingResult = stbook.executeUpdate();
						if(bookingResult > 0) {
							ticketBooked++;
							System.out.println("Booked Seat " + i + " in Coach " + coachName);
							
							Booking booking = new Booking(train_id, userId, coachType, coachName, i);
						}
						//update available seats in coach 
						PreparedStatement stUpdate = conn.prepareStatement(
								"update Coach SET availableSeats=availableSeats-1 where train_id=? AND coachName=?;"
						);
						stUpdate.setInt(1, train_id);
						stUpdate.setString(2, coachName);
						stUpdate.executeUpdate();						
					}
				}
			}
			if (ticketBooked < noOfTickets) {
	            System.out.println("Not enough seats available in the requested coach type.");
	        } else {
	            System.out.println("All tickets successfully booked.");
	        }
		} catch (SQLException e) {
			System.out.println("Error in booking seats: "+e);
		}
		
	}
	public ResultSet viewTickets(int userId,String query) {
		ResultSet rs=null;
		
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return rs;
			}
			PreparedStatement st = conn.prepareStatement(query);
			
			rs = st.executeQuery();
			return rs;
		} catch (SQLException e) {
			System.out.println("Error in fetching bookings: "+e);
		}
		return rs;
	}
	public Boolean cancelTicket(int userId, int ticket_id) {
		
		String query = "update booking set status='CANCELLED' where userId=? and id=?;";
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return false;
			}
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, ticket_id);
			if(st.executeUpdate()>0) {
				return true;
			}
			
		} catch (SQLException e) {
			System.out.println("Error in Cancel bookings: "+e);
		}
		
		return false;
	}
	public ResultSet sortBasedOnPreference(String src, String dest, String preference) {
		ResultSet rs=null;
		if(src==null || dest==null || preference==null) {
			System.out.println("something is missing.");
		}
		String query = null;
		if(preference.equals("departureTime")) {
			query = "select * from train where source=? and destination=? order by departureTime";
		}
		else {
			System.out.println("Invalid preference");
		}
		
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return null;
			}
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, src);
			st.setString(2, dest);
			rs = st.executeQuery();
			return rs;
			
		} catch (SQLException e) {
			System.out.println("Error in Sorting Trains: "+e);
		}
		
		return rs;
	}
	
}
