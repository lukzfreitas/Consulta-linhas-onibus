/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Lucas
 */
public class EstruturaDeDados {
    //TODO: histograma com intervalo de 84 linhas
    public ArrayList<Parada> listaParadas = new ArrayList<>();
    public ArrayList<Linha> listaLinhas = new ArrayList<>();
    public ArrayList<ParadaLinha> listaParadasLinhas = new ArrayList<>();
    public Map<Integer, ListDoubleLinked<Linha>> listaDeLinhasPorParada = new HashMap<Integer, ListDoubleLinked<Linha>>();
    public Map<Integer, ListDoubleLinked<Parada>> listaDeParadasPorLinha = new HashMap<Integer, ListDoubleLinked<Parada>>();

    public EstruturaDeDados() throws IOException {
        lerParadas();
        lerLinhas();
        lerParadasLinhas();
    }
    
    public Map<Integer, ListDoubleLinked<Parada>> getListaDeParadasPorLinha(){
        ListDoubleLinked<Parada> listaParada = new ListDoubleLinked<>(); 
        for(Linha listaLinha : listaLinhas){
            listaParada = getParadasPorLinhaDeOnibus(listaLinha.getIdLinha());
            listaDeParadasPorLinha.put(listaLinha.getIdLinha(), listaParada);
        }
        return listaDeParadasPorLinha;
    }

    public void lerParadas() throws IOException {
        Path path = Paths.get("paradas.csv");
        try (BufferedReader br = Files.newBufferedReader(path,
                Charset.defaultCharset())) {
            String line = null;
            line = br.readLine();
            Scanner cs = new Scanner(line).useDelimiter(";");
            cs.nextLine();

            while ((line = br.readLine()) != null) {
                Scanner sc = new Scanner(line).useDelimiter("[ ]");
                sc.useDelimiter(";");
                String id = sc.next();
                int idParada = Integer.parseInt(id);
                String cod = sc.next();
                cod = cod.replace('"', ' ').replace(" ", "");
                int codigo = Integer.parseInt(cod);
                String longi = sc.next();
                longi = longi.replace(",", ".");
                double longitude = Double.parseDouble(longi);
                String lati = sc.next();
                lati = lati.replace(",", ".");
                double latitude = Double.parseDouble(lati);
                String terminal = sc.next();
                Parada parada = new Parada(idParada, codigo, longitude, latitude, terminal);
                listaParadas.add(parada);
            }

        } catch (IOException e) {
            System.err.format("Erro de E/S: %s%n", e);
        }
    }

    public void lerLinhas() throws IOException {
        Path path = Paths.get("linhas.csv");
        try (BufferedReader br = Files.newBufferedReader(path,
                Charset.defaultCharset())) {
            String line = null;
            line = br.readLine();
            Scanner cs = new Scanner(line).useDelimiter(";");
            cs.nextLine();
            while ((line = br.readLine()) != null) {
                Scanner sc = new Scanner(line).useDelimiter("[ ]");
                sc.useDelimiter(";");
                String id = sc.next();
                int idLinha = Integer.parseInt(id);
                String nome = sc.next();
                String codigo = sc.next();
                String tipo = sc.next();
                String tp = tipo.substring(1, 2);
                if (tp.equals("O")) {
                    Linha linha = new Linha(idLinha, nome, codigo, tipo);
                    listaLinhas.add(linha);
                }
            }
            listaLinhas.toString();
        } catch (IOException e) {
            System.err.format("Erro de E/S: %s%n", e);
        }
    }

    public void lerParadasLinhas() throws IOException {
        Path path = Paths.get("paradalinha.csv");
        try (BufferedReader br = Files.newBufferedReader(path,
                Charset.defaultCharset())) {
            String line = null;
            line = br.readLine();
            Scanner cs = new Scanner(line).useDelimiter(";");
            cs.nextLine();
            while ((line = br.readLine()) != null) {
                Scanner sc = new Scanner(line).useDelimiter("[ ]");
                sc.useDelimiter(";");
                String idL = sc.next();
                int idLinha = Integer.parseInt(idL);
                String idP = sc.next();
                int idParada = Integer.parseInt(idP);
                ParadaLinha paradaLinha = new ParadaLinha(idLinha, idParada);
                listaParadasLinhas.add(paradaLinha);
            }
        } catch (IOException e) {
            System.err.format("Erro de E/S: %s%n", e);
        }
    }    

    /**
     * 
     * @param idParada
     * @return 
     */
    public ListDoubleLinked<Linha> getLinhasDeOnibusPorParada(int idParada) {        
        for(Integer key: listaDeLinhasPorParada.keySet()){
            if(key == idParada)
                return listaDeLinhasPorParada.get(key);
        }
        return auxgetLinhasDeOnibusPorParada(idParada);
    }    
    
    public ListDoubleLinked<Linha> auxgetLinhasDeOnibusPorParada(int idParada){
        ListDoubleLinked<Linha> lista = new ListDoubleLinked<>();
        for (ParadaLinha listaParadaLinha : listaParadasLinhas) {
            if (listaParadaLinha.getIdParada() == idParada) {
                for (Linha listaLinha : listaLinhas) {
                    if (listaLinha.getIdLinha() == listaParadaLinha.getIdLinha()) {
                        lista.add(listaLinha);
                    }
                }
            }
        }
        listaDeLinhasPorParada.put(idParada, lista);
        return lista;
    }
    
    /**
     * 
     * @param idLinha
     * @return 
     */
    public ListDoubleLinked<Parada> getParadasPorLinhaDeOnibus(int idLinha) {
        for(Integer key: listaDeParadasPorLinha.keySet()){
            if(key == idLinha)
                return listaDeParadasPorLinha.get(key);
        }
        return getParadasPorLinhasDeOnibus(idLinha);
    }  
    
    private ListDoubleLinked<Parada> getParadasPorLinhasDeOnibus(int idLinha){
        ListDoubleLinked<Parada> lista = new ListDoubleLinked<>();
        for (ParadaLinha listaParadaLinha : listaParadasLinhas) {
            if (listaParadaLinha.getIdLinha() == idLinha) {
                for (Parada listaParada : listaParadas) {
                    if (listaParada.getIdParada() == listaParadaLinha.getIdParada()) {
                        lista.add(listaParada);
                    }
                }
            }
        }        
        listaDeParadasPorLinha.put(idLinha, lista);
        return lista;
    }    

    public String getLinhas() {
        return listaLinhas.toString();
    }

    public String getParadas() {
        return listaParadas.toString();
    }

    public String getParadasLinhas() {
        return listaParadasLinhas.toString();
    }
}
