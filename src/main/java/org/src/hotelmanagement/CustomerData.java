package org.src.hotelmanagement;

import java.util.Date;

public class CustomerData {
    private String num;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Date checkInDate;
    private Date checkOutDate;
    private String room;

    public CustomerData(String num, String firstName, String lastName, String phone, String email, Date checkInDate, Date checkOutDate, String room) {
        this.num = num;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
    }

    public String getNum() {
        return num;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public String getRoom() {
        return room;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
