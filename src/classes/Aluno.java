/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;

/**
 *
 * @author aluno
 */
public class Aluno extends Usuario implements Serializable {
    private static final long serialVersionUID = 123L;
    private String matricula;
    private Turma turma;
    
    public Aluno (String nome, String matricula, String email){
        super(nome,email);
        this.matricula = matricula;
        //this.turma = turma;
    }
    
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
    
}

