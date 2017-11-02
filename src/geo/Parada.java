/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package geo;

/**
 *
 * @author Lucas
 */
public class Parada {
    private int idParada;
    private int codigo;
    private double longitude;
    private double latitude;
    private String terminal;

    public Parada(
        int idParada,
        int codigo, 
        double longitude, 
        double latitude,             
        String terminal
    ) {
        this.idParada = idParada;
        this.codigo = codigo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.terminal = terminal;
    }   

    public int getIdParada() {
        return idParada;
    }

    public int getCodigo() {
        return codigo;
    }

    public double getLongitude() {
        return longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }   

    public String getTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
                return 
                
                "\n ID Parada: "+this.idParada+
                "\n CÃ³digo: "+this.codigo+
                "\n Longitude: "+this.longitude+
                "\n Latitude: "+this.latitude+
                "\n Terminal: "+this.terminal+
                "\n=================================================";

    }   
    
}
