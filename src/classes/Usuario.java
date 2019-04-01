/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;


public abstract class Usuario implements Serializable {

    /**
     * @param args the command line arguments
     */
    private static final long serialVersionUID = 123L;
    protected int id_usuario;
    protected String nome;
    protected String email;
    protected String senha;

    public Usuario (String nome, String email){
        this.nome = nome;
        this.email = email;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
