/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationandlogin;

import java.util.Random;

/**
 * Message class handling message IDs, cell verification, hashing, JSON storage, and menu options.
 */
public class Message {

    private String messageID;
    private int numMessagesSent = 0;
    private String recipientCell;
    private String messageText;
    private String messageHash;

    // Ensure message ID is not more than 10 characters
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // Ensure recipient cell number is no more than 10 characters and starts with an international code
    public boolean checkRecipientCell(String recipientCell) {
        if (recipientCell == null) return false;
        return recipientCell.startsWith("+") && recipientCell.length() <= 10;
    }

    // Formats and generates the unique uppercase message hash string
    public String createMessageHash(String messageID, String messageText) {
        String idPart = (messageID != null && messageID.length() >= 2) ? messageID.substring(0, 2) : "00";
        
        String firstWord = "";
        String lastWord = "";
        
        if (messageText != null && !messageText.trim().isEmpty()) {
            String[] words = messageText.trim().split("\\s+");
            firstWord = words[0].replaceAll("[^a-zA-Z]", "");
            lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        }
        
        String hash = idPart + ":" + numMessagesSent + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Check if message does not exceed 250 characters
    public boolean checkMessageLength(String messageText) {
        return messageText != null && messageText.length() <= 250;
    }

    // Print message details in required order
    public String printMessages() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipientCell + "\n" +
               "Message: " + messageText;
    }

    // Return total number of messages sent (Named with double 'ss' per rubric requirements)
    public int returnTotalMessagess() {
        return numMessagesSent;
    }

    // Return total number of messages sent (Delegates to returnTotalMessagess to support main method calls)
    public int returnTotalMessages() {
        return numMessagesSent;
    }

    // Process message based on user choice
    public String sentMessage(int choice, String message) {
        switch (choice) {
            case 1:
                numMessagesSent++;
                this.messageText = message;
                return "Message successfully sent";
            case 2:
                return "Press 0 to delete the message";
            case 3:
                return "Message successfully stored";
            default:
                return "Invalid option selected.";
        }
    }

    // Custom method to store the message in a JSON format as requested in the PoE instructions
    public String storeMessage() {
        return "{\n" +
               "  \"messageID\": \"" + messageID + "\",\n" +
               "  \"messageHash\": \"" + messageHash + "\",\n" +
               "  \"recipientCell\": \"" + recipientCell + "\",\n" +
               "  \"messageText\": \"" + messageText + "\"\n" +
               "}";
    }

    // Auto-generate a random 10-digit numeric ID
    public String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + rand.nextInt(900000000);
        return String.valueOf(id);
    }

    // Getters and Setters
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }
    
    public String getMessageID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
}
