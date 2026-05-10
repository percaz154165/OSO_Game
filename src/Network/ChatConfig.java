/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author aperc
 */
public class ChatConfig extends Thread{
    private String hostAddr;
    private int port;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    public JButton enviar;
    public JTextField USUARIO;
    public JTextField MENSAJE;
    public JTextArea CHAT;

    
    public ChatConfig(JButton enviar, JTextField USUARIO, JTextField MENSAJE, JTextArea CHAT,String port, String ip) {
        this.enviar = enviar;
        this.USUARIO = USUARIO;
        this.MENSAJE = MENSAJE;
        this.CHAT = CHAT;
        this.hostAddr = ip;
        this.port = Integer.parseInt(port);
        
        ImageIcon icon = new ImageIcon("src\\DATA\\enviar.png");
        this.enviar.setIcon(icon);
        this.enviar.setEnabled(false);
        this.CHAT.setEditable(false);
        this.MENSAJE.setEnabled(false);
        
        
        
        this.enviar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!MENSAJE.getText().equals("")){
                    String msg;
                    msg = USUARIO.getText()+"-> "+MENSAJE.getText()+"\n";
                    MENSAJE.setText("");
                    try { 
                        out.writeUTF(msg);
                        out.flush();
                    } catch (IOException ex) {
                        System.out.println("");
                    }
                }
            }
        });
    }
    
    @Override
    public void run(){
        if(!USUARIO.getText().equals("") && !USUARIO.getText().equals(" ")){
            enviar.setEnabled(true);
            MENSAJE.setEnabled(true);
            USUARIO.setEnabled(false);

            try {
                socket = new Socket(hostAddr, port);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                Thread recibir = new Thread(()->{
                   try {
                       while(true){
                           String recv = in.readUTF();
                           CHAT.append(recv);
                       }
                   } catch(IOException ex){

                   }
                });
                recibir.start();
            } catch (IOException ex) {
                System.out.println("");
            } 
        }
        else{
            JOptionPane.showMessageDialog(null, "USUARIO NO VALIDO", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void desconectar(){
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
            this.interrupt();
        } catch (IOException ex) {
            
        }
    }

}
