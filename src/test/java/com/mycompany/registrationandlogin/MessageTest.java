package com.mycompany.registrationandlogin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLengthSuccess() {
        Message msg = new Message();
        String validMsg = "Hi Mike, can you join us for dinner tonight?";
        assertTrue(msg.checkMessageLength(validMsg));
    }

    @Test
    public void testCheckMessageLengthFailure() {
        Message msg = new Message();
        String longMsg = "A".repeat(260);
        assertFalse(msg.checkMessageLength(longMsg));
        int excess = longMsg.length() - 250;
        assertEquals(10, excess);
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message msg = new Message();
        // Tests exact real South African number format from test data
        assertTrue(msg.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message msg = new Message();
        assertFalse(msg.checkRecipientCell("08575975889")); // Missing international code '+'
    }

    @Test
    public void testCreateMessageHashExact() {
        Message msg = new Message();
        msg.setMessageID("0012345678");
        // Test Case 1 data: ID starting with 00, index 0, message "Hi Mike, can you join us for dinner tonight?"
        String hash = msg.createMessageHash("0012345678", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testHashLoopGeneration() {
        Message msg = new Message();
        // Testing loop increment for hash generation across multiple messages
        String hash1 = msg.createMessageHash("1234567890", "Hello world test", 0);
        String hash2 = msg.createMessageHash("1234567890", "Another message here", 1);
        
        assertEquals("12:0:HELLOTEST", hash1);
        assertEquals("12:1:ANOTHERHERE", hash2);
    }
}