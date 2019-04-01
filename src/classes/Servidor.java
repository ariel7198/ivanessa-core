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
public class Servidor extends Usuario implements Serializable {
    private static final long serialVersionUID = 123L;
    private String funcao;

    public Servidor(String nome, String email, String funcao) {
        super(nome, email);
        this.funcao = funcao;

    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

}
