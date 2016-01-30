/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author Maggie
 */
import java.io.*;
import java.net.*;
 
public class Client
{
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 2002;
 
    public static void main(String[] args)
    {
        BufferedReader rebre = null;
        PrintWriter enviar = null;
        
        try 
        {
           // Connect to Chat Server
           Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
           rebre = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
           enviar = new PrintWriter(
               new OutputStreamWriter(socket.getOutputStream()));
           System.out.println("S'ha conectat al servidor " +
              SERVER_HOSTNAME + ":" + SERVER_PORT);
           
           
           Sender sender = new Sender(enviar);
           sender.setDaemon(true);
           sender.start();
 
           try 
           {
            // rep missatges del servidor i els imprimeix
            String message;
                while ((message=rebre.readLine()) != null) 
                {
                    System.out.println(message);
                }
           } 
           catch (Exception ioe) 
           {
                System.err.println("S'ha perdut la connexió amb el servidor.");
           }
           
           
        } 
        
        catch (IOException ioe) 
        {
           System.err.println("No s'ha pogut conectar al servidor " +
               SERVER_HOSTNAME + ":" + SERVER_PORT);
        }
 
        // Create and start Sender thread
        
    }
}