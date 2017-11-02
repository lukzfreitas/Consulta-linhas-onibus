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
public class ParadaLinha {
    private int idLinha;
    private int idParada;

    public ParadaLinha(int idLinha, int idParada) {
        this.idLinha = idLinha;
        this.idParada = idParada;
    }

    public int getIdLinha() {
        return idLinha;
    }

    public int getIdParada() {
        return idParada;
    }

    @Override
    public String toString() {
        return "ParadaLinha{" + "idLinha=" + idLinha + ", idParada=" + idParada + '}';
    }  
    
}
