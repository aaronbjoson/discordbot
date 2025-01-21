package com.disdaccbot;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

public class main {
	

	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			MyDiscBot myBot = new MyDiscBot();
		} catch (LoginException e) {
			System.out.println("Error: Invalid bot token!");
		} catch(SQLException s) {
			System.out.println("Error: SQLException has been thrown");
		}
		
	}

}
