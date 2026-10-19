/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationandlogin;

import java.util.regex.Pattern;

/**
 * Login class handling user registration, credential validation, 
 * cell phone verification, and login status messaging.
 */
public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Method to check username: must contain an underscore ('_') and be no more than 5 characters long
    public boolean checkUserName(String username) {
        if (username != null && username.contains("_") && username.length() <= 5) {
            return true;
        }
        return false;
    }

    // Method to check password complexity: >= 8 chars, contains a capital letter, a number, and a special character
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Method to check cell phone number formatting.
     * Ensures it starts with '+' and has a maximum length of 10 characters.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+\\d{1,9}$";
        return Pattern.matches(regex, cellPhoneNumber) && cellPhoneNumber.length() <= 10;
    }

    // Register user method incorporating username, password, cell phone, and names
    public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        
        return "User registered successfully. Cell phone number successfully added.";
    }

    // Verify login credentials
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername != null && enteredUsername.equals(this.username) &&
               enteredPassword != null && enteredPassword.equals(this.password);
    }

    // Return status message upon login attempt
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}