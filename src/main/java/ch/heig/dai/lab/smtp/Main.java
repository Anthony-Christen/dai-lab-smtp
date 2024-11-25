package ch.heig.dai.lab.smtp;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        TextualTCPClient.run();

    }


}

class TextualTCPClient {
    public static void run() {

        try (Socket socket = new Socket("localhost", 1025);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            
            String line ;
            System.out.println(in.readLine());
            out.write("EHLO test" + "\n");
            out.flush();
            while((line=in.readLine())!=null){
                System.out.println(line);
            }
            //appelle à la fonction de récup des adresses e-mail
            //génère les groupes selon le param n
            //choisir le MSG
            String mail ;
            //out.write("MAIL FROM:<" + mail);
        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}