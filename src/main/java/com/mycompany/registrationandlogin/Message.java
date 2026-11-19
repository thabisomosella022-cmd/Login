package com.mycompany.registrationandlogin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message class handling message IDs, cell verification, hashing, JSON file storage,
 * Part 3 arrays, menu options, and reporting.
 */
public class Message {

    public static class StoredMessageRecord {
        private final String messageID;
        private final String messageHash;
        private final String sender;
        private final String recipientCell;
        private final String messageText;

        public StoredMessageRecord(String messageID, String messageHash, String sender, String recipientCell, String messageText) {
            this.messageID = messageID;
            this.messageHash = messageHash;
            this.sender = sender;
            this.recipientCell = recipientCell;
            this.messageText = messageText;
        }

        public String getMessageID() { return messageID; }
        public String getMessageHash() { return messageHash; }
        public String getSender() { return sender; }
        public String getRecipientCell() { return recipientCell; }
        public String getMessageText() { return messageText; }
    }

    private String messageID;
    private int numMessagesSent = 0;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String currentSender = "Current User";

    private final List<String> sentMessagesList = new ArrayList<>();
    private final List<String> disregardedMessagesList = new ArrayList<>();
    private final List<String> messageHashesList = new ArrayList<>();
    private final List<String> messageIDsList = new ArrayList<>();
    private final List<StoredMessageRecord> storedRecordsList = new ArrayList<>();
    private final List<String> sentMessageSummaries = new ArrayList<>();

    public String[] getSentMessagesArray() {
        return sentMessagesList.toArray(new String[0]);
    }

    public String[] getDisregardedMessagesArray() {
        return disregardedMessagesList.toArray(new String[0]);
    }

    public String[] getMessageHashesArray() {
        return messageHashesList.toArray(new String[0]);
    }

    public String[] getMessageIDsArray() {
        return messageIDsList.toArray(new String[0]);
    }

    /**
     * Reads stored message records from "messages.json" into an array without clearing
     * existing in-memory records.
     * JSON parsing pattern adapted from standard Java regular expression and file I/O tutorials
     * (Oracle Java SE Documentation / Baeldung).
     */
    public StoredMessageRecord[] readStoredMessagesFromJsonArray() {
        return readStoredMessagesFromJsonArray("messages.json");
    }

    public StoredMessageRecord[] readStoredMessagesFromJsonArray(String filePath) {
        List<StoredMessageRecord> loaded = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return storedRecordsList.toArray(new StoredMessageRecord[0]);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }

        Pattern objectPattern = Pattern.compile("\\{[^{}]*\\}");
        Matcher objectMatcher = objectPattern.matcher(content.toString());

        while (objectMatcher.find()) {
            String obj = objectMatcher.group();
            String id = extractJsonField(obj, "messageID");
            String hash = extractJsonField(obj, "messageHash");
            String sender = extractJsonField(obj, "sender");
            String recipient = extractJsonField(obj, "recipientCell");
            String text = extractJsonField(obj, "messageText");
            if (sender.isEmpty()) {
                sender = currentSender;
            }
            StoredMessageRecord record = new StoredMessageRecord(id, hash, sender, recipient, text);
            loaded.add(record);

            boolean alreadyPresent = storedRecordsList.stream().anyMatch(r ->
                (r.getMessageID() != null && r.getMessageID().equals(id)) &&
                (r.getMessageText() != null && r.getMessageText().equals(text))
            );
            if (!alreadyPresent) {
                storedRecordsList.add(record);
            }
        }

        return loaded.isEmpty()
            ? storedRecordsList.toArray(new StoredMessageRecord[0])
            : loaded.toArray(new StoredMessageRecord[0]);
    }

    private String extractJsonField(String jsonObject, String fieldName) {
        Pattern fieldPattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = fieldPattern.matcher(jsonObject);
        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }
        return "";
    }

    public String[] getStoredMessagesTextArray() {
        StoredMessageRecord[] records = readStoredMessagesFromJsonArray();
        String[] texts = new String[records.length];
        for (int i = 0; i < records.length; i++) {
            texts[i] = records[i].getMessageText();
        }
        return texts;
    }

    public void addTestMessageRecord(String id, String hash, String sender, String recipient, String text, String flag) {
        this.messageID = id;
        this.messageHash = hash;
        this.currentSender = sender;
        this.recipientCell = recipient;
        this.messageText = text;

        if (id != null && !id.isEmpty()) {
            messageIDsList.add(id);
        }
        if (hash != null && !hash.isEmpty()) {
            messageHashesList.add(hash);
        }

        String normalizedFlag = (flag != null) ? flag.trim().toLowerCase() : "";
        if ("sent".equals(normalizedFlag)) {
            numMessagesSent++;
            sentMessagesList.add(text);
            sentMessageSummaries.add(
                "Message ID: " + id + "\n" +
                "Message Hash: " + hash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + text
            );
        } else if ("disregard".equals(normalizedFlag) || "disregarded".equals(normalizedFlag)) {
            disregardedMessagesList.add(text);
        } else if ("stored".equals(normalizedFlag) || "store".equals(normalizedFlag)) {
            storedRecordsList.add(new StoredMessageRecord(id, hash, sender, recipient, text));
        }
    }

    // 2a. Display sender and recipient of all stored messages
    public String displayStoredSendersAndRecipients() {
        StoredMessageRecord[] records = readStoredMessagesFromJsonArray();
        StoredMessageRecord[] source = !storedRecordsList.isEmpty()
            ? storedRecordsList.toArray(new StoredMessageRecord[0])
            : records;
        if (source.length == 0) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length; i++) {
            sb.append((i + 1) + ". Sender: " + source[i].getSender() + " | Recipient: " + source[i].getRecipientCell());
            if (i < source.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // 2b. Display the longest stored message (also checks active/sent/stored messages if needed)
    public String displayLongestStoredMessage() {
        readStoredMessagesFromJsonArray();
        String longest = "";
        for (StoredMessageRecord record : storedRecordsList) {
            if (record.getMessageText() != null && record.getMessageText().length() > longest.length()) {
                longest = record.getMessageText();
            }
        }
        if (longest.isEmpty()) {
            for (String sent : sentMessagesList) {
                if (sent != null && sent.length() > longest.length()) {
                    longest = sent;
                }
            }
        }
        return longest.isEmpty() ? "No messages available." : longest;
    }

    // 2c. Search for a message ID and display corresponding recipient and message
    public String searchByMessageID(String targetID) {
        if (targetID == null) {
            return "Message ID not found.";
        }
        for (StoredMessageRecord record : storedRecordsList) {
            if (targetID.equals(record.getMessageID())) {
                return "Recipient: " + record.getRecipientCell() + "\nMessage: " + record.getMessageText();
            }
        }
        for (int i = 0; i < messageIDsList.size(); i++) {
            if (targetID.equals(messageIDsList.get(i))) {
                for (String summary : sentMessageSummaries) {
                    if (summary.contains("Message ID: " + targetID)) {
                        String recipient = extractLineValue(summary, "Recipient: ");
                        String msg = extractLineValue(summary, "Message: ");
                        return "Recipient: " + recipient + "\nMessage: " + msg;
                    }
                }
            }
        }
        return "Message ID not found.";
    }

    private String extractLineValue(String block, String prefix) {
        for (String line : block.split("\n")) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length());
            }
        }
        return "";
    }

    // 2d. Search for all messages stored (or sent) for a particular recipient
    public String searchMessagesByRecipient(String targetRecipient) {
        if (targetRecipient == null) {
            return "No messages found for recipient.";
        }
        List<String> matches = new ArrayList<>();
        readStoredMessagesFromJsonArray();
        for (StoredMessageRecord record : storedRecordsList) {
            if (targetRecipient.equals(record.getRecipientCell()) && record.getMessageText() != null) {
                if (!matches.contains(record.getMessageText())) {
                    matches.add(record.getMessageText());
                }
            }
        }
        for (String summary : sentMessageSummaries) {
            String recipient = extractLineValue(summary, "Recipient: ");
            String msg = extractLineValue(summary, "Message: ");
            if (targetRecipient.equals(recipient) && !msg.isEmpty() && !matches.contains(msg)) {
                matches.add(msg);
            }
        }
        if (matches.isEmpty()) {
            return "No messages found for recipient " + targetRecipient + ".";
        }
        return String.join("\n", matches);
    }

    // 2e. Delete a message using the message hash
    public String deleteMessageByHash(String targetHash) {
        if (targetHash == null || targetHash.trim().isEmpty()) {
            return "Message hash not found.";
        }

        for (int i = 0; i < storedRecordsList.size(); i++) {
            StoredMessageRecord record = storedRecordsList.get(i);
            if (targetHash.equals(record.getMessageHash())) {
                String removedText = record.getMessageText();
                storedRecordsList.remove(i);
                messageHashesList.remove(targetHash);
                messageIDsList.remove(record.getMessageID());
                rewriteJsonFileFromStoredRecords();
                return "Message: \"" + removedText + "\" successfully deleted.";
            }
        }

        for (int i = 0; i < sentMessageSummaries.size(); i++) {
            String summary = sentMessageSummaries.get(i);
            String hash = extractLineValue(summary, "Message Hash: ");
            if (targetHash.equals(hash)) {
                String removedText = extractLineValue(summary, "Message: ");
                sentMessageSummaries.remove(i);
                sentMessagesList.remove(removedText);
                messageHashesList.remove(targetHash);
                if (numMessagesSent > 0) {
                    numMessagesSent--;
                }
                return "Message: \"" + removedText + "\" successfully deleted.";
            }
        }

        return "Message with hash " + targetHash + " not found.";
    }

    private void rewriteJsonFileFromStoredRecords() {
        try (FileWriter fileWriter = new FileWriter("messages.json", false)) {
            for (StoredMessageRecord record : storedRecordsList) {
                String jsonContent = "{\n" +
                        "  \"messageID\": \"" + escapeJson(record.getMessageID()) + "\",\n" +
                        "  \"messageHash\": \"" + escapeJson(record.getMessageHash()) + "\",\n" +
                        "  \"sender\": \"" + escapeJson(record.getSender()) + "\",\n" +
                        "  \"recipientCell\": \"" + escapeJson(record.getRecipientCell()) + "\",\n" +
                        "  \"messageText\": \"" + escapeJson(record.getMessageText()) + "\"\n" +
                        "}";
                fileWriter.write(jsonContent + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating JSON file: " + e.getMessage());
        }
    }

    // 2f. Display a report listing full details of all stored/sent messages
    public String displayStoredMessagesReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== STORED & SENT MESSAGES REPORT ===\n");

        if (!sentMessageSummaries.isEmpty()) {
            report.append("--- Sent Messages ---\n");
            for (String summary : sentMessageSummaries) {
                String id = extractLineValue(summary, "Message ID: ");
                String hash = extractLineValue(summary, "Message Hash: ");
                String recipient = extractLineValue(summary, "Recipient: ");
                String msg = extractLineValue(summary, "Message: ");
                report.append("Message ID: ").append(id)
                      .append(" | Message Hash: ").append(hash)
                      .append(" | Recipient: ").append(recipient)
                      .append(" | Message: ").append(msg)
                      .append("\n");
            }
        }

        readStoredMessagesFromJsonArray();
        if (!storedRecordsList.isEmpty()) {
            report.append("--- Stored Messages ---\n");
            for (StoredMessageRecord record : storedRecordsList) {
                report.append("Message ID: ").append(record.getMessageID())
                      .append(" | Message Hash: ").append(record.getMessageHash())
                      .append(" | Sender: ").append(record.getSender())
                      .append(" | Recipient: ").append(record.getRecipientCell())
                      .append(" | Message: ").append(record.getMessageText())
                      .append("\n");
            }
        }

        if (sentMessageSummaries.isEmpty() && storedRecordsList.isEmpty()) {
            return "No messages available for report.";
        }
        return report.toString().trim();
    }

    // Ensure message ID is not more than 10 characters
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // Validates international format ('+' followed by digits, max 15 chars total, e.g., +27718693002)
    public boolean checkRecipientCell(String recipientCell) {
        if (recipientCell == null) {
            return false;
        }
        return Pattern.matches("^\\+\\d{1,14}$", recipientCell) && recipientCell.length() <= 15;
    }

    // Formats and generates the unique uppercase message hash string using the specific message index
    public String createMessageHash(String messageID, String messageText, int messageIndex) {
        String idPart = (messageID != null && messageID.length() >= 2) ? messageID.substring(0, 2) : "00";

        String firstWord = "";
        String lastWord = "";

        if (messageText != null && !messageText.trim().isEmpty()) {
            String[] words = messageText.trim().split("\\s+");
            firstWord = words[0].replaceAll("[^a-zA-Z]", "");
            lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        }

        String hash = idPart + ":" + messageIndex + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Check if message does not exceed 250 characters
    public boolean checkMessageLength(String messageText) {
        return messageText != null && messageText.length() <= 250;
    }

    // Returns status message matching the 250-character length requirement
    public String getMessageLengthStatus(String messageText) {
        if (messageText != null && checkMessageLength(messageText)) {
            return "Message ready to send.";
        }
        int length = (messageText != null) ? messageText.length() : 0;
        int excess = Math.max(0, length - 250);
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    // Print message details in required order: Message ID, Message Hash, Recipient, Message
    public String printMessages() {
        if (sentMessageSummaries.isEmpty()) {
            return "Message ID: " + messageID + "\n" +
                   "Message Hash: " + messageHash + "\n" +
                   "Recipient: " + recipientCell + "\n" +
                   "Message: " + messageText;
        }
        return String.join("\n----------------------------------------\n", sentMessageSummaries);
    }

    // Return total number of messages sent (Named with double 'ss' per rubric requirements)
    public int returnTotalMessagess() {
        return numMessagesSent;
    }

    public int returnTotalMessages() {
        return numMessagesSent;
    }

    // Process message based on user choice with exact required punctuation and output strings
    public String sentMessage(int choice, String message) {
        if (messageID != null && !messageID.isEmpty() && !messageIDsList.contains(messageID)) {
            messageIDsList.add(messageID);
        }
        if (messageHash != null && !messageHash.isEmpty() && !messageHashesList.contains(messageHash)) {
            messageHashesList.add(messageHash);
        }

        switch (choice) {
            case 1:
                numMessagesSent++;
                this.messageText = message;
                sentMessagesList.add(message);
                sentMessageSummaries.add(
                    "Message ID: " + messageID + "\n" +
                    "Message Hash: " + messageHash + "\n" +
                    "Recipient: " + recipientCell + "\n" +
                    "Message: " + messageText
                );
                return "Message successfully sent.";
            case 2:
                this.messageText = message;
                disregardedMessagesList.add(message);
                return "Press 0 to delete the message";
            case 3:
                this.messageText = message;
                StoredMessageRecord record = new StoredMessageRecord(messageID, messageHash, currentSender, recipientCell, message);
                storedRecordsList.add(record);
                storeMessageToJsonFile();
                return "Message successfully stored";
            default:
                return "Invalid option selected.";
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    /**
     * Stores the message into a JSON file named "messages.json".
     */
    public void storeMessageToJsonFile() {
        String jsonContent = "{\n" +
                "  \"messageID\": \"" + escapeJson(messageID) + "\",\n" +
                "  \"messageHash\": \"" + escapeJson(messageHash) + "\",\n" +
                "  \"sender\": \"" + escapeJson(currentSender) + "\",\n" +
                "  \"recipientCell\": \"" + escapeJson(recipientCell) + "\",\n" +
                "  \"messageText\": \"" + escapeJson(messageText) + "\"\n" +
                "}";

        try (FileWriter fileWriter = new FileWriter("messages.json", true)) {
            fileWriter.write(jsonContent + "\n");
        } catch (IOException e) {
            System.out.println("Error saving message to JSON file: " + e.getMessage());
        }
    }

    // Retained for backward compatibility if called without args
    public String storeMessage() {
        storeMessageToJsonFile();
        return "{\n" +
                "  \"messageID\": \"" + escapeJson(messageID) + "\",\n" +
                "  \"messageHash\": \"" + escapeJson(messageHash) + "\",\n" +
                "  \"sender\": \"" + escapeJson(currentSender) + "\",\n" +
                "  \"recipientCell\": \"" + escapeJson(recipientCell) + "\",\n" +
                "  \"messageText\": \"" + escapeJson(messageText) + "\"\n" +
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
    public void setCurrentSender(String currentSender) { this.currentSender = currentSender; }

    public String getMessageID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getCurrentSender() { return currentSender; }
}