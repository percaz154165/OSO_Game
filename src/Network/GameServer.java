package Network;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import GameLogic.GameState;
import Persistence.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aperc
 */
public class GameServer extends Thread{    
    final int port;
    final List<ClientThread> clients = new LinkedList<>();
    GameState state;
    int filas;
    int columnas;
    
    
    public GameServer(int port,int filas,int columnas) {
        this.port = port;
        this.columnas = columnas;
        this.filas = filas;
    }

    @Override
    public void run(){
        try( ServerSocket serverSocket = new ServerSocket(port); ){
            System.out.println("Started server on port " + port);
            state = new GameState(this.filas,this.columnas);
            // repeatedly wait for connections
            while(! interrupted() ){
                
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clients, clientSocket,clients.size(),state,this.filas,this.columnas);
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
        DataInputStream in;
        int id;
        GameState state;
        int filas;
        int columnas;
        String name;
        Log logger;

        public ClientThread(List<ClientThread> clients, Socket socket,int id,GameState state,int filas, int columnas) {
            this.clients = clients;
            this.socket = socket;
            this.id = id;
            this.state = state;
            this.filas = filas;
            this.columnas = columnas;
            this.logger = new Log();
        }

        //only one thread at the time can send messages through the socket
        synchronized public void sendMsg(String msg){
            try {
                out.writeUTF(msg);
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            
            try {
                System.out.println("Connection from " + socket.getInetAddress() + ":" + socket.getPort());
                
                
                
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String nom = in.readUTF();
                String partes[] = nom.split(",");
                this.name = partes[1];
                int turno = 0;
                
                sendMsg(String.valueOf(this.filas));//filas
                sendMsg(String.valueOf(this.columnas));//columnas
                

                //now that we have managed to stablish proper connection, we add ourselve into the list
                synchronized (clients) { //we must sync because other clients may be iterating over it
                    clients.add(this);
                }
                for(ClientThread c : clients){
                    if(turno != c.id){
                        c.sendMsg("BLOCK");
                    }
                }
                
                
                try{
                    for (String line; (line = in.readUTF()) != null;) {
                        System.out.println(line);
                        for(ClientThread c : clients){
                            c.sendMsg(line);
                            c.sendMsg("BLOCK");
                        }

                        state.juega(line);
                        String[] osos = this.state.extraeOsos();

                        for(ClientThread c : clients){
                            if(c.id == 0){
                                c.sendMsg("OSOS,"+this.state.ososJ1+","+this.state.ososJ2);
                            }
                            else{
                                c.sendMsg("OSOS,"+this.state.ososJ2+","+this.state.ososJ1);
                            }
                            if(osos != null){
                                for(String ubi : osos){
                                    if(ubi != null){
                                        c.sendMsg("OSOUBI,"+ubi);
                                    }
                                }
                            }
                        }

                        this.state.cambiaTurno();


                        if(state.juego.finPartida()){
                            System.out.println("JUEGO: SE ACABO MANIN");
                            break;
                        }


                        for(ClientThread c : clients){
                            if(this.state.turno == c.id){
                                c.sendMsg("UNBLOCK");
                                break;
                            }
                        }
                    }
                }catch(Exception e){
                    
                }
                
                
            for(ClientThread c : clients){
                if(this.state.ososJ1 > this.state.ososJ2){
                    if(c.id == 0){
                        c.sendMsg("GANADOR");
                        logger.incrementarVictorias(c.name);
                    }
                    else{
                        c.sendMsg("PERDEDOR");
                    }
                }
                else if(this.state.ososJ1 == this.state.ososJ2){
                    c.sendMsg("EMPATE");
                }
                else{
                    if(c.id == 0){
                        c.sendMsg("PERDEDOR");
                    }
                    else{
                        c.sendMsg("GANADOR");
                        logger.incrementarVictorias(c.name);
                    }
                }
                    
                }
                System.out.println("JUEGO: FIN");
                
                
                System.exit(0);

            } catch (EOFException | SocketException s) {
                s.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally { //we have finished or failed so let's close the socket and remove ourselves from the list
                try{ socket.close(); } catch(Exception ex){} //this will make sure that the socket closes
                synchronized (clients) {
                    clients.remove(this);
                }
            }
        }

        
    }
}
