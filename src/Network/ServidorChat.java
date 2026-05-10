/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aperc
 */
public class ServidorChat extends Thread{    
    final int port;
    final List<ClientThread> clients = new LinkedList<>();
    
    public ServidorChat(int port) {
        this.port = port;
    }

    @Override
    public void run(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Started server on port " + port);
            // repeatedly wait for connections
            while(! interrupted() ){
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clients, clientSocket);
                clientThread.start();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public class ClientThread extends Thread{
        final List<ClientThread> clients;
        final Socket socket;
        DataOutputStream out;

        public ClientThread(List<ClientThread> clients, Socket socket) {
            this.clients = clients;
            this.socket = socket;
        }

        //only one thread at the time can send messages through the socket
        synchronized public void sendMsg(String msg){
            try {
                out.writeUTF(msg);
            } catch (IOException ex) {
                Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            try {
                System.out.println("Connection from " + 
                        socket.getInetAddress() + ":" + socket.getPort());
                
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                //now that we have managed to stablish proper connection, we add ourselve into the list
                synchronized (clients) { //we must sync because other clients may be iterating over it
                    clients.add(this);
                }
                try{
                    for (String line; (line = in.readUTF()) != null;) {
                        for(ClientThread c : clients){
                            c.sendMsg(line);
                        }
                    }
                }catch(java.net.SocketException j){
                    
                }
            } catch (Exception ex) {
            } finally { //we have finished or failed so let's close the socket and remove ourselves from the list
                clients.forEach(c -> c.sendMsg( "Client disconnected: " + socket.getInetAddress() + ":" + socket.getPort()+"\n" ));
                try{ socket.close(); } catch(Exception ex){} //this will make sure that the socket closes
                synchronized (clients) {
                    clients.remove(this);
                }

            }
        }

        
    }
}
