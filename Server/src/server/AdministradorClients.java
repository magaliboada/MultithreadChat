/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Maggie
 */
public class AdministradorClients extends Thread
{
    private Vector CuaMissatges = new Vector();
    private Vector mClients = new Vector();
 
    /**
     * Adds given client to the server's client list.
     */
    public synchronized void afegirClient(Client aClientInfo)
    {
        mClients.add(aClientInfo);
    }
 
    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void eliminarClient(Client aClientInfo)
    {
        int clientIndex = mClients.indexOf(aClientInfo);
        if (clientIndex != -1)
           mClients.removeElementAt(clientIndex);
    }
 
    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void gestionarMissatge(Client client, String missatge)
    {
        Socket socket = client.mSocket;
        
        missatge = socket.getPort() + " : " + missatge;
        CuaMissatges.add(missatge);
        notify();
    }
 
    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized String getNextMessageFromQueue()
    throws InterruptedException
    {
        //mentres no hi hagi missatge en cua espera
        while (CuaMissatges.size()==0)
           wait();
        
        //llegeix el missatge en cua, el borra i el retorna
        String missatge = (String) CuaMissatges.get(0);
        CuaMissatges.removeElementAt(0);
        return missatge;
    }
 
    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    private synchronized void sendMessageToAllClients(String missatge)
    {
        //envia el missatge a la llista de clients
        for (int i=0; i<mClients.size(); i++) {
           Client clientInfo = (Client) mClients.get(i);
           clientInfo.mClientSender.sendMessage(missatge);
        }
    }
 
    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */
    public void run()
    {
        try {
           while (true) {
               String message = getNextMessageFromQueue();
               sendMessageToAllClients(message);
           }
        } catch (InterruptedException ie) {
           // Thread interrupted. Stop its execution
        }
    }
 
}

