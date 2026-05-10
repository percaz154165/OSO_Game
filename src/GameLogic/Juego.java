package GameLogic;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author aperc
 */
public class Juego {
    public Tablero tablero;
    public int osos;
    public boolean[][] ososVistos;

    public Juego(int filas,int columnas) {
        this.tablero = new Tablero(filas,columnas);
        this.osos = 0;
        this.ososVistos = new boolean[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                this.ososVistos[i][j] = false;
            }
            
        }
    }
    

    
    public boolean finPartida(){
        for (int i = 0; i < this.tablero.filas; i++) {
            for (int j = 0; j < this.tablero.columnas; j++) {
                if(this.tablero.tablero[i][j].equals(" ")){
                    return false;
                }
            }
        }
        return true;
    }
    


    public void realizaJugada(String jugada){
        String[] partes = jugada.split(",");
        int fila = Integer.parseInt(partes[0]);
        int columna = Integer.parseInt(partes[1]);
        String letra = partes[2];
        if(esJugadaValida(letra,fila,columna)){
            this.tablero.actualizarTablero(letra, fila, columna);
            this.osos = this.osos + cuentaOsos();
        }
        else{
            System.out.println("Jugada no valida, intentelo de nuevo");
        }
    }
    
    public boolean esJugadaValida(String letra,int fila,int columna){
        return (fila < this.tablero.filas && columna < this.tablero.columnas && fila >= 0 && columna >= 0 && this.tablero.tablero[fila][columna].equals(" ") && (letra.equals("O") || letra.equals("S")));
    }
    
    private int cuentaOsos(){
        int cont = 0;
        for (int i = 0; i < this.tablero.filas; i++) {
            for (int j = 0; j < this.tablero.columnas; j++) {
                if(this.tablero.tablero[i][j].equals("S") && !this.ososVistos[i][j]){
                    
                    if((j-1>=0 && j+1<this.tablero.columnas)&&this.tablero.tablero[i][j-1].equals("O")&&this.tablero.tablero[i][j+1].equals("O")&& !this.ososVistos[i][j-1] && !this.ososVistos[i][j+1]){
                        System.out.println("SE ENCONTRO!!");
                        cont = cont+1;
                        this.ososVistos[i][j-1] = true;
                        this.ososVistos[i][j] = true;
                        this.ososVistos[i][j+1] = true;
                    }
                    else if((i-1>=0&&i+1<this.tablero.filas)&&this.tablero.tablero[i-1][j].equals("O")&&this.tablero.tablero[i+1][j].equals("O")&& !this.ososVistos[i+1][j] && !this.ososVistos[i-1][j]){
                        System.out.println("SE ENCONTRO!!");
                        cont = cont+1;
                        this.ososVistos[i-1][j] = true;
                        this.ososVistos[i][j] = true;
                        this.ososVistos[i+1][j] = true;
                    }
                    else if((i-1>=0&&i+1<this.tablero.filas)&&(j-1>=0 && j+1<this.tablero.columnas)&&this.tablero.tablero[i-1][j-1].equals("O")&&this.tablero.tablero[i+1][j+1].equals("O") && !this.ososVistos[i-1][j-1] && !this.ososVistos[i+1][j+1]){
                        System.out.println("SE ENCONTRO!!");
                        cont = cont+1;
                        this.ososVistos[i-1][j-1] = true;
                        this.ososVistos[i][j] = true;
                        this.ososVistos[i+1][j+1] = true;
                    }
                    else if((i-1>=0&&i+1<this.tablero.filas)&&(j-1>=0 && j+1<this.tablero.columnas)&&this.tablero.tablero[i-1][j+1].equals("O")&& this.tablero.tablero[i+1][j-1].equals("O") && !this.ososVistos[i-1][j+1] && !this.ososVistos[i+1][j-1]){
                        System.out.println("SE ENCONTRO!!");
                        cont = cont+1;
                        this.ososVistos[i-1][j+1] = true;
                        this.ososVistos[i][j] = true;
                        this.ososVistos[i+1][j-1] = true;
                    }
                }
            }
        }
        return cont;
    }
}
