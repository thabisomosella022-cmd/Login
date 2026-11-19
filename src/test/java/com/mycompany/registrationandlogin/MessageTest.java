package com.mycompany.registrationandlogin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message populatePart3TestData() {
        Message msg = new Message();
        msg.addTestMessageRecord("1000000001", "10:0:DIDCAKE", "Developer", "+27834557896", "Did you get the cake?", "Sent");
        msg.addTestMessageRecord("1000000002", "10:1:WHEREONTIME", "Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        msg.addTestMessageRecord("1000000003", "10:2:YOHOOOOGATE", "Developer", "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        msg.addTestMessageRecord("0838884567", "08:3:ITTIME", "Developer", "+27838884567", "It is dinner time !", "Sent");
        msg.addTestMessageRecord("1000000005", "10:4:OKYOU", "Developer", "+27838884567", "Ok, I am leaving without you.", "Stored");
        return msg;
    }

    @Test
    public void testCheckMessageLengthSuccess() {
        Message msg = new Message();
        String validMsg = "Hi Mike, can you join us for dinner tonight?";
        assertTrue(msg.checkMessageLength(validMsg));
        assertEquals("Message ready to send.", msg.getMessageLengthStatus(validMsg));
    }

    @Test
    public void testCheckMessageLengthFailure() {
        Message msg = new Message();
        String longMsg = "A".repeat(260);
        assertFalse(msg.checkMessageLength(longMsg));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.getMessageLengthStatus(longMsg));
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message msg = new Message();
        assertTrue(msg.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message msg = new Message();
        assertFalse(msg.checkRecipientCell("08575975889"));
        assertFalse(msg.checkRecipientCell("+27abc123"));
    }

    @Test
    public void testCreateMessageHashExact() {
        Message msg = new Message();
        msg.setMessageID("0012345678");
        String hash = msg.createMessageHash("0012345678", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testHashLoopGeneration() {
        Message msg = new Message();
        String hash1 = msg.createMessageHash("1234567890", "Hello world test", 0);
        String hash2 = msg.createMessageHash("1234567890", "Another message here", 1);

        assertEquals("12:0:HELLOTEST", hash1);
        assertEquals("12:1:ANOTHERHERE", hash2);
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Message msg = populatePart3TestData();
        String[] sent = msg.getSentMessagesArray();
        assertArrayEquals(new String[]{"Did you get the cake?", "It is dinner time !"}, sent);
    }

    @Test
    public void testDisplayLongestMessage() {
        Message msg = populatePart3TestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.", msg.displayLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        Message msg = populatePart3TestData();
        String result = msg.searchByMessageID("0838884567");
        assertEquals("Recipient: +27838884567\nMessage: It is dinner time !", result);
    }

    @Test
    public void testSearchMessagesByRecipient() {
        Message msg = populatePart3TestData();
        String result = msg.searchMessagesByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message msg = populatePart3TestData();
        String result = msg.deleteMessageByHash("10:1:WHEREONTIME");
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
    }

    @Test
    public void testDisplayReport() {
        Message msg = populatePart3TestData();
        String report = msg.displayStoredMessagesReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}