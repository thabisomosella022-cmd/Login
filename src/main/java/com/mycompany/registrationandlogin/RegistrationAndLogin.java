/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationandlogin;

import java.util.Scanner;

public class RegistrationAndLogin {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("--- User Registration ---");
        System.out.print("Enter username (must contain '_' and max 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 chars, uppercase, number, special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter cell phone number (e.g., +27838968976): ");
        String cellPhoneNumber = scanner.nextLine();

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        // Register user with all required parameters
        String regMessage = loginSystem.registerUser(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(regMessage);

        // If registration was successful, test login flow
        if (regMessage.contains("successfully")) {
            System.out.println("\n--- User Login ---");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            boolean isLoggedIn = loginSystem.loginUser(loginUsername, loginPassword);
            String loginStatus = loginSystem.returnLoginStatus(isLoggedIn);
            System.out.println(loginStatus);
        }

        scanner.close();
    }
}
