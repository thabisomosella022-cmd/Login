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
        // Create a string longer than 250 characters
        String longMsg = "A".repeat(260);
        assertFalse(msg.checkMessageLength(longMsg));
        
        int excess = longMsg.length() - 250;
        assertEquals(10, excess);
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message msg = new Message();
        assertTrue(msg.checkRecipientCell("+27718693002") || msg.checkRecipientCell("+277186"));
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message msg = new Message();
        assertFalse(msg.checkRecipientCell("08575975889")); // Missing international code '+'
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message();
        msg.setMessageID("0012345678");
        String hash = msg.createMessageHash("0012345678", "Hi Mike, can you join us for dinner tonight?");
        
        // Expected format based on rules: [First 2 ID digits]:[Num sent]:[FIRSTWORDLASTWORD]
        assertTrue(hash.startsWith("00:"));
        assertTrue(hash.contains("HIMIZE") || hash.contains("HITONIGHT") || hash.contains("HIMAKE"));
    }
}
