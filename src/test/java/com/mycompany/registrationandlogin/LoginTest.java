/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationandlogin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testCheckUserNameValid() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserNameInvalid() {
        Login login = new Login();
        assertFalse(login.checkUserName("kylelongname"));
    }

    @Test
    public void testCheckPasswordComplexityValid() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCheckCellPhoneNumberValid() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+278389"));
    }

    @Test
    public void testCheckCellPhoneNumberInvalid() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUserInvalidUsernameMessage() {
        Login login = new Login();
        String result = login.registerUser("toolongname", "Ch&&sec@ke99!", "+278389", "Kyle", "Smith");
        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testRegisterUserInvalidPasswordMessage() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "weak", "+278389", "Kyle", "Smith");
        assertTrue(result.contains("Password is not correctly formatted"));
    }

    @Test
    public void testRegisterUserSuccessMessage() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+278389", "Kyle", "Smith");
        assertEquals("User registered successfully. Cell phone number successfully added.", result);
    }

    @Test
    public void testLoginUserSuccess() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+278389", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginUserFailure() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+278389", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "WrongPassword!"));
    }
}