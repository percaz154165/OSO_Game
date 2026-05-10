/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Network.GameServer;
import Network.ServidorChat;
import java.util.Scanner;

/**
 *
 * @author aperc
 */
public class EjecutorServers {
    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Introduce las filas: ");
            int filas = scan.nextInt();
            System.out.println("Introduce las columnas: ");
            int columnas = scan.nextInt();
            while(!(filas >=3 && filas <=5) && !(columnas >= 3 && columnas <= 6)){
                System.out.println("\n\nERROR\n\n");
                System.out.println("Introduce las filas: ");
                filas = scan.nextInt();
                System.out.println("Introduce las columnas: ");
                columnas = scan.nextInt();
            }

            ServidorChat svchat = new ServidorChat(12000);
            GameServer svjuego = new GameServer(12001,filas, columnas);
            svchat.start();
            svjuego.start();
        }
    }
    
}
