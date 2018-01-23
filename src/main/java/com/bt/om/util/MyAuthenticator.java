package com.bt.om.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
   private String username;
   private String password;

   public MyAuthenticator(String username, String password) {
       this.username = username;
       this.password = password;
   }

   @Override
   protected PasswordAuthentication getPasswordAuthentication() {
       return new PasswordAuthentication(username, password);
   }
}
