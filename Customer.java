package com.example.bookstoreapp;

import java.util.ArrayList;

public class Customer {
    private String username;
    private String password;
    private int points;
    //private String name;

    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
        //this.name = name;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPoints() { return points; }

    public String getStatus() {
        return points >= 1000 ? "Gold" : "Silver";
    }

    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }

    public void deductPoints(int pointsToDeduct) {
        points = Math.max(0, points - pointsToDeduct);
    }
    public String toString(){
        ArrayList<String> l = new ArrayList<>();
        l.add(username);
        l.add(password);
        l.add(points+"");
        return l+"";
    }
//    public String getName(){return name;}
}
