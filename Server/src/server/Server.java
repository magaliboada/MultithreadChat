/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.*;
import java.net.*;
import java.lang.*;

public class Server
{
    public static final int LISTENING_PORT = 2002;
 
    public static void main(String[] args)
    {
        // prepara el servidor per acceptar conexions
        ServerSocket serverSocket = null;
        try 
        {
           serverSocket = new ServerSocket(LISTENING_PORT);
           System.out.println("NakovChatServer started on port " + LISTENING_PORT);
        } 
        catch (IOException se) 
        {
           System.err.println("Can not start listening on port " + LISTENING_PORT);
           se.printStackTrace();
           System.exit(-1);
        }
 
        // administra els clients
        AdministradorClients administradorClients = new AdministradorClients();
        administradorClients.start();
 
        // maneja les coneccions
        while (true) {
           try 
           {
               Socket socket = serverSocket.accept();
               Client client = new Client();
               
               //inicialitza el socket
               client.mSocket = socket;
               
               //crea els threads que sencarreguen de escoltar i enviar
               ClientListener clientListener = new ClientListener(client, administradorClients);
               ClientSender clientSender = new ClientSender(client, administradorClients);
               
               //inicialitza els threads dins del objecte client
               client.mClientListener = clientListener;
               client.mClientSender = clientSender;
               
               clientListener.start();
               clientSender.start();
               
               administradorClients.afegirClient(client);
           } 
           catch (IOException ioe) 
           {
               ioe.printStackTrace();
           }
        }
    }
 
}