package com.example.bookstoreapp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class HelloApplication extends Application {
    private ArrayList<String> accounts = new ArrayList<String>();
    private ArrayList<String> books = new ArrayList<String>();
    Customer currentCustomer = null;
    Book book = null;
    private static int g = 0;
    public void getInput() throws FileNotFoundException {
        Scanner fileInput = new Scanner(new File("C:\\Users\\abhin\\IdeaProjects\\BookStoreApp\\src\\main\\java\\com\\example\\bookstoreapp\\loginFile.txt"));
        Scanner fileInput2 = new Scanner(new File("C:\\Users\\abhin\\IdeaProjects\\BookStoreApp\\src\\main\\java\\com\\example\\bookstoreapp\\booksFile.txt"));
        while (fileInput.hasNextLine()) {
            accounts.add(fileInput.nextLine());
        }
        int i=0;
        while (fileInput2.hasNextLine()) {
            books.add(fileInput2.nextLine());
            String b = books.get(i);
            b = b.substring(1, b.length() - 1);
            String[] l = b.split(", ");
            dataB.add(new Report(l[0], l[1]));
            i++;
        }
        System.out.println("it works 1");
        fileInput.close();
    }
    public void getOutput() {
        try {
            System.out.println("it works 2");

            PrintWriter lw = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\abhin\\IdeaProjects\\BookStoreApp\\src\\main\\java\\com\\example\\bookstoreapp\\loginFile.txt")));
            PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\abhin\\IdeaProjects\\BookStoreApp\\src\\main\\java\\com\\example\\bookstoreapp\\booksFile.txt")));
            for (String each : accounts) {
                lw.println(each);
            }
            for (String each : books) {
                bw.println(each);
            }
            lw.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("This is broken!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Label l = new Label("Please click a box");
    private final ObservableList<Report> dataB = FXCollections.observableArrayList();
    private Button button = new Button("Buy");
    private Button redeemButton = new Button("Redeem points and Buy");
    public void check(ActionEvent event2) {
        l.setText(g+"");
    }
    private Stage primaryStage;
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            getInput();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(showLoginScreen());
        primaryStage.setTitle("Bookstore App");
        primaryStage.show();
    }
    boolean isOwner;
    boolean authorize = false;
    public void verify(TextField card2, TextField pass2) {
        boolean theSame = false;
        String card = card2.getText();
        String pass = pass2.getText();


        for (int i = 0; i < accounts.size(); i++) {
            String fileUserName = accounts.get(i);
            fileUserName = fileUserName.substring(1, fileUserName.length() - 1);
            String[] list = fileUserName.split(", ");
            if (list[0].equals(card) && list[1].equals(pass)) {
                System.out.println("it works verify"+accounts.get(i));
                theSame = true;
                int p = Integer.parseInt(list[2]);

                currentCustomer = new Customer(card, pass, p);
                i = accounts.size();
            }
        }
        authorize = theSame;
    }
    private Scene showLoginScreen() {
        Label titleLabel = new Label("Book Store Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Label statusLabel = new Label();
        Stage next = new Stage();

        VBox vbox = new VBox(10, titleLabel, usernameField, passwordField, loginButton, statusLabel);
        vbox.setAlignment(Pos.CENTER);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check for owner login
            if ("admin".equals(username) && "admin".equals(password)) {
                isOwner = true;
                next.setScene(showOwnerStartScreen());
                primaryStage.close();
                primaryStage = next;
                primaryStage.show();
            }
            // Check for customer login
            else {
                verify(usernameField, passwordField);
                if (authorize) {
                    System.out.println("it works logIn");
                    next.setScene(showCustomerStartScreen());
                    primaryStage.close();
                    primaryStage = next;
                    primaryStage.show();
                } else {
                    statusLabel.setText("Invalid username or password");
                }
            }
        });

        return new Scene(vbox, 300, 200);
    }
    private CheckBox bookCheck;
    public Scene showCustomerStartScreen() {
        System.out.println("start");
        Scene scene = new Scene(new Group(), 500, 600);
        final Label label = new Label("Welcome "+currentCustomer.getUsername()+". You have "+currentCustomer.getPoints()+" points. Your status is "+currentCustomer.getStatus());

        // Create Table
        TableView<Report> table = new TableView<>(dataB);
        table.setPrefHeight(450);
        table.setPrefWidth(500);

        // Column 1: Books
        TableColumn<Report, String> bookCol = new TableColumn<>("Books");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("handle"));

        // Column 2: Prices
        TableColumn<Report, String> priceCol = new TableColumn<>("Prices");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("handle2"));

        // Column 3: CheckBox
        TableColumn<Report, CheckBox> checkCol = new TableColumn<>("Select");
        checkCol.setCellValueFactory(new PropertyValueFactory<>("bookCheck"));
        table.getColumns().addAll(bookCol, priceCol, checkCol);

        // Layout
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(label, table, button, redeemButton, l);


        // Button Actions
        button.setOnAction(e -> {
            int totalCost = g;
            currentCustomer.addPoints(totalCost * 10); // Earn 10 points per CAD
            Stage stage = new Stage();
            stage.setScene(customerCostScene(totalCost, false));
            primaryStage.close();
            primaryStage = stage;
            primaryStage.show();
        });

        redeemButton.setOnAction(e -> {
            int totalCost = g;
            int redeemedAmount = Math.min(currentCustomer.getPoints() / 100, totalCost); // Redeem up to total cost
            totalCost -= redeemedAmount;
            currentCustomer.deductPoints(redeemedAmount * 100); // Deduct points used
            currentCustomer.addPoints(totalCost * 10); // Earn points on remaining cost
            Stage stage = new Stage();
            stage.setScene(customerCostScene(totalCost, true));
            primaryStage.close();
            primaryStage = stage;
            primaryStage.show();
        });


        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        return scene;
    }
    public Scene customerCostScene(int totalCost, boolean redeemed) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        // Total Cost label
        Label totalCostLabel = new Label("Total Cost: " + totalCost);

        // Points and Status label
        Label pointsStatusLabel = new Label("Points: " + currentCustomer.getPoints() + ", Status: " + currentCustomer.getStatus());

        // Logout button
        Button log = new Button("Logout");
        Stage stage = new Stage();
        log.setOnAction(e -> {
            getOutput();
            stage.setScene(showLoginScreen());
            primaryStage.close();
            for (Report report : dataB) {
                report.getBookCheck().setSelected(false);
            }
            g=0;n=0;
            primaryStage = stage;
            primaryStage.show();});
        vbox.getChildren().addAll(totalCostLabel, pointsStatusLabel, log);
        return new Scene(vbox, 300, 200);
    }
    private Scene showOwnerStartScreen() {
        Label welcomeLabel = new Label("Welcome Owner");
        Button booksButton = new Button("Manage Books");
        Button customersButton = new Button("Manage Customers");
        Button logoutButton = new Button("Logout");
        Stage stage = new Stage();
        currentCustomer = new Customer("admin", "admin", 0);
        VBox vbox = new VBox(10, welcomeLabel, booksButton, customersButton, logoutButton);
        vbox.setAlignment(Pos.CENTER);


        booksButton.setOnAction(e -> {
            stage.setScene(showOwnerBooksScreen());
            primaryStage.close();
            primaryStage = stage;
            primaryStage.show();
        });
        customersButton.setOnAction(e -> {
            stage.setScene(showOwnerCustomersScreen());
            primaryStage.close();
            primaryStage = stage;
            primaryStage.show();
        });
        logoutButton.setOnAction(e -> {
            getOutput();
            isOwner = false;
            stage.setScene(showLoginScreen());
            primaryStage.close();
            primaryStage = stage;
            primaryStage.show();
        });
        return new Scene(vbox, 300, 200);
    }
    private Scene showOwnerBooksScreen() {
        // Book table
        TableView<Book> table = new TableView<>();
        ObservableList<Book> data = FXCollections.observableArrayList();

        for (String book : books) {
            book = book.substring(1, book.length() - 1);
            String[] parts = book.split(", ");
            data.add(new Book(parts[0], Integer.parseInt(parts[1])));
        }

        TableColumn<Book, String> nameCol = new TableColumn<>("Book Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, Double> priceCol = new TableColumn<>("Book Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(nameCol, priceCol);
        table.setItems(data);

        // Add book controls
        TextField nameField = new TextField();
        nameField.setPromptText("Book Name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        Button addButton = new Button("Add");

        HBox addBox = new HBox(10, nameField, priceField, addButton);

        // Action buttons
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");


        HBox actionBox = new HBox(10, deleteButton, backButton);
        VBox vbox = new VBox(10, table, addBox, actionBox);

        Stage back = new Stage();
        addButton.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && !priceField.getText().isEmpty()) {
                try {
                    int bookPrice = Integer.parseInt(priceField.getText());
                    boolean exists = false;
                    for (Book b : data) {
                        if (b.getName().equalsIgnoreCase(nameField.getText())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        data.add(new Book(nameField.getText(), bookPrice));
                        books.add("["+nameField.getText() + ", " + bookPrice+"]");
                        nameField.clear();
                        priceField.clear();
                    }
                } catch (NumberFormatException ex) {
                    // Invalid price
                }
            }
        });


        deleteButton.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                data.remove(selected);
                books.removeIf(b -> b.startsWith("["+selected.getName() + ", "));
            }
        });


        backButton.setOnAction(e -> {
            back.setScene(showOwnerStartScreen());
            primaryStage.close();
            primaryStage = back;
            primaryStage.show();
        });


        return new Scene(vbox, 400, 500);
    }
    private Scene showOwnerCustomersScreen() {
        // Customer table
        TableView<Customer> table = new TableView<>();
        ObservableList<Customer> data = FXCollections.observableArrayList();


        for (String account : accounts) {
            account = account.substring(1, account.length() - 1);
            String[] parts = account.split(", ");
            data.add(new Customer(parts[0], parts[1], Integer.parseInt(parts[2])));
        }


        TableColumn<Customer, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));


        TableColumn<Customer, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));


        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));


        table.getColumns().addAll(userCol, passCol, pointsCol);
        table.setItems(data);


        // Add customer controls
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        Button addButton = new Button("Add");

        HBox addBox = new HBox(10, usernameField, passwordField, addButton);
        Stage back = new Stage();

        // Action buttons
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");


        HBox actionBox = new HBox(10, deleteButton, backButton);
        VBox vbox = new VBox(10, table, addBox, actionBox);


        addButton.setOnAction(e -> {
            if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                Customer c = new Customer(usernameField.getText(), passwordField.getText(), 0);
                data.add(c);
                accounts.add(c.toString());
                usernameField.clear();
                passwordField.clear();
            }
        });


        deleteButton.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                data.remove(selected);
                accounts.removeIf(a -> a.startsWith("["+selected.getUsername() + ","));
            }
        });


        backButton.setOnAction(e ->{
            back.setScene(showOwnerStartScreen());
            primaryStage.close();
            primaryStage = back;
            primaryStage.show();
        });


        return new Scene(vbox, 300, 200);
    }
    private static int n = 0;
    public static void num() {
        n += 1;
    }
    public static void subNum() {
        n -= 1;
    }
    public static void grade(int gr) {
        g = g + gr;
    }
    public static void subGrade(int gr) {
        g = g - gr;
    }
    public static class Report {
        private String handle = "";
        private String handle2 = "";
        private CheckBox bookCheck = new CheckBox();
        private int count=0;
        private ToggleGroup tg = new ToggleGroup();
        private VBox rb = new VBox();

        private Report(String t, String p) {
            handle = t;
            handle2 = p;
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if (bookCheck.isSelected()) {
                        grade(Integer.parseInt(handle2));
                        num();
                        count++;
                    } else if (!bookCheck.isSelected()) {
                        if (count > 0) {
                            subGrade(Integer.parseInt(handle2));
                            subNum();
                        } else {
                            count = 0;
                        }}}};
            bookCheck.setOnAction(event);
        }
        public CheckBox getBookCheck(){return bookCheck;}
        public String getHandle() {
            return handle;
        }


        public String getHandle2() {
            return handle2;
        }
    }
    public static void main(String[] args) {
        launch();
    }
}

