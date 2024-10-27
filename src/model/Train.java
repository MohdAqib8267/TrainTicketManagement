package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Train {
	private int id;
	private String name;
    private String source;
    private String destination;
    private String arrivalTime;
    private String departureTime;
    private Map<String,List<Coach>>coachMap = new HashMap<>();
    
    public Train() {}
    
    public Train(String name, String source, String destination, String arrivalTime,String departureTime, Map<String,List<Coach>>coachMap) {
    	this.name = name;
    	this.source = source;
    	this.destination = destination;
    	this.arrivalTime = arrivalTime;
    	this.departureTime = departureTime;
        this.coachMap = coachMap;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public Map<String,List<Coach>> getCoachMap() {
        return coachMap;
    }
    
   
    public void displayCoaches() {
		coachMap.forEach((type, coaches) -> {
			for (Coach coach : coaches) {
				System.out.println("Coach Name: " + coach.getCoachName() +
						", Coach Type: " + coach.getCoachType() +
						", Total Seats: " + coach.getSeatCount() +
						", Available Seats: " + coach.getAvailableSeats());
			}
		});
    }
}
