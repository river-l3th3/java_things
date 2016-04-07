/**
 *
 * @author river-l3th3
 */


/*
This is freeware. The GNU license applies. There are no guarantees.

If you really like it, send a bitcoin through the ether.

Be sure to compile with the ODBC driver for mysql and javax.mail.*, which is 
a free .jar that you will need to add to $CLASSPATH (easier in netbeans, else, edit your /home/user/.bash.rc file

Also be sure and apply the database scripts to your MySQL instance or you'll get a code failure

You will need to populate the your_database.CARRIERS table with the appropriate email
suffixes that are used by each carrier to receive emails as SMS

You can get started with:

+--------------+--------------+
| CARRIER_NAME | MSG_ADDRESS  |
+--------------+--------------+
| AT&T         | @txt.att.net |
| T-Mobile     | @tmomail.net |
| Verizon      | @vtext.com   |
+--------------+--------------+

 */

package BirthdayServer;

import java.sql.*;
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BirthdayServer {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/your_database";
    
    static final String USER = "username";
    static final String PASS = "password";
    //String msg;
    
    public static int sendSMS(String number,String name,String day,String suffix) 
    {
        System.out.println("In the SMS function...");
        
        String fullNumber = number + suffix;  
        String msg = "";
        if (null != day)
        switch (day) {
            case "tomorrow":
                msg = "It is " + name + "'s birthday " + day;
                break;
            case "today":
                msg = "It is " + name + "'s birthday " + day;
                break;
            default:
                msg = "It is " + name + "'s birthday in " + day;
                break;
        }
        String emailUser = "gmail_username";
        String emailPass = "gmail_password";
        String host = "smtp.gmail.com";
        
        Properties props = new Properties();
        
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        
        

        try {
            Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUser, emailPass);
                }
              });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUser +"@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(fullNumber));
            message.setText(msg);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }       
        
        return 0;
        
    }
    
    public static void main(String args[]) {
        Connection conn = null;
	Statement stmt = null;
	
        System.out.println("Starting...");
        
	try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database...");

            conn=DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "call find_birthdays;";
            System.out.println("Executing SQL...");
            ResultSet rs = stmt.executeQuery(sql);
            //System.out.println("Test");
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    //System.out.println(rs.next());
                    String name = rs.getString("BDAY_NAME");
                    String date = rs.getString("DIFF");
                    String suffix = rs.getString("CARRIER_ADDRESS");
                    String number = rs.getString("LINKED_NUMBER");
                    sendSMS(number,name,date,suffix);
                    System.out.println(name + " " +date + " "+number);
                }
            }

        } catch(SQLException exc) {
            exc.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }	
    }
}
