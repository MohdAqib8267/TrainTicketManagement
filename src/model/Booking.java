package model;

import java.sql.Date;

public class Booking {
	private int id;
	private int train_id;
	private int userId;
	private String coachType;
	private String coachName;
	private int seatNumber;
	private Date bookingDate;
	private BookingStatus status;
	
	public Booking() {}
	
	public Booking(int train_id,int userId,String coachType,String coachName,int seatNumber) {

		this.train_id = train_id;
		this.userId = userId;
		this.coachType = coachType;
		this.coachName = coachName;
		this.seatNumber = seatNumber;
	}
	public int getBookingId() {
		return id;
	}
	public int getTrainId() {
		return train_id;
	}
	public int getUserId() {
		return userId;
	}
	public String getCoachType() {
		return coachType;
	}
	public String getCoachName() {
		return coachName;
	}
	public int getSeatNumber() {
		return seatNumber;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public BookingStatus getStatus() {
		return status;
	}
	
}
