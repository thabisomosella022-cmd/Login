package com.mycompany.registrationandlogin;

import java.util.Scanner;

public class RegistrationAndLogin {

    private static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // discard invalid token
            }
        }
    }

    private static int readNonNegativeInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (value >= 0) {
                    return value;
                }
                System.out.println("Please enter 0 or a positive number.");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // discard invalid token
            }
        }
    }

    private static void handleStoredMessagesMenu(Scanner scanner, Message msgManager) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n========================================");
            System.out.println("            STORED MESSAGES MENU         ");
            System.out.println("========================================");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message ID (display recipient and message)");
            System.out.println("4. Search for all messages stored for a particular recipient");
            System.out.println("5. Delete a message using the message hash");
            System.out.println("6. Display full report of all stored/sent messages");
            System.out.println("7. Back to Main Menu");

            int subChoice = readIntInRange(scanner, "Choose an option: ", 1, 7);

            switch (subChoice) {
                case 1:
                    System.out.println("\n" + msgManager.displayStoredSendersAndRecipients());
                    break;
                case 2:
                    System.out.println("\nLongest message:\n" + msgManager.displayLongestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter Message ID to search: ");
                    String searchId = scanner.nextLine();
                    System.out.println("\n" + msgManager.searchByMessageID(searchId));
                    break;
                case 4:
                    System.out.print("Enter recipient cell number to search: ");
                    String searchRecipient = scanner.nextLine();
                    System.out.println("\n" + msgManager.searchMessagesByRecipient(searchRecipient));
                    break;
                case 5:
                    System.out.print("Enter message hash to delete: ");
                    String deleteHash = scanner.nextLine();
                    System.out.println("\n" + msgManager.deleteMessageByHash(deleteHash));
                    break;
                case 6:
                    System.out.println("\n" + msgManager.displayStoredMessagesReport());
                    break;
                case 7:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();
        Message msgManager = new Message();

        System.out.println("========================================");
        System.out.println("   WELCOME TO THE REGISTRATION SYSTEM   ");
        System.out.println("========================================");

        boolean registrationSuccess = false;
        String loggedInFullName = "User";

        // --- PART 1: USER REGISTRATION LOOP ---
        while (!registrationSuccess) {
            System.out.print("Enter username (must contain '_' and max 5 characters): ");
            String username = scanner.nextLine();

            System.out.print("Enter password (min 8 chars, uppercase, number, special char): ");
            String password = scanner.nextLine();

            System.out.print("Enter cell phone number (e.g., +27718693002): ");
            String cellPhoneNumber = scanner.nextLine();

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            String regMessage = loginSystem.registerUser(username, password, cellPhoneNumber, firstName, lastName);
            System.out.println("\n" + regMessage + "\n");

            if (regMessage.contains("successfully")) {
                registrationSuccess = true;
                loggedInFullName = (firstName + " " + lastName).trim();
            } else {
                System.out.println("Please try registering again with correct formatting.\n");
            }
        }

        // --- PART 1: USER LOGIN FLOW ---
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("========================================");
            System.out.println("        USER LOGIN AUTHENTICATION        ");
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

        msgManager.setCurrentSender(loggedInFullName);

        // --- PART 2 & 3: QUICKCHAT MESSAGING SYSTEM ---
        System.out.println("Welcome to QuickChat.");

        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("            QUICKCHAT MAIN MENU          ");
            System.out.println("========================================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages (Coming Soon)");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            int option = readIntInRange(scanner, "Choose an option: ", 1, 4);

            switch (option) {
                case 1:
                    int totalMessagesToEnter = readNonNegativeInt(scanner, "How many messages would you wish to enter? ");

                    for (int i = 1; i <= totalMessagesToEnter; i++) {
                        System.out.println("\n--- Entering Message " + i + " of " + totalMessagesToEnter + " ---");

                        // Recipient Cell Validation
                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient cell number (e.g., +27718693002): ");
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
                                break;
                            } else {
                                System.out.println(msgManager.getMessageLengthStatus(messageContent));
                            }
                        }

                        // Generate ID and Hash (passing message index i - 1 for proper hash numbering)
                        String generatedID = msgManager.generateMessageID();
                        msgManager.setMessageID(generatedID);
                        msgManager.setRecipientCell(recipient);
                        msgManager.setMessageText(messageContent);

                        String hash = msgManager.createMessageHash(generatedID, messageContent, (i - 1));
                        msgManager.setMessageHash(hash);

                        // Message Action Sub-menu
                        System.out.println("\nChoose an action for this message:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");

                        int actionChoice = readIntInRange(scanner, "Select option: ", 1, 3);

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

                case 4:
                    handleStoredMessagesMenu(scanner, msgManager);
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }
}