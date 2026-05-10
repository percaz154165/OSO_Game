package Persistence;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aperc
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alumno
 */
public class Log {
    private Map<String, Integer> contadorVictorias;
    private String archivoContador = "src/DATA/log.txt";

    public Log() {
        contadorVictorias = new HashMap<>();
        cargarContadorDesdeArchivo();
    }

    public void incrementarVictorias(String jugador) {
        contadorVictorias.put(jugador, contadorVictorias.getOrDefault(jugador, 0) + 1);
        guardarContadorEnArchivo();
    }

    public void cargarContadorDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoContador))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(": ");
                String jugador = partes[0];
                int victorias = Integer.parseInt(partes[1]);
                contadorVictorias.put(jugador, victorias);
            }
        } catch (IOException e) {
            // Manejar excepciones de lectura de archivo
            e.printStackTrace();
        }
    }

    public void guardarContadorEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoContador))) {
            for (Map.Entry<String, Integer> entry : contadorVictorias.entrySet()) {
                bw.write(entry.getKey() + ": " + entry.getValue());
                bw.newLine();
            }
            System.out.println("Escrito correctamente en el fichero");
        } catch (IOException e) {
            // Manejar excepciones de escritura de archivo
            e.printStackTrace();
        }
    }

    public void mostrarContador() {
        System.out.println("Contador de victorias:");
        for (Map.Entry<String, Integer> entry : contadorVictorias.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
