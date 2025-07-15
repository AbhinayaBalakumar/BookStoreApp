package com.example.bookstoreapp;

import java.util.ArrayList;

public class Owner {
    /*Owner (Singleton)
Manage Books (includes view book list)
Add Book
Delete Book
Manage Customers (includes view customer list)
Register Customer
Remove Customer
*/
    private ArrayList<String> books = new ArrayList<String>();
    private ArrayList<String> customers = new ArrayList<String>();
    public Owner(){

    }
    public void addBook(double p, String n){
        Book b = new Book(n,p);
        books.add(b.toString());
    }
    public void removeBook(double p, String n){
        Book b = new Book(n,p);
        books.remove(b.toString());
    }
    public void manageCustomers(){

    }
    public void removeCustomers(String u, String p, int point){
        Customer c = new Customer(u,p,point);
        customers.remove(c.toString());
    }
    public void addCustomer(String u, String p, int point){
        Customer c = new Customer(u,p,point);
        customers.add(c.toString());
    }
    public ArrayList<String> getBooks(){
        return books;
    }

    public ArrayList<String> getCustomers(){
        return customers;
    }

}
