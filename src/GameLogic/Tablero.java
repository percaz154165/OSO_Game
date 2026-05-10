package GameLogic;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author aperc
 */
public class Tablero {
    public String[][] tablero;
    public int filas;
    public int columnas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        
        this.tablero = new String[this.filas][this.columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                this.tablero[i][j] = " ";
            }
        }
    }
    
    public void actualizarTablero(String caracter, int fila, int columna){
        this.tablero[fila][columna] = caracter;
    }
    
    @Override
    public String toString(){
        String frase;
       frase = "-----TABLERO-----\n";
        for (int i = 0; i < this.filas; i++) {
            frase = frase+"     ";
            for (int j = 0; j < this.columnas; j++) {
                frase = frase + this.tablero[i][j]+",";
            }
            frase = frase+"\n";
        }
        frase = frase + "-----------------";
        return frase;
    }
    
}
