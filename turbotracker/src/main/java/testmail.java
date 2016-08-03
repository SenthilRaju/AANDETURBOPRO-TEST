import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
 




import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class testmail {
	
		
	/*public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        
       
        final String toEmail = "velsubha@gmail.com"; // can be any email id 
        final String subject="Test mail";
        final String body="testing from development team";
        
//        final String fromEmail = "eric@aandespecialties.com"; //requires valid gmail id
//        final String password = "#turb0Pr0"; // correct password for gmail id
//        final String hostaddr="smtp.office365.com";
//        final String portaddr="587";
        
//        final String fromEmail = "rhiannon@1hvac.com"; //requires valid gmail id
//        final String password = "Sammy"; // correct password for gmail id
//        final String hostaddr="smtp.office365.com";
//        final String portaddr="587";
        
        final String fromEmail = "rhiannon@1hvac.com"; //requires valid gmail id
        final String password = "Sammy"; // correct password for gmail id
        final String hostaddr="mail.1hvac.com";
        final String portaddr="587";
        		
        //rhiannon@1hvac.com Sammy
         
        System.out.println("Mail started");
        Properties props = new Properties();
        props.put("mail.smtp.host", hostaddr); //SMTP Host
        props.put("mail.smtp.port", portaddr); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
         
                //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
         
        
        
        
        
        
        
        
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("eric@aandespecialties.com", "Eric"));

        msg.setReplyTo(InternetAddress.parse(toEmail, false));

        msg.setSubject(subject, "UTF-8");

        msg.setText(body, "UTF-8");

        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        System.out.println("Message is ready");
        Transport.send(msg);  

        System.out.println("EMail Sent Successfully!!");
        
        
         
    }*/
		 /* public static void main(String[] argv) throws Exception {
		    int decimalPlaces = 2;
		    BigDecimal bd = new BigDecimal("36.889");
		     
		    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);
		    System.out.println(bd);
		    String string = bd.toString();
		    
		  }*/
	
	public static void main(String[] args) {

		   
		   // returns the float value represented by the string argument
		   double retval = Double.parseDouble("170392.99");
		   System.out.println("Value = " + retval);
		   float f =(float) 17039.99;
		   System.out.println("Value = " + f);
		   }
}
