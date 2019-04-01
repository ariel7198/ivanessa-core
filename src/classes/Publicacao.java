/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.Serializable;

public class Publicacao implements Serializable {

    private static final long serialVersionUID = 123L;
    private int id_publicacao;
    private String titulo;
    private String descricao;
    private String data;
    private int status;
    private int tipo;
    private Local local;
    private File arquivo;

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }
    private int id_usuario_origem;
    private int id_usuario_final;
    private int situacao;
    private int confirmacao;
    private String nome_Usuario;

    public String getNome_Usuario() {
        return nome_Usuario;
    }

    public void setNome_Usuario(String nome_Usuario) {
        this.nome_Usuario = nome_Usuario;
    }
    private String localLiteral;

    public Publicacao(String titulo, String descricao, String data, int status, int tipo, int situacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.status = status;
        this.tipo = tipo;
        this.situacao = situacao;
    }

    public String getLocalLiteral() {
        return localLiteral;
    }

    public void setLocalLiteral(String localLiteral) {
        this.localLiteral = localLiteral;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public int getId_publicacao() {
        return id_publicacao;
    }

    public void setId_publicacao(int id_publicacao) {
        this.id_publicacao = id_publicacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    /**
     * @return the usuario_origem
     */
    public int getUsuario_origem() {
        return id_usuario_origem;
    }

    /**
     * @param usuario_origem the usuario_origem to set
     */
    public void setUsuario_origem(int usuario_origem) {
        this.id_usuario_origem = usuario_origem;
    }

    /**
     * @return the usuario_final
     */
    public int getUsuario_final() {
        return id_usuario_final;
    }

    /**
     * @param usuario_final the usuario_final to set
     */
    public void setUsuario_final(int usuario_final) {
        this.id_usuario_final = usuario_final;
    }

    public int getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(int confirmacao) {
        this.confirmacao = confirmacao;
    }

}
