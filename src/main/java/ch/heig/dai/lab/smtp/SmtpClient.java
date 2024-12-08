/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
    public SmtpClient(String smtpServerAddress, int smtpServerPort, Charset encoding) {
        this.smtpServerAddress = smtpServerAddress;
        this.smtpServerPort = smtpServerPort;
        this.encoding = encoding;
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    public void connect()throws UnknownHostException, IOException{
        
        try{
            socket = new Socket(getSmtpServerAddress(), getSmtpServerPort());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), getEncoding()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), getEncoding()));
            System.out.println("Connecting to " + smtpServerAddress + ":" + smtpServerPort);

            String inLine ;
            String outLine ;

            System.out.println(in.readLine());
            
            outLine = "EHLO test" + "\n" ;
            sendLine(outLine);

            while((inLine=in.readLine())!=null){

                System.out.println(inLine);
                if(inLine.charAt(3) == ' '){
                    break ;
                }
            }

        }catch(Exception e){
            System.err.println("Erreur " + e);
        }
        
    }

    public void send(Email email) {
        String inLine = "";
        String outLine;
    
        // MAIL FROM
        outLine = "MAIL FROM:<" + email.getSender() + ">\n";
        sendLine(outLine);
        receptingLine(inLine);
    
        // RCPT TO for each receiver
        for (var r : email.getReceivers()) {
            sendLine("RCPT TO:<" + r + ">\n");
            receptingLine(inLine);
        }
    
        // DATA
        outLine = "DATA\n";
        sendLine(outLine);
        receptingLine(inLine);
    
        // Headers
        outLine = "From: " + email.getSender() + "\n";
        sendLine(outLine);

        // Concatenate all recipients for the "To" header
        String toHeader = "To: " + String.join(", ", email.getReceivers()) + "\n";
        sendLine(toHeader);
    
        // Dynamically encoded Subject
        outLine = "Subject: " + encodeSubject(email.getSubject()) + "\n";
        sendLine(outLine);
    
        // Content-Type header using the client's encoding
        outLine = "Content-Type: text/plain; charset=" + encoding.name() + "\n";
        sendLine(outLine);
    
        // Content-Transfer-Encoding header for Base64
        outLine = "Content-Transfer-Encoding: base64\n\n";
        sendLine(outLine);
    
        // Encoded Body
        outLine = encodeBody(email.getBody()) + "\r\n.\r\n";
        sendLine(outLine);
        receptingLine(inLine);
    
        System.out.println("Sending email... (" + smtpServerAddress + ":" + smtpServerPort + ")");
        System.out.println(email);
    }
    

    public void quit() {

        String inLine="" ;
        sendLine("QUIT\n");
        receptingLine(inLine);

        System.out.println("Closing connection...");
    }

    private String getSmtpServerAddress(){return smtpServerAddress;}
    private int getSmtpServerPort(){return smtpServerPort;}
    private Charset getEncoding(){return encoding;}
    private void sendLine(String outLine){

        try{
            System.out.print(outLine);
            out.write(outLine);
            out.flush();
        }catch(IOException e){
            System.err.println("Error "+ e);
        }
        

    }
    private void receptingLine(String inLine){
        try{
            inLine = in.readLine();
            System.out.println(inLine);
        }catch(IOException e){
            System.err.println("Error "+ e);
        }
    }

    private String encodeSubject(String subject) {
    return "=?"
            + encoding.name() // Utilise l'encodage du client
            + "?B?"
            + Base64.getEncoder().encodeToString(subject.getBytes(encoding))
            + "?=";
    }

    private String encodeBody(String body) {
        return Base64.getEncoder().encodeToString(body.getBytes(encoding));
    }
    

}