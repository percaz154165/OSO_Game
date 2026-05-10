package UI;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import javax.swing.JButton;

/**
 *
 * @author aperc
 */
public class OsoButton extends JButton{
    public int fila;
    public int columna;
    public Boolean actual; //Es el boton pulsado
    public boolean estaEnOso;

    public OsoButton(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.actual = false;
        this.estaEnOso = false;
        this.setVisible(true);
        this.setFocusable(false);
        this.setText(" ");
    }
    
    public void cambiaEstado(){

        switch (this.getText()) {
            case "O":
                this.setText("S");
                break;
            case "S":
                this.setText(" ");
                break;
            default:
                this.setText("O");
                break;
        }
    }
    
    public void reiniciarBoton(){
        this.setText(" ");
        this.actual = false;
    }   
}
