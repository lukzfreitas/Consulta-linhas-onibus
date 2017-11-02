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
public class Linha {
    private int idLinha;
    private String nome;
    private String codigo;
    private String tipo;

    public Linha(int id, String nome, String codigo, String tipo) {
        this.idLinha = id;
        this.nome = nome;
        this.codigo = codigo;
        this.tipo = tipo;
    }

    public int getIdLinha() {
        return idLinha;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
                return
                "\nLinha: "+this.idLinha+
                "\n Nome: "+this.nome+
                "\n CÃ³digo: "+this.codigo+
                "\n Tipo: "+this.tipo+
                "\n=================================================";

    }
    
    
    
    
}
