package UI;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author aperc
 */
public class TableroGUI{
    public JPanel tablero;
    public int filas;
    public int columnas;
    public OsoButton botones[][];
    

    public TableroGUI(JPanel tablero, int filas, int columnas) {
        this.tablero = tablero;
        this.filas = filas;
        this.columnas = columnas;
        this.botones = new OsoButton [this.filas][this.columnas];
        
        this.tablero.setLayout(new GridLayout(filas,columnas));
        
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                OsoButton boton = new OsoButton(i,j);
                boton.setPreferredSize(new Dimension(this.tablero.getWidth()/this.columnas,this.tablero.getHeight()/this.filas));
                this.tablero.add(boton);
                botones[i][j] = boton;
                
                boton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boton.actual = true;
                        boton.cambiaEstado();
                        reiniciarTablero();
                        boton.actual = false;
                    }
                });
            }
        }
        this.tablero.revalidate();
        this.tablero.setVisible(true);
    }
    
    public void reiniciarTablero(){
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if(!this.botones[i][j].actual && this.botones[i][j].isEnabled())  {
                    this.botones[i][j].reiniciarBoton();
                }              
            }
        }
    }
    
    public void enviar(){
       for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if(this.botones[i][j].getText().equals("S") || this.botones[i][j].getText().equals("O")){
                    this.botones[i][j].setEnabled(false);
                }              
            }
        } 
    }
    
    public int cuentaOsos(){
        int cont = 0;
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if(this.botones[i][j].getText().equals("S") && !this.botones[i][j].estaEnOso){
                    
                    if((j-1>=0 && j+1<this.columnas)&&this.botones[i][j-1].getText().equals("O")&&this.botones[i][j+1].getText().equals("O")&& !this.botones[i][j-1].estaEnOso && !this.botones[i][j+1].estaEnOso){
                        this.botones[i][j-1].estaEnOso = true;
                        this.botones[i][j-1].setForeground(Color.blue);
                        
                        this.botones[i][j].estaEnOso = true;
                        this.botones[i][j].setForeground(Color.blue);
                        
                        this.botones[i][j+1].estaEnOso = true;
                        this.botones[i][j+1].setForeground(Color.blue);
                        cont = cont+1;
                    }
                    
                    else if((i-1>=0 && i+1<this.filas)&&this.botones[i-1][j].getText().equals("O")&&this.botones[i+1][j].getText().equals("O")&& !this.botones[i-1][j].estaEnOso && !this.botones[i+1][j].estaEnOso){
                        this.botones[i-1][j].estaEnOso = true;
                        this.botones[i-1][j].setForeground(Color.blue);
                        
                        this.botones[i][j].estaEnOso = true;
                        this.botones[i][j].setForeground(Color.blue);
                        
                        
                        this.botones[i+1][j].estaEnOso = true;
                        this.botones[i+1][j].setForeground(Color.blue);
                        cont = cont+1;
                    }
                    
                    else if((j-1>=0 && j+1<this.columnas) && (i-1>=0 && i+1<this.filas) &&this.botones[i-1][j-1].getText().equals("O")&&this.botones[i+1][j+1].getText().equals("O")&& !this.botones[i-1][j-1].estaEnOso && !this.botones[i+1][j+1].estaEnOso){
                        this.botones[i-1][j-1].estaEnOso = true;
                        this.botones[i-1][j-1].setForeground(Color.blue);
                        
                        this.botones[i][j].estaEnOso = true;
                        this.botones[i][j].setForeground(Color.blue);
                        
                        this.botones[i+1][j+1].estaEnOso = true;
                        this.botones[i+1][j+1].setForeground(Color.blue);
                        cont = cont + 1;
                    }
                    
                    else if((j-1>=0 && j+1<this.columnas) && (i-1>=0 && i+1<this.filas) &&this.botones[i-1][j+1].getText().equals("O")&&this.botones[i+1][j-1].getText().equals("O")&& !this.botones[i-1][j+1].estaEnOso && !this.botones[i+1][j-1].estaEnOso){
                        this.botones[i-1][j+1].estaEnOso = true;
                        this.botones[i-1][j+1].setForeground(Color.blue);
                        
                        this.botones[i][j].estaEnOso = true;
                        this.botones[i][j].setForeground(Color.blue);
                        
                        this.botones[i+1][j-1].estaEnOso = true;
                        this.botones[i+1][j-1].setForeground(Color.blue);
                        cont = cont + 1;
                    }
                }
            }
        }
        return cont;
    }
    
    public boolean estaAcabado(){
        for(OsoButton[] fila : this.botones){
            for(OsoButton boton : fila){
                if(boton.isEnabled()){
                    return false;
                }
            }
        }
        return true;
    }
    
    public String extraeJugada(){
        String jug;
        jug = "";
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if(this.botones[i][j].isEnabled() && !this.botones[i][j].getText().equals(" ")){
                    jug = i + "," + j + "," + this.botones[i][j].getText();
                    break;
                }
            }
        }
        return jug;
    }
}
