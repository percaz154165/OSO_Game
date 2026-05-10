package GameLogic;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aperc
 */
public class GameState {
    public int turno;
    public Juego juego;
    public int ososJ1;
    public int ososJ2;

    public GameState(int filas,int columnas) {
        this.turno = 0;
        this.juego = new Juego(filas,columnas);
        this.ososJ1 = juego.osos;
        this.ososJ2 = juego.osos;
    }
    
    public void cambiaTurno(){
        if(this.turno == 0){
            this.turno = 1;
        }
        else{
            this.turno = 0;
        }
    }
    
    public void juega(String jug){
        this.juego.realizaJugada(jug);
        if(turno == 0){
            if(this.ososJ1 + this.ososJ2 != juego.osos){
                ososJ1 += 1;
            }
        }
        else {
            if(this.ososJ1 + this.ososJ2 != juego.osos){
                ososJ2 += 1;
            }
        }
    }
    
    public String[] extraeOsos(){
        String[] jug = new String[this.juego.tablero.filas * this.juego.tablero.columnas];
        int k = 0;
        for (int i = 0; i < this.juego.ososVistos.length; i++) {
            for (int j = 0; j < this.juego.ososVistos[0].length; j++) {
                if(this.juego.ososVistos[i][j]){
                    jug[k] = i+","+j;
                    k = k+1;
                }
            }
        }
        return jug;
    }
}
