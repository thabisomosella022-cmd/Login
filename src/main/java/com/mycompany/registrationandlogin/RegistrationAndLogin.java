/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationandlogin;

import java.util.Scanner;

public class RegistrationAndLogin {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();
        Message msgManager = new Message();

        System.out.println("========================================");
        System.out.println("   WELCOME TO THE REGISTRATION SYSTEM   ");
        System.out.println("========================================");

        boolean registrationSuccess = false;

        // --- PART 1: USER REGISTRATION LOOP ---
        while (!registrationSuccess) {
            System.out.print("Enter username (must contain '_' and max 5 characters): ");
            String username = scanner.nextLine();

            System.out.print("Enter password (min 8 chars, uppercase, number, special char): ");
            String password = scanner.nextLine();

            System.out.print("Enter cell phone number (e.g., +278389): ");
            String cellPhoneNumber = scanner.nextLine();

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            String regMessage = loginSystem.registerUser(username, password, cellPhoneNumber, firstName, lastName);
            System.out.println("\n" + regMessage + "\n");

            if (regMessage.contains("successfully")) {
                registrationSuccess = true;
            } else {
                System.out.println("Please try registering again with correct formatting.\n");
            }
        }

        // --- PART 1: USER LOGIN FLOW ---
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("========================================");
            System.out.println("       USER LOGIN AUTHENTICATION        ");
            System.out.println("========================================");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            isLoggedIn = loginSystem.loginUser(loginUsername, loginPassword);
            String loginStatus = loginSystem.returnLoginStatus(isLoggedIn);
            System.out.println("\n" + loginStatus + "\n");

            if (!isLoggedIn) {
                System.out.println("Authentication failed. Please re-enter your credentials.\n");
            }
        }

        // --- PART 2: QUICKCHAT MESSAGING SYSTEM ---
        System.out.println("Welcome to QuickChat.");

        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("           QUICKCHAT MAIN MENU          ");
            System.out.println("========================================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages (Coming Soon)");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            int option = -1;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                System.out.println("Invalid input. Please enter a numeric menu choice (1, 2, or 3).");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("How many messages would you wish to enter? ");
                    int totalMessagesToEnter = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    for (int i = 1; i <= totalMessagesToEnter; i++) {
                        System.out.println("\n--- Entering Message " + i + " of " + totalMessagesToEnter + " ---");

                        // Recipient Cell Validation
                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient cell number (max 10 chars, must include international code like +): ");
                            recipient = scanner.nextLine();
                            if (msgManager.checkRecipientCell(recipient)) {
                                System.out.println("Cell phone number successfully captured.");
                                break;
                            } else {
                                System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                            }
                        }

                        // Message Length Validation
                        String messageContent;
                        while (true) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageContent = scanner.nextLine();
                            if (msgManager.checkMessageLength(messageContent)) {
                                System.out.println("Message sent");
                                break;
                            } else {
                                int excess = messageContent.length() - 250;
                                System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
                            }
                        }

                        // Generate ID and Hash
                        String generatedID = msgManager.generateMessageID();
                        msgManager.setMessageID(generatedID);
                        msgManager.setRecipientCell(recipient);
                        msgManager.setMessageText(messageContent);
                        
                        String hash = msgManager.createMessageHash(generatedID, messageContent);
                        msgManager.setMessageHash(hash);

                        // Message Action Sub-menu
                        System.out.println("\nChoose an action for this message:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");
                        System.out.print("Select option: ");
                        
                        int actionChoice = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        String actionResult = msgManager.sentMessage(actionChoice, messageContent);
                        System.out.println(actionResult);

                        if (actionChoice == 1) {
                            System.out.println("\n----------------------------------------");
                            System.out.println(msgManager.printMessages());
                            System.out.println("----------------------------------------");
                        }
                    }

                    System.out.println("\nTotal number of messages processed: " + msgManager.returnTotalMessages());
                    break;

                case 2:
                    System.out.println("Coming Soon.");
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting QuickChat. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }
}
