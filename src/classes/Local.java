/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;

/**
 *
 * @author ariel
 */
public class Local implements Serializable {
    private static final long serialVersionUID = 123L;
    private int id_local;
    private String nome;

    public Local (int id_local, String nome){
        this.id_local = id_local;
        this.nome = nome;
        
    }
    
    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}