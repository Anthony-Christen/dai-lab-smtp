package ch.heig.dai.lab.smtp;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

class SMTPClient {
    public static void main(String [] args) {

        try (Socket socket = new Socket("localhost", 1025);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            
            String inLine ;
            String outLine ;

            System.out.println(in.readLine());
            
            outLine = "EHLO test" + "\n" ;
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            while((inLine=in.readLine())!=null){

                System.out.println(inLine);
                if(inLine.charAt(3) == ' '){
                    break ;
                }
            }
            outLine = "MAIL FROM:<JQP@bar.com>" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            inLine = in.readLine();
            System.out.println(inLine);

            outLine = "RCPT TO:<Jones@XYZ.COM>" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            inLine = in.readLine();
            System.out.println(inLine);

            outLine = "RCPT TO:<Admin.MRC@foo.com>" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            inLine = in.readLine();
            System.out.println(inLine);

            outLine = "DATA" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            inLine = in.readLine();
            System.out.println(inLine);

            outLine = "From: John Q. Public <JQP@bar.com>" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            outLine = "To: Jones@xyz.com" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            outLine = "To: Admin.MRC@foo.com" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            outLine = "Subject: The Next Meeting of the Board" + "\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            outLine = "\nBill:\nThe next meeting of the board of directors will be on Tuesday.\nJohn." + "\r\n.\r\n";
            System.out.print(outLine);
            out.write(outLine);
            out.flush();

            inLine = in.readLine();
            System.out.println(inLine);

            //appelle à la fonction de récup des adresses e-mail
            //génère les groupes selon le param n
            //choisir le MSG
            //String mail ;
            //out.write("MAIL FROM:<" + mail);
        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}