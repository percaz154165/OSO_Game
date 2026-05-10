/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;

import UI.TableroGUI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author aperc
 */
public class JuegoOSO extends Thread{
    
    private String hostAddr;
    private int port;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private int filas;
    private int columnas;
    private String name;
    
    private JPanel panel;
    private JButton enviar;
    private TableroGUI tablero;
    private JLabel ososLocal;
    private JLabel ososRival;

    

    public JuegoOSO(JButton enviar,JPanel tablero,JLabel osoLoc, JLabel osoRiv, String port, String ip, String name) {
        this.enviar = enviar;
        this.panel = tablero;
        this.enviar.setEnabled(false);
        this.ososLocal = osoLoc;
        this.ososRival = osoRiv;
        this.hostAddr = ip;
        this.port = Integer.parseInt(port);
        this.name = name;
    }
    
    @Override
    public void run(){
        try {
           socket = new Socket(this.hostAddr,this.port);
           in = new DataInputStream(socket.getInputStream());
           out = new DataOutputStream(socket.getOutputStream());
           
           out.writeUTF("NOMBRE,"+name);

           this.filas = Integer.parseInt(in.readUTF());
           this.columnas = Integer.parseInt(in.readUTF());
           
           this.enviar.setEnabled(true);
           this.tablero = new TableroGUI(this.panel,filas,columnas);
           
           this.enviar.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String jug = "";
                    jug = tablero.extraeJugada();
                    try {
                        out.writeUTF(jug);
                    } catch (IOException ex) {
                        Logger.getLogger(JuegoOSO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            });
           for (String recv; (recv = in.readUTF()) != null;) {

                System.out.println(recv);
                if(recv.equals("GANADOR")){
                    JOptionPane.showMessageDialog(null, "GANASTES", "final", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                else if(recv.equals("PERDEDOR")){
                    JOptionPane.showMessageDialog(null, "PERDISTES :(", "final", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                else if(recv.equals("EMPATE")){
                    JOptionPane.showMessageDialog(null, "EMPATASTES", "final", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                else if(recv.equals("BLOCK")){
                    tablero.tablero.setEnabled(false);
                    enviar.setEnabled(false);
                }
                else if(recv.equals("UNBLOCK")){
                    tablero.tablero.setEnabled(true);
                    enviar.setEnabled(true);
                }
                else if(recv.startsWith("OSOS,")){
                    String[] partes = recv.split(",");
                    this.ososLocal.setText(partes[1]);
                    this.ososRival.setText(partes[2]);
                }
                else if(recv.startsWith("OSOUBI,")){
                    pintarOsos(recv);
                }
                else{
                    pintar(recv);
                }

                       
        }  
        } catch (IOException ex) {
            ImageIcon icon = new ImageIcon("src\\DATA\\osoError.png");
            JOptionPane.showMessageDialog(null, "ERROR AL CONECTAR AL SERVIDOR", "Error", JOptionPane.ERROR_MESSAGE,icon);
            Logger.getLogger(JuegoOSO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pintar(String jugada){
        String[] partes = jugada.split(",");
        int fila = Integer.parseInt(partes[0]);
        int columna = Integer.parseInt(partes[1]);
        String letra = partes[2];
        
        this.tablero.botones[fila][columna].setText(letra);
        this.tablero.botones[fila][columna].setEnabled(false);
        
    }
    
    public void pintarOsos(String jugada){
        String[] partes = jugada.split(",");
        int fila = Integer.parseInt(partes[1]);
        int columna = Integer.parseInt(partes[2]);
        this.tablero.botones[fila][columna].setForeground(Color.pink);
    }  
    public void desconectar(){
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
            System.exit(0);
        } catch (IOException ex) {
            
        }
    }
}
