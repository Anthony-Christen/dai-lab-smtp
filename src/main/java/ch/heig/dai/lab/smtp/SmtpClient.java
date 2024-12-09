/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

 package ch.heig.dai.lab.smtp;
 import java.io.*;
 import java.net.*;
 import java.nio.charset.Charset;
 
 class SmtpClient {
     // ------------------------------------------------------------------------------
     // Attributes
     // ------------------------------------------------------------------------------
     private final String smtpServerAddress;
     private final int smtpServerPort;
     private final Charset encoding;
     private Socket socket ;
     private BufferedReader in;
     private BufferedWriter out;
 
     // ------------------------------------------------------------------------------
     // Constructor
     // ------------------------------------------------------------------------------
     /**
      * Constructor for the SmtpClient class.
      * @param smtpServerAddress The SMTP server address.
      * @param smtpServerPort The SMTP server port.
      * @param encoding The character encoding to use.
      */
     public SmtpClient(String smtpServerAddress, int smtpServerPort, Charset encoding) {
         this.smtpServerAddress = smtpServerAddress;
         this.smtpServerPort = smtpServerPort;
         this.encoding = encoding;
     }
 
     // ------------------------------------------------------------------------------
     // Methods
     // ------------------------------------------------------------------------------
 
     /**
      * Establishes a connection to the SMTP server and performs the initial handshake.
      * @throws UnknownHostException If the server address is invalid.
      * @throws IOException If an I/O error occurs during connection or communication.
      */
     public void connect() throws UnknownHostException, IOException {
         socket = new Socket(smtpServerAddress, smtpServerPort);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream(), encoding));
         out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), encoding));
         System.out.println("Connecting to " + smtpServerAddress + ":" + smtpServerPort);
 
         checkSMTPServerStatut("220");
         sendLine("EHLO test");
 
         String inLine;
         while ((inLine = in.readLine()) != null) {
             if (inLine.charAt(3) == ' ') {
                 break;
             }
         }
     }
 
     /**
      * Sends an email using the SMTP protocol.
      * @param email The Email object containing sender, recipients, subject, and body.
      * @throws IOException If an I/O error occurs during communication.
      */
     public void send(Email email) throws IOException {
         // MAIL FROM
         sendLine("MAIL FROM:<" + email.getSender() + ">");
         checkSMTPServerStatut("250");
     
         // RCPT TO for each receiver
         for (var r : email.getReceivers()) {
             sendLine("RCPT TO:<" + r + ">");
             checkSMTPServerStatut("250");
         }
     
         // DATA
         sendLine("DATA");
         checkSMTPServerStatut("354");
 
         StringBuilder content = new StringBuilder();
         content.append("Content-Type: text/plain; charset=\"").append(encoding.name()).append("\"\n") // Encoding
         .append("From: ").append(email.getSender()).append("\n") // Headers
         .append("To: you\n")                                         // To   
         .append("Subject: ").append(email.getSubject()).append("\n") // Subject
         .append("\n").append(email.getBody()).append("\r\n.\r");  // Body
 
         // Sending the data
         sendLine(content.toString());
         checkSMTPServerStatut("250");
 
         System.out.println("Sending email... (" + smtpServerAddress + ":" + smtpServerPort + ")");
         System.out.println(email);
     }
 
     /**
      * Closes the connection to the SMTP server.
      * @throws IOException If an I/O error occurs during disconnection.
      */
     public void quit() throws IOException {
         sendLine("QUIT");
         checkSMTPServerStatut("221");
         in.close();
         out.close();
         socket.close();
     
         System.out.println("Closing connection...");
     }
 
     /**
      * Sends a single line of text to the SMTP server.
      * @param outLine The line of text to send.
      * @throws IOException If an I/O error occurs during writing.
      */
     private void sendLine(String outLine) throws IOException {
         out.write(outLine + "\n");
         out.flush();
     }
 
     /**
      * Checks the server's response status code.
      * @param prefix The expected prefix of the response status code.
      * @throws IOException If the server's response does not match the expected prefix.
      */
     private void checkSMTPServerStatut(String prefix) throws IOException {
         String inLine = in.readLine();
 
         if (!inLine.startsWith(prefix)) {
             throw new IOException("[SMTP Server] " + inLine);
         }
     }
 }
 
