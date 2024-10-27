package model;

import java.sql.*;

public class UserDao {
    static final String url = "jdbc:mysql://localhost:3306/trainTicketBooking";
    static final String user = "root";
    static final String password = "Aqib8267@";
    
    private Connection conn = null;
    private static UserDao instance;
    
    private UserDao() {
        createConnection();
    }
    // Singleton instance method
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }
    
    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Created");
        } catch (Exception e) {
            System.out.println("Error creating connection: " + e);
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
	public void createUserTable() {
		String userQuery = """
                CREATE TABLE User (
			    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
			    name VARCHAR(50) NOT NULL,
			    email VARCHAR(50) NOT NULL UNIQUE,
			    password VARCHAR(255) NOT NULL,
			    admin Boolean
			);
            """;
		try {
			if (conn == null || conn.isClosed()) {
			    System.out.println("Connection is not established.");
			    return; 
			}
			Statement st = conn.createStatement();
			st.execute(userQuery);
			System.out.println("Table is created.");
			
		} catch (SQLException e) {
			
			System.out.println("Error creating tables: " + e);
		}
	}
	
    public User findUser(String email, String password) {
        String query = "SELECT * FROM User WHERE email = ? AND password = ?";
        User user = null;

        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Connection is not established.");
                return null;
            }

            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setAdmin(rs.getBoolean("admin")); // Assuming there's an 'admin' field
            }

            st.close();
        } catch (Exception e) {
            System.out.println("Error finding user: " + e);
        }

        return user;
    }
		
		public int addUser(String name,String email,String password){
			String query = "insert into User (name, email, password, admin) values (?,?,?,?)";
			
			PreparedStatement st;
			try {
				st = conn.prepareStatement(query);
				st.setString(1, name);
				st.setString(2, email);
				st.setString(3, password);
				st.setBoolean(4, false);
				int count  = st.executeUpdate();
				return count;
			} catch (Exception e) {
				System.out.println(e);
			}
			return 0;
		}
}
