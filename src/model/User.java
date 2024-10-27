package model;


public class User {
	private int id;
    private String email;
    private String password;
    private String name;
    private boolean admin = false; // Assuming there's an 'admin' field

    // Constructors
    public User() {
    }

    public User(String email, String password, String name, boolean admin) {
    	
        this.email = email;
        this.password = password;
        this.name = name;
        this.admin = admin;
    }

    // Getters and setters
    public void setId(int id) {
    	this.id = id;
    }
    public int getId() {
    	return id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

