/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.airline;

/**
 *
 * @author Kou yang
 */

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ScheduleFlightFrame extends JFrame {
    private JComboBox<String> departureAirportDropdown;
    private JComboBox<String> arrivalAirportDropdown;
    private JDateChooser dateChooser;
    private int userId;

    public ScheduleFlightFrame(int userId) {
        this.userId = userId;
        setTitle("Schedule New Flight");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        departureAirportDropdown = new JComboBox<>();
        arrivalAirportDropdown = new JComboBox<>();
        dateChooser = new JDateChooser();
        dateChooser.setMinSelectableDate(new java.util.Date());

        populateAirports();

        add(new JLabel("Departure Airport:"));
        add(departureAirportDropdown);
        add(new JLabel("Arrival Airport:"));
        add(arrivalAirportDropdown);
        add(new JLabel("Departure Date:"));
        add(dateChooser);

        JButton submitButton = new JButton("Schedule Flight");
        submitButton.addActionListener(e -> scheduleFlight());
        add(submitButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new MainMenuFrame(userId);
        });
        add(backButton);

        setVisible(true);
    }

    private ScheduleFlightFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void populateAirports() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT iata_code FROM airports ORDER BY iata_code";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String iataCode = rs.getString("iata_code");
                    departureAirportDropdown.addItem(iataCode);
                    arrivalAirportDropdown.addItem(iataCode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void scheduleFlight() {
        String departureAirport = (String) departureAirportDropdown.getSelectedItem();
        String arrivalAirport = (String) arrivalAirportDropdown.getSelectedItem();
        java.util.Date date = dateChooser.getDate();

        // Check if the required fields are filled
        if (departureAirport == null || arrivalAirport == null || date == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ensure departure and arrival airports are different
        if (departureAirport.equals(arrivalAirport)) {
            JOptionPane.showMessageDialog(this, "Departure and arrival airports must be different.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the flight is already scheduled for this date
        try (Connection conn = DBConnection.getConnection()) {
            String checkSql = "SELECT * FROM scheduled_flights WHERE user_id = ? AND departure_date = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setDate(2, new java.sql.Date(date.getTime()));
                ResultSet rs = checkStmt.executeQuery();

                // If a flight already exists for the user on that date
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Flight already scheduled for this date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Generate a flight number using the current time
            String flightNumber = "FL" + System.currentTimeMillis();

            // Insert the flight into the scheduled_flights table
            String insertSql = "INSERT INTO scheduled_flights (user_id, departure_airport, arrival_airport, departure_date, flight_number) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, departureAirport);
                insertStmt.setString(3, arrivalAirport);
                insertStmt.setDate(4, new java.sql.Date(date.getTime()));
                insertStmt.setString(5, flightNumber);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Flight scheduled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainMenuFrame(userId); // Navigate to main menu (ensure this is valid)
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to schedule flight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScheduleFlightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleFlightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleFlightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleFlightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduleFlightFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
