/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.airline;

/**
 *
 * @author Kou yang
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CreateAccountFrame extends JFrame {
    private JTextField usernameField, fullNameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton createAccountButton, backButton;

    public CreateAccountFrame() {
        setTitle("Create Account");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();

        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });

        backButton = new JButton("Back to Login");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnToLogin();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(fullNameLabel);
        add(fullNameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(createAccountButton);
        add(backButton);

        setVisible(true);
    }

    private void createAccount() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if username already exists
            String checkUserSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insert new user into the database
            String insertUserSql = "INSERT INTO users (username, password, full_name, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertUserSql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // In a real system, you'd hash this
                stmt.setString(3, fullName);
                stmt.setString(4, email);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    returnToLogin();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while creating the account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnToLogin() {
        // Dispose the current frame and open the login frame
        dispose();
        new LoginFrame(); // Ensure you have the LoginFrame class
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
            java.util.logging.Logger.getLogger(CreateAccountFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateAccountFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateAccountFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateAccountFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateAccountFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
