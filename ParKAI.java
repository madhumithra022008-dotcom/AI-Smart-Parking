import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

public class ParkAI extends Frame implements ActionListener {

    // =========================
    // FILES
    // =========================

    static final String PARKING_FILE = "parking.csv";
    static final String USERS_FILE = "users.csv";
    static final String RESERVATION_FILE = "reservations.csv";

    // =========================
    // AWT COMPONENTS
    // =========================

    CardLayout cardLayout;
    Panel mainPanel;

    // Login
    TextField loginUsername;
    TextField loginPassword;

    // Register
    TextField registerUsername;
    TextField registerPassword;

    // Dashboard
    Label welcomeLabel;

    // =========================
    // CURRENT USER
    // =========================

    String currentUser = "";

    // =========================
    // CONSTRUCTOR
    // =========================

    public ParkAI() {

        setTitle("PARKAI - Smart Parking Demand Prediction");
        setSize(950, 650);
        setLocationRelativeTo(null);

        createFiles();

        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createAdminPanel(), "ADMIN");

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        cardLayout.show(mainPanel, "LOGIN");

        setVisible(true);
    }

    // =========================
    // CREATE FILES
    // =========================

    void createFiles() {

        try {

            File parkingFile = new File(PARKING_FILE);

            if (!parkingFile.exists()) {

                PrintWriter pw = new PrintWriter(new FileWriter(parkingFile));

                pw.println("id,name,location,totalSpaces,availableSpaces,pricePerHour");

                pw.println("1,Central Mall Parking,Salem Tamil Nadu,500,120,50");
                pw.println("2,City Hospital Parking,Salem Tamil Nadu,300,85,30");
                pw.println("3,Metro Station Parking,Chennai Tamil Nadu,400,250,20");
                pw.println("4,Tech Park Parking,Bengaluru Karnataka,700,90,60");
                pw.println("5,City Stadium Parking,Coimbatore Tamil Nadu,600,45,40");
                pw.println("6,Airport Parking,Chennai Airport Tamil Nadu,800,300,80");
                pw.println("7,Railway Station Parking,Salem Railway Station Tamil Nadu,450,100,40");
                pw.println("8,IT Park Parking,Coimbatore Tamil Nadu,550,180,50");

                pw.close();
            }

            File userFile = new File(USERS_FILE);

            if (!userFile.exists()) {

                PrintWriter pw = new PrintWriter(new FileWriter(userFile));

                pw.println("username,password");
                pw.println("admin,admin123");

                pw.close();
            }

            File reservationFile = new File(RESERVATION_FILE);

            if (!reservationFile.exists()) {

                PrintWriter pw = new PrintWriter(new FileWriter(reservationFile));

                pw.println(
                    "bookingId,username,customerName,parkingId,parkingName,date,time,riskScore,riskLevel,status"
                );

                pw.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // LOGIN PANEL
    // =========================

    Panel createLoginPanel() {

        Panel panel = new Panel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label title = new Label(
            "PARKAI - Smart Parking Demand Prediction",
            Label.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        loginUsername = new TextField(20);
        loginPassword = new TextField(20);
        loginPassword.setEchoChar('*');

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Create Account");

        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e ->
            cardLayout.show(mainPanel, "REGISTER")
        );

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new Label("Username:"), gbc);

        gbc.gridx = 1;

        panel.add(loginUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new Label("Password:"), gbc);

        gbc.gridx = 1;

        panel.add(loginPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(loginButton, gbc);

        gbc.gridx = 1;

        panel.add(registerButton, gbc);

        return panel;
    }

    // =========================
    // REGISTER PANEL
    // =========================

    Panel createRegisterPanel() {

        Panel panel = new Panel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label title = new Label(
            "Create PARKAI Account",
            Label.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        registerUsername = new TextField(20);
        registerPassword = new TextField(20);

        registerPassword.setEchoChar('*');

        Button createButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        createButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e ->
            cardLayout.show(mainPanel, "LOGIN")
        );

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new Label("Username:"), gbc);

        gbc.gridx = 1;

        panel.add(registerUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new Label("Password:"), gbc);

        gbc.gridx = 1;

        panel.add(registerPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(createButton, gbc);

        gbc.gridx = 1;

        panel.add(backButton, gbc);

        return panel;
    }

    // =========================
    // DASHBOARD
    // =========================

    Panel createDashboardPanel() {

        Panel panel = new Panel(new BorderLayout());

        Panel top = new Panel(new FlowLayout());

        welcomeLabel = new Label("Welcome");

        welcomeLabel.setFont(
            new Font("Arial", Font.BOLD, 18)
        );

        top.add(welcomeLabel);

        panel.add(top, BorderLayout.NORTH);

        Panel buttons = new Panel(new GridLayout(5, 3, 10, 10));

        Button searchButton =
            new Button("Search Parking");

        Button allParkingButton =
            new Button("View All Parking");

        Button predictionButton =
            new Button("AI Demand Prediction");

        Button reserveButton =
            new Button("Reserve Parking");

        Button historyButton =
            new Button("Booking History");

        Button cancelButton =
            new Button("Cancel Reservation");

        Button profileButton =
            new Button("My Profile");

        Button statsButton =
            new Button("Parking Statistics");

        Button mapsButton =
            new Button("Google Maps");

        Button logoutButton =
            new Button("Logout");

        searchButton.addActionListener(e -> searchParking());

        allParkingButton.addActionListener(e ->
            showAllParking()
        );

        predictionButton.addActionListener(e ->
            demandPrediction()
        );

        reserveButton.addActionListener(e ->
            reserveParking()
        );

        historyButton.addActionListener(e ->
            bookingHistory()
        );

        cancelButton.addActionListener(e ->
            cancelReservation()
        );

        profileButton.addActionListener(e ->
            showProfile()
        );

        statsButton.addActionListener(e ->
            showStatistics()
        );

        mapsButton.addActionListener(e ->
            googleMapsMenu()
        );

        logoutButton.addActionListener(e -> logout());

        buttons.add(searchButton);
        buttons.add(allParkingButton);
        buttons.add(predictionButton);

        buttons.add(reserveButton);
        buttons.add(historyButton);
        buttons.add(cancelButton);

        buttons.add(profileButton);
        buttons.add(statsButton);
        buttons.add(mapsButton);

        buttons.add(logoutButton);

        panel.add(buttons, BorderLayout.CENTER);

        return panel;
    }

    // =========================
    // ADMIN PANEL
    // =========================

    Panel createAdminPanel() {

        Panel panel = new Panel(new BorderLayout());

        Label title =
            new Label(
                "PARKAI ADMIN PANEL",
                Label.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        panel.add(title, BorderLayout.NORTH);

        Panel buttons =
            new Panel(new GridLayout(3, 2, 10, 10));

        Button parkingButton =
            new Button("Parking Overview");

        Button reservationButton =
            new Button("All Reservations");

        Button usersButton =
            new Button("All Users");

        Button statisticsButton =
            new Button("System Statistics");

        Button mapsButton =
            new Button("Google Maps Parking");

        Button logoutButton =
            new Button("Logout");

        parkingButton.addActionListener(e ->
            adminParkingOverview()
        );

        reservationButton.addActionListener(e ->
            adminReservations()
        );

        usersButton.addActionListener(e ->
            adminUsers()
        );

        statisticsButton.addActionListener(e ->
            adminStatistics()
        );

        mapsButton.addActionListener(e ->
            googleMapsMenu()
        );

        logoutButton.addActionListener(e ->
            logout()
        );

        buttons.add(parkingButton);
        buttons.add(reservationButton);
        buttons.add(usersButton);
        buttons.add(statisticsButton);
        buttons.add(mapsButton);
        buttons.add(logoutButton);

        panel.add(buttons, BorderLayout.CENTER);

        return panel;
    }

    // =========================
    // LOGIN
    // =========================

    void loginUser() {

        String username =
            loginUsername.getText().trim();

        String password =
            loginPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {

            showMessage(
                "Please enter username and password."
            );

            return;
        }

        if (username.equals("admin")
            && password.equals("admin123")) {

            currentUser = "admin";

            cardLayout.show(mainPanel, "ADMIN");

            return;
        }

        try {

            BufferedReader br =
                new BufferedReader(
                    new FileReader(USERS_FILE)
                );

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 2) {

                    if (data[0].equals(username)
                        && data[1].equals(password)) {

                        currentUser = username;

                        welcomeLabel.setText(
                            "Welcome, " + currentUser
                        );

                        cardLayout.show(
                            mainPanel,
                            "DASHBOARD"
                        );

                        br.close();

                        return;
                    }
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        showMessage("Invalid username or password.");
    }

    // =========================
    // REGISTER
    // =========================

    void registerUser() {

        String username =
            registerUsername.getText().trim();

        String password =
            registerPassword.getText().trim();

        if (username.isEmpty()
            || password.isEmpty()) {

            showMessage(
                "Please enter username and password."
            );

            return;
        }

        if (username.equalsIgnoreCase("admin")) {

            showMessage(
                "This username is reserved."
            );

            return;
        }

        try {

            BufferedReader br =
                new BufferedReader(
                    new FileReader(USERS_FILE)
                );

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 1
                    && data[0].equals(username)) {

                    br.close();

                    showMessage(
                        "Username already exists."
                    );

                    return;
                }
            }

            br.close();

            PrintWriter pw =
                new PrintWriter(
                    new FileWriter(
                        USERS_FILE,
                        true
                    )
                );

            pw.println(
                username + "," + password
            );

            pw.close();

            showMessage(
                "Account created successfully!"
            );

            registerUsername.setText("");
            registerPassword.setText("");

            cardLayout.show(mainPanel, "LOGIN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // PARKING DATA
    // =========================

    ArrayList<Parking> getParkingList() {

        ArrayList<Parking> list =
            new ArrayList<>();

        try {

            BufferedReader br =
                new BufferedReader(
                    new FileReader(PARKING_FILE)
                );

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data =
                    line.split(",");

                if (data.length >= 6) {

                    Parking p = new Parking();

                    p.id =
                        Integer.parseInt(data[0]);

                    p.name = data[1];

                    p.location = data[2];

                    p.totalSpaces =
                        Integer.parseInt(data[3]);

                    p.availableSpaces =
                        Integer.parseInt(data[4]);

                    p.pricePerHour =
                        Integer.parseInt(data[5]);

                    list.add(p);
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // SEARCH PARKING
    // =========================

    void searchParking() {

        String keyword =
            JOptionPane.showInputDialog(
                this,
                "Enter parking name or city/location:"
            );

        if (keyword == null
            || keyword.trim().isEmpty()) {
            return;
        }

        keyword =
            keyword.toLowerCase();

        StringBuilder result =
            new StringBuilder();

        boolean found = false;

        for (Parking p : getParkingList()) {

            if (p.name.toLowerCase().contains(keyword)
                || p.location.toLowerCase().contains(keyword)) {

                result.append(
                    "ID: " + p.id + "\n"
                );

                result.append(
                    "Parking: " + p.name + "\n"
                );

                result.append(
                    "Location: " + p.location + "\n"
                );

                result.append(
                    "Available: "
                    + p.availableSpaces
                    + " / "
                    + p.totalSpaces
                    + "\n"
                );

                result.append(
                    "Price: Rs."
                    + p.pricePerHour
                    + " per hour\n"
                );

                result.append(
                    "--------------------------\n"
                );

                found = true;
            }
        }

        if (!found) {

            showMessage(
                "No parking found."
            );

            return;
        }

        showText(
            "Search Results",
            result.toString()
        );
    }

    // =========================
    // SHOW ALL PARKING
    // =========================

    void showAllParking() {

        StringBuilder result =
            new StringBuilder();

        for (Parking p : getParkingList()) {

            result.append(
                "ID: " + p.id + "\n"
            );

            result.append(
                "Parking: " + p.name + "\n"
            );

            result.append(
                "Location: " + p.location + "\n"
            );

            result.append(
                "Available: "
                + p.availableSpaces
                + " / "
                + p.totalSpaces
                + "\n"
            );

            result.append(
                "Price: Rs."
                + p.pricePerHour
                + "/hour\n"
            );

            result.append(
                "--------------------------\n"
            );
        }

        showText(
            "All Parking Locations",
            result.toString()
        );
    }

    // =========================
    // AI DEMAND PREDICTION
    // =========================

    void demandPrediction() {

        ArrayList<Parking> list =
            getParkingList();

        if (list.isEmpty()) {

            showMessage(
                "No parking data available."
            );

            return;
        }

        Choice choice =
            new Choice();

        for (Parking p : list) {
            choice.add(
                p.id + " - " + p.name
            );
        }

        TextField hour =
            new TextField("18");

        Checkbox event =
            new Checkbox(
                "Nearby event"
            );

        Checkbox rain =
            new Checkbox(
                "Rain"
            );

        Checkbox traffic =
            new Checkbox(
                "Heavy traffic"
            );

        Panel panel =
            new Panel(new GridLayout(5, 2, 5, 5));

        panel.add(
            new Label("Parking:")
        );

        panel.add(choice);

        panel.add(
            new Label("Arrival hour (0-23):")
        );

        panel.add(hour);

        panel.add(
            new Label("Event:")
        );

        panel.add(event);

        panel.add(
            new Label("Rain:")
        );

        panel.add(rain);

        panel.add(
            new Label("Traffic:")
        );

        panel.add(traffic);