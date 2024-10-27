package model;

public class Coach {
	private String coachName;
	private String coachType;
	private int seatCount;
	private int availableSeats;
	
	 public Coach() {}

	 public Coach(String coachType, String coachName, int seatCount, int availableSeats) {
	        this.coachType = coachType;
	        this.coachName = coachName;
	        this.seatCount = seatCount;
	        this.availableSeats = availableSeats;
	 }
	 public String getCoachType() {
		 return coachType;
	 }
	 public String getCoachName() {
		 return coachName;
	 }
	 public int getSeatCount() {
		 return seatCount;
	 }
	 public int getAvailableSeats() {
		 return availableSeats;
	 }
	 public boolean bookSeat() {
		 if(availableSeats>0) {
			 availableSeats--;
	         return true;
		 }
		 return false;
	 }
}
