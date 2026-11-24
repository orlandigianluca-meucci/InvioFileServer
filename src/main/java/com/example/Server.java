package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(3000);
        Socket s = ss.accept();
        handle(s);
        s.close();
        ss.close();    
    }

    static void handle(Socket s) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String input = in.readLine();
        
        if (input == null) return;

        if (input.contains("/") || input.contains("\\") || input.contains("..") ) {
            out.println("ERR SYNTAX");
            return;
        } 

        if (!input.endsWith(".txt") && !input.endsWith(".md") && !input.endsWith(".csv")) {
            out.println("ERR SYNTAX");
            return;
        }
        
        String parti[] = input.trim().split("\\s+");

        if (!parti[0].contains("LEGGI")) {
            out.println("ERR UNKNOWNCMD");
            return;
        }

        String[] estensioni = {".txt", ".md", ".csv"}; 

        System.out.println(input);

        File cartella = new File("documenti");
        
        if (!parti[1].contains(".")) {
    
            File file = null;

            for(String est : estensioni) {
                File prova = new File(cartella, parti[1] + est);
                if (prova.isFile()) {
                    file = prova;
                    break;
                }
            }

            boolean presente = file.isFile();

            if (presente) {
            try (BufferedReader lettoreFile = new BufferedReader(new FileReader(file))) {
                out.println("OK FILE");
                out.println(); // Riga vuota

                String riga;
                while ((riga = lettoreFile.readLine()) != null) {
                    out.println(riga);
                }
            } catch (IOException e) {
                out.println("ERR READ");
                return;
            }
            }
        }
        else{
            
            File file = new File(cartella, parti[1]);
            
            boolean presente = file.isFile();

             if (presente) {
                try (BufferedReader lettoreFile = new BufferedReader(new FileReader(file))) {
                    out.println("OK FILE");
                    out.println(); // Riga vuota

                    String riga;
                    while ((riga = lettoreFile.readLine()) != null) {
                        out.println(riga);
                    }
                } catch (IOException e) {
                    out.println("ERR READ");
                    return;
                }
            }
        }
    }
}