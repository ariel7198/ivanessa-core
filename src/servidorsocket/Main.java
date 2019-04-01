/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import classes.Aluno;
import classes.Criptografia;
import classes.Local;
import classes.Publicacao;
import classes.Recados;
import classes.Servidor;
import classes.Turma;
import classes.JavaMailApp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Main {

    public static File diretorio_fotos;
    public static File diretorio_fotos_linux;

    public static void main(String[] args) throws SQLException {

        try {

            ServerSocket servidor = new ServerSocket(12345);
            //boolean ok = new java.io.File(System.getProperty("user.home"), "ifound").mkdirs();

            String diretorio_home = System.getProperty("user.home");
            //diretorio_fotos = new File(diretorio_home + "\\ifoundImagens");
            diretorio_fotos_linux = new File(diretorio_home + "/ifoundImagens");
            //diretorio_fotos.mkdir();

            if (!diretorio_fotos_linux.exists()) {
                System.out.println("Diretório não existe. Vai criar agora");

                diretorio_fotos.mkdirs();
            } else {

                System.out.println("Diretório já existe.");
            }
            System.out.println("Servidor inicializado. Aguardando conexao...");

            while (true) {
                // cria a thread do cliente a cada nova conexÃ£o
                Socket cliente = servidor.accept();
                System.out.println("Conexão com " + cliente.getInetAddress().getHostAddress());
                TrataCliente trataCliente = new TrataCliente(cliente);

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    //0 ENCONTRADO
    //1 PERDIDO
}

class TrataCliente extends Thread {

    Socket cliente;
    public static Statement st;
    public static ResultSet rs;
    public static Conexao c;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;

    String serverName = "localhost"; //caminho do servidor
    String mydataBase = "ifound"; //nome do banco
    String url = "jdbc:mysql://" + serverName + "/" + mydataBase;
    String username = "root"; // nome do usuario
    String password = "root"; //senha
    Connection conexao = DriverManager.getConnection(url, username, password);
    //conexao = DriverManager.getConnection(url, username, password);

    public TrataCliente(Socket cliente) throws SQLException, SocketException {
        this.cliente = cliente;
        //cliente.setSoTimeout(15000);
        this.start();

    }

    public void run() {
        Scanner entrada = new Scanner(System.in);
        try {
            System.out.println("Cliente conectado ");
            // Recebendo mensagem do cliente
            in = new ObjectInputStream(this.cliente.getInputStream());
            out = new ObjectOutputStream(this.cliente.getOutputStream());
            st = conexao.createStatement();

            String mensagemRecebida = (String) in.readObject();
            System.out.println("recebido: " + mensagemRecebida);
            String mensagemEnviar = "";
            while (!mensagemRecebida.equals("fim")) {
                System.out.println("Recebido do cliente: " + mensagemRecebida);

                //teste das ações solicitadas
                /* ---------
                --------- LOGIN -----------
                 */
                if (mensagemRecebida.equals("login")) { //login de usuario
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar); //envia mensagem de confirmação
                    System.out.println("Enviou o Ok");
                    mensagemRecebida = (String) in.readObject(); //recebe o matricula
                    System.out.println("Recebido no if: " + mensagemRecebida);

                    if (!mensagemRecebida.equals("")) { //testa se o login não é vazio
                        String usuarioLogin = mensagemRecebida; //recebe apenas o login
                        out.writeObject(mensagemEnviar); //envia o Ok novamente para aguardar pela senha
                        //DataInputStream intin = new DataInputStream(this.cliente.getInputStream());
                        //int resultado = intin.readInt();
                        mensagemRecebida = (String) in.readObject(); //recebe a senha

                        System.out.println("Senha: " + mensagemRecebida);
                        if (!mensagemRecebida.equals("")) { //testa a senha
                            String usuarioSenha = mensagemRecebida; //armazena a senha em uma variavel
                            out.writeObject(mensagemEnviar);

                            System.out.println("Chegou na função");
                            System.out.println("Enviado para a funcao: ");
                            System.out.println("usuario: " + usuarioLogin);
                            System.out.println("senha: " + usuarioSenha);
                            int result = usuarioLogin(usuarioLogin, usuarioSenha);
                            if (result == 1) {
                                System.out.println("Resultado da função: " + result);
                                //out.flush();
                                //out.writeObject(mensagemEnviar);
                                //out.println ("Enviou: "+mensagemEnviar);
                            } else if (result == 0) {
                                System.out.println("Nao encontrou. ");
                            }
                        }
                    }
                }

                if (mensagemRecebida.equals("recuperaSenha")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    System.out.println("Enviou a confirmação");
                    mensagemRecebida = (String) in.readObject();
                    System.out.println("Recebeu o usuario");
                    int result = recuperaSenha(mensagemRecebida);
                    if (result == 1) {
                        System.out.println("Mensagem enviada");
                        out.writeObject(mensagemEnviar);
                    } else if (result == 0) {
                        System.out.println("Ocorreu um erro no envio da mensagem.");
                        mensagemEnviar = "erro";
                        out.writeObject(mensagemEnviar);
                    } else if (result == 2) {
                        System.out.println("Dados nao encontrados. Pesquisou por: " + mensagemRecebida);
                        mensagemEnviar = "naoEncontrado";
                        out.writeObject(mensagemEnviar);
                    } else if (result == 3) {
                        System.out.println("Algo deu errado na consulta");
                        mensagemEnviar = ("erro");
                        out.writeObject(mensagemEnviar);
                    }

                }
                if (mensagemRecebida.equals("alteraSenha")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    int id = Integer.parseInt(mensagemRecebida);
                    out.writeObject(mensagemEnviar);
                    String senhaAntigaCrip = (String) in.readObject();
                    out.writeObject(mensagemEnviar);
                    String senhaNovaCrip = (String) in.readObject();
                    int result = alteraSenha(id, senhaAntigaCrip, senhaNovaCrip);
                    if (result == 1) {
                        out.writeObject(mensagemEnviar);
                    } else if (result == 0) {
                        mensagemEnviar = "naoEncontrado";
                        out.writeObject(mensagemEnviar);
                    } else if (result == 3) {
                        mensagemEnviar = "erroConsulta";
                        out.writeObject(mensagemEnviar);
                    } else if (result == 2) {
                        mensagemEnviar = "erroUpdate";
                        out.writeObject(mensagemEnviar);
                    }
                }
                if (mensagemRecebida.equals("excluirUsuario")) {
                    mensagemEnviar = "ok";
                    System.out.println("Entrou no excluir");
                    out.writeObject(mensagemEnviar);
                    System.out.println("Mandou ok");
                    mensagemRecebida = (String) in.readObject();
                    int result = excluirUsuario(mensagemRecebida);
                    if (result == 1) {
                        System.out.println("Resultado = 1");
                        out.writeObject(mensagemEnviar);

                    } else if (result == 0) {
                        System.out.println("Resultado = 0");
                        mensagemEnviar = "erro";
                        out.writeObject(mensagemEnviar);
                    }
                }


                /* ---------
                --------- CADASTRO ALUNO -----------
                 */
                if (mensagemRecebida.equals("cadastroAluno")) { //função de cadastro do aluno
                    mensagemEnviar = "ok"; //escreve mensagem de confirmacao para cliente
                    out.writeObject(mensagemEnviar); //envia mensagem de confirmação
                    System.out.println("Enviou a confirmação.");

                    try {
                        System.out.println("Vai receber o aluno.");
                        Aluno a = (Aluno) in.readObject(); //lê o objeto recebido do cliente
                        System.out.println("Recebeu o aluno.");
                        out.writeObject(mensagemEnviar);
                        System.out.println("Enviou o ok.");
                        int fk_turma = (int) in.readObject();
                        Turma t = buscaTurma(fk_turma);
                        System.out.println("Turma dada para o aluno:" + t.getNome());
                        a.setTurma(t);
                        int result = cadastraAluno(a); //chama a função de cadastro de aluno
                        if (result == 1) { //cadastro funcionou, enviar confirmacao para usuario
                            out.writeObject(mensagemEnviar);
                        } else if (result == 0) { //cadastro não funcionou, enviar falha para usuario
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    } catch (IOException ex) {
                        System.out.println("Um erro ocorreu: " + ex);

                    } catch (SQLException ex) {
                        Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                /* ---------
                --------- CADASTRO SERVIDOR -----------
                 */
                if (mensagemRecebida.equals("cadastroServidor")) { //função de cadastro do servidor
                    mensagemEnviar = "ok";                        //escreve mensagem de confirmacao para cliente
                    out.writeObject(mensagemEnviar);              //envia mensagem de confirmação

                    try {
                        Servidor s = (Servidor) in.readObject();  //le o objeto enviado e cria um objeto servidor
                        int result = cadastraServidor(s);                    //chama a função que cadastra o servidor
                        if (result == 1) { //cadastro deu certo
                            out.writeObject(mensagemEnviar);  //envia a confirmação
                        } else if (result == 0) { //cadastro nao deu certo 
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar); //envia codigo de erro
                        }
                    } catch (IOException ex) {
                        System.out.println("Um erro ocorreu: " + ex);
                    }
                }
                /* ---------
                --------- CADASTRO PERDIDO E ENCONTRADO -----------
                 */
                if (mensagemRecebida.equals("cadastroPublicacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    try {
                        Publicacao p = (Publicacao) in.readObject();
                        out.writeObject(mensagemEnviar);
                        int spinner = (int) in.readObject();
                        Local l = buscaLocal(spinner);
                        p.setLocal(l);
                        out.writeObject(mensagemEnviar);
                        //recebimento da imagem

                        /*BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(cliente.getInputStream()));
                        JFrame frame = new JFrame();
                        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
                        frame.pack();
                        frame.setVisible(true);

                        File foto = new File("\\Teste\\" + gerarNome() + ".png"); */
                        //ImageIO.write(img, "PNG", foto);
                        int result = 2;
                        if (p.getTipo() == 0) { // 0 = perdido
                            result = cadastraPublicacao(p, 0); //envia o objeto e o numero 0 para indicar que é um objeto PERDIDO
                        } else if (p.getTipo() == 1) { // 1 = encontrado
                            result = cadastraPublicacao(p, 1); //envia o objeto e o numero 1 para indiciar que é um objeto ENCONTRADO
                        }

                        if (result == 1) {
                            out.writeObject(mensagemEnviar);
                        } else if (result == 0) {
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    } catch (IOException ex) {
                        System.out.println("Um erro ocorreu: " + ex);
                    }

                }
                if (mensagemRecebida.equals("cadastroPublicacaoServidor")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    try {
                        Publicacao p = (Publicacao) in.readObject();
                        out.writeObject(mensagemEnviar);
                        int spinner = (int) in.readObject();
                        Local l = buscaLocal(spinner);
                        p.setLocal(l);
                        out.writeObject(mensagemEnviar);

                        int result = 2;
                        if (p.getTipo() == 0) { // 0 = perdido
                            result = cadastraPublicacaoServidor(p, 0); //envia o objeto e o numero 0 para indicar que é um objeto PERDIDO
                        } else if (p.getTipo() == 1) { // 1 = encontrado
                            result = cadastraPublicacaoServidor(p, 1); //envia o objeto e o numero 1 para indiciar que é um objeto ENCONTRADO
                        }

                        if (result == 1) {
                            out.writeObject(mensagemEnviar);
                        } else if (result == 0) {
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    } catch (IOException ex) {
                        System.out.println("Um erro ocorreu: " + ex);
                    }

                }
                if (mensagemRecebida.equals("cadastraMensagem")) {
                    System.out.println("Entrou no cadastro mensagem");
                    mensagemEnviar = "ok";
                    int result = 0;
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    if (mensagemRecebida.equals("aluno")) {
                        System.out.println("Recebeu aluno");
                        out.writeObject(mensagemEnviar);
                        String matricula = (String) in.readObject();
                        out.writeObject(mensagemEnviar);
                        String mensagem = (String) in.readObject();
                        result = cadastraMensagem(matricula, mensagem, 1);
                    } else if (mensagemRecebida.equals("turma")) {
                        System.out.println("Recebeu turma");
                        out.writeObject(mensagemEnviar);
                        int id_turma = (int) in.readObject();
                        String turma = String.valueOf(id_turma);
                        out.writeObject(mensagemEnviar);
                        String mensagem = (String) in.readObject();
                        result = cadastraMensagem(turma, mensagem, 2);
                    }
                    if (result == 1) {
                        out.writeObject("ok");
                    } else if (result == 0) {
                        out.writeObject("erro");
                    }
                }
                if (mensagemRecebida.equals("apagaPublicacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    //mensagemRecebida = (String) in.readObject();
                    int id_pb = (int) in.readObject();
                    if (id_pb != 0) {
                        int result = apagaPublicacao(id_pb);
                        if (result == 1) {
                            out.writeObject(mensagemEnviar);
                        } else if (result == 0) {
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    }

                }

                /* ---------
                --------- LISTA DE OBJETOS ENCONTRADOS-----------
                 */
                if (mensagemRecebida.equals("listaEncontrados")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    if (!mensagemRecebida.equals("")) {

                        //define o tipo de lista desejada
                        if (mensagemRecebida.equals("ativos")) {
                            System.out.println("Lista ativos");
                            LinkedList<Publicacao> listaPublicacoes = listaEncontradosAtivosAceitos();
                            System.out.println("Chamou funcao e pegou lista");

                            if (listaPublicacoes != null) {
                                //int tamanhoLista = listaPublicacoes.size();
                                out.writeObject("ok");
                                out.flush();
                                //mensagemRecebida = (String) in.readObject();*/
                                out.writeObject(listaPublicacoes);
                                System.out.println("Lista nao vazia. Lista enviada com " + listaPublicacoes.size() + "itens. ");

                            } else {
                                System.out.println("A lista de publicaões está vazia.");
                                String saida = ("erro");
                                out.writeObject(saida);
                            }
                        }

                        if (mensagemRecebida.equals("finalizados")) {
                            //chamada do método dos finalizados
                        }
                        if (mensagemRecebida.equals("pendentes")) {
                            LinkedList<Publicacao> listaPublicacoes = listaPublicacoesPendentes(1);
                            out.writeObject(listaPublicacoes);
                        }

                    }

                }

                /* ---------
                --------- LISTA DE OBJETOS PERDIDOS -----------
                 */
                if (mensagemRecebida.equals("listaPerdidos")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    if (!mensagemRecebida.equals("")) {
                        if (mensagemRecebida.equals("ativos")) {
                            LinkedList<Publicacao> listaPublicacoes = listaPerdidosAtivosAceitos();
                            out.writeObject(listaPublicacoes);
                        }
                        if (mensagemRecebida.equals("finalizados")) {
                            //chamada do método
                        }
                        if (mensagemRecebida.equals("pendentes")) {
                            LinkedList<Publicacao> listaPublicacoes = listaPublicacoesPendentes(0);
                            out.writeObject(listaPublicacoes);
                        }
                    }
                }
                if (mensagemRecebida.equals("listaDupla")) { //duas listas, perdidos e achados para o android
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar); //envia o ok
                    LinkedList<Publicacao> listaEncontrados = listaEncontradosAtivosAceitos();
                    LinkedList<Publicacao> listaPerdidos = listaPerdidosAtivosAceitos();
                    mensagemEnviar = "achados"; //envia a mensagem para dizer que a primeira lista é de achados
                    out.writeObject(mensagemEnviar); //envia "achados"
                    out.flush(); //limpa o canal de saida
                    out.writeObject(listaEncontrados); //envia a lista de achados
                    mensagemRecebida = (String) in.readObject(); //espera o ok
                    if (mensagemRecebida.equals("ok")) { //testa o ok
                        out.writeObject("perdidos"); //avisa que vai enviar a lista de perdidos
                        out.writeObject(listaPerdidos); //envia a lista de perdidos
                        mensagemRecebida = (String) in.readObject(); //recebeo ok
                        if (mensagemRecebida.equals("ok")) {
                            System.out.println("Listas enviadas");
                        }
                    }
                }
                if (mensagemRecebida.equals("buscaLocal")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    if (!mensagemRecebida.equals("")) {
                        int id = Integer.parseInt(mensagemRecebida);
                        Local l = buscaLocal(id);
                        out.writeObject(l);

                    }
                }
                if (mensagemRecebida.equals("buscaNomeUsuario")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    int id = (int) in.readObject();
                    System.out.println("id recebido: " + id);
                    String result = getNomeUsuario(id);
                    if (!result.equals("erro")) {
                        out.writeObject(result);
                    } else {
                        out.writeObject(result);
                    }
                }
                if (mensagemRecebida.equals("buscaUsuario")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    System.out.println("Enviou o ok");
                    String pesquisa = (String) in.readObject();
                    System.out.println("Leu o dado recebido. Vai chamar a funcao. Recebeu: " + pesquisa);
                    getUsuario(pesquisa);
                }
                /* ---------
                --------- LISTA DE USUÁRIOS -----------
                 */
                if (mensagemRecebida.equals("listaUsuarios")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();
                    if (!mensagemRecebida.equals("")) {
                        ArrayList<Aluno> listaAlunos = getListaAlunos();
                        ArrayList<Servidor> listaServidores = getListaServidores();
                        //enviar listas
                    }
                }

                if (mensagemRecebida.equals("listaTurmas")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();

                    if (!mensagemRecebida.equals("")) {
                        ArrayList<Turma> listaTurmas = getListaTurmas();
                        if (!listaTurmas.isEmpty()) {
                            out.writeObject(listaTurmas);
                        } else {
                            System.out.println("Lista de turmas vazia.");
                        }
                    }
                }
                if (mensagemRecebida.equals("listaLocais")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    mensagemRecebida = (String) in.readObject();

                    if (mensagemRecebida.equals("ok")) {
                        ArrayList<Local> listaLocais = getListaLocais();
                        if (!listaLocais.isEmpty()) {
                            out.writeObject(listaLocais);
                        } else {
                            System.out.println("Lista de locais vazia.");
                            out.writeObject("vazio");
                        }

                    } else {
                        System.out.println("Esperava um Ok. Recebeu:" + mensagemRecebida);
                    }
                }

                if (mensagemRecebida.equals("listaRecados")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    int id_usuario = (int) in.readObject();
                    if (id_usuario != 0) {
                        out.writeObject(mensagemEnviar);
                        int id_turma = (int) in.readObject();
                        if (id_turma != 0) {
                            LinkedList<Recados> listaRecados = getListaRecados(id_usuario, id_turma);
                            out.writeObject(listaRecados);
                        }
                    }
                }
                if (mensagemRecebida.equals("aceitaPublicacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    int id = (int) in.readObject();
                    if (id != 0) {
                        int result = aceitaPublicacao(id);
                        if (result == 1) {
                            out.writeObject(mensagemEnviar);
                        } else {
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    } else {
                        System.out.println("Id inválido. Id = 0");
                    }
                }
                if (mensagemRecebida.equals("rejeitaPublicacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    int id = (int) in.readObject();
                    if (id != 0) {
                        int result = rejeitaPublicacao(id);
                        if (result == 1) {
                            out.writeObject(mensagemEnviar);
                        } else {
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    }
                }
                if (mensagemRecebida.equals("alteraSituacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    System.out.println("Enviou o ok");
                    int id_publicacao = (int) in.readObject();
                    System.out.println("Recebeu id da publicacao: " + id_publicacao);
                    int id_usuario_final = (int) in.readObject();
                    System.out.println("Recebeu id do usuario: " + id_usuario_final);
                    if (id_publicacao != 0 && id_usuario_final != 0) {
                        System.out.println("Recebeu os dois. Vai chamar a funcao");
                        int result = alteraSituacao(id_publicacao, id_usuario_final);
                        if (result == 1) {

                            System.out.println("Deu certo");
                            out.writeObject(mensagemEnviar);
                        } else if (result == 0) {
                            System.out.println("Nao deu certo");
                            mensagemEnviar = "erro";
                            out.writeObject(mensagemEnviar);
                        }
                    }

                }
                if (mensagemRecebida.equals("consultaPublicacoesUsuario")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    System.out.println("Enviou o ok");
                    int id_usuario = (int) in.readObject();
                    System.out.println("Recebeu o id: " + id_usuario);
                    LinkedList<Publicacao> listaPublicacoes = publicacoesUsuario(id_usuario);
                    if (listaPublicacoes.isEmpty()) {
                        System.out.println("Lista vazia");
                        out.writeObject("vazio");
                    } else {
                        System.out.println("Lista com elementos");
                        out.writeObject("ok");
                        out.writeObject(listaPublicacoes);
                    }

                }
                if (mensagemRecebida.equals("confirmaSituacao")) {
                    mensagemEnviar = "ok";
                    out.writeObject(mensagemEnviar);
                    System.out.println("Recebeu confirma situação e enviou ok");
                    int id_publicacao = (int) in.readObject();
                    System.out.println("recebeu os ids");
                    int result = confirmaSituacao(id_publicacao);
                    System.out.println("Pegou a função");
                    if (result == 1) {
                        out.writeObject(mensagemEnviar);
                        System.out.println("OK");
                    } else if (result == 0) {
                        mensagemEnviar = "erro";
                        out.writeObject(mensagemEnviar);
                        System.out.println("Erro");
                    }
                }

                //teste final do while
                System.out.println("Testou no final");
                mensagemRecebida = (String) in.readObject();
            }

            in.close();
            out.close();
            this.cliente.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException classNot) {

        } catch (SQLException ex) {
            Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* -------------
    ------------ FUNÇÕES ----------
     */
    public int usuarioLogin(String usuario, String senha) throws SQLException, IOException {
        System.out.println("Dentro da função");
        System.out.println("Senha recebida na função: " + senha);

        //String senhaDescript = classes.Criptografia.Descriptografa(senha);
        System.out.println("Consulta que vai ser feita: SELECT * FROM usuario WHERE matricula = '" + usuario + "' or email = '" + usuario + "' and senha ='" + senha + "'");
        st.execute("SELECT * FROM usuario WHERE matricula = '" + usuario + "' or email = '" + usuario + "' and senha = '" + senha + "'");
        rs = st.getResultSet();
        System.out.println("Fez a consulta");

        while (rs.next() == true) {
            System.out.println("Encontrou resultado");

            String id = rs.getString("id_usuario");
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String matricula = rs.getString("matricula");
            //String turma = rs.getString("fk_turma");
            String funcao = rs.getString("funcao");
            int turma_fk = Integer.parseInt(rs.getString("fk_turma"));
            int tipo = Integer.parseInt(rs.getString("tipo"));

            if (tipo == 0) { //instaciar aluno
                System.out.println("Entrou em aluno");
                Aluno alunoLogin = new Aluno(nome, matricula, email);
                alunoLogin.setId_usuario(Integer.parseInt(id));
                Turma t = buscaTurma(turma_fk);
                alunoLogin.setTurma(t);

                //alunoLogin.setMatricula(matricula);
                try {
                    System.out.println("Enviou aluno");

                    out.writeObject("ok aluno"); //confirmacaoo para enviar objeto
                    //System.out.println("mandou o ok usuario");
                    out.flush();
                    System.out.println("Nome: " + alunoLogin.getNome());
                    System.out.println("Email: " + alunoLogin.getEmail());
                    System.out.println("Matricula: " + alunoLogin.getMatricula());
                    System.out.println("Realmente mandou o objeto");
                    out.writeObject(alunoLogin);
                    out.flush();
                    return 1;

                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
                }
            } else if (tipo == 1) { //intanciar servidor
                System.out.println("Entrou em servidor");
                Servidor servidorLogin = new Servidor(nome, email, funcao);

                servidorLogin.setId_usuario(Integer.parseInt(id));
                servidorLogin.setSenha(senha);

                try {
                    System.out.println("Enviou servidor");

                    TrataCliente.out.writeObject("ok servidor");

                    out.flush();
                    out.writeObject(servidorLogin);
                    return 2;

                } catch (IOException ex) {
                    System.out.println("ErrLo: " + ex);
                    return 0;
                }
            }

        }
        System.out.println("Nao encontrou. ");
        out.writeObject("nao encontrado");
        return 0;
    }

    public int recuperaSenha(String usuario) {
        try {
            st.executeQuery("SELECT nome, email, senha from usuario where email = '" + usuario + "' or matricula = '" + usuario + "'");
            rs = st.getResultSet();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String senhaCriptografada = rs.getString("senha");
                String email = rs.getString("email");
                String senhaDescriptografada = Criptografia.Descriptografa(senhaCriptografada);
                int op = JavaMailApp.enviaEmailRecuperacao(nome, email, senhaDescriptografada);
                return op;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            return 3;
        }

    }

    public int alteraSenha(int id, String senhaAntiga, String novaSenha) {
        try {
            st.executeQuery("SELECT nome from usuario where id_usuario = " + id + " and senha = '" + senhaAntiga);
            rs = st.getResultSet();
            if (rs.next()) {
                int result = st.executeUpdate("UPDATE usuario set senha = '" + novaSenha + "' WHERE id_usuario = " + id + "");
                if (result == 1) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return 3;
        }
    }

    public int cadastraAluno(Aluno aluno) throws SQLException {
        try {
            //variaveis com dados recebidos por parametro
            String nome = aluno.getNome();
            String email = aluno.getEmail();
            String senha = aluno.getSenha();
            String matricula = aluno.getMatricula();
            Turma turma = aluno.getTurma();
            System.out.println("Nome recebido: " + nome);
            System.out.println("Email recebido: " + email);
            System.out.println("Senha recebida: " + senha);
            System.out.println("Matricula recebida: " + matricula);
            System.out.println("Turma recebida: " + turma.getNome());

            st.executeUpdate("INSERT INTO usuario (nome,email,senha,matricula,fk_turma) VALUES ('" + nome + "', '" + email + "', '" + senha + "', '" + matricula + "', " + turma.getId_turma() + ")");
            System.out.println("Cadastro efetuado.");
            return 1;

        } catch (SQLException ex) {
            System.out.println("Erro no cadastro: " + ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int cadastraServidor(Servidor servidor) {
        try {
            //variaveis com dados recebidos por parametro
            String nome = servidor.getNome();
            String email = servidor.getEmail();
            String senha = servidor.getSenha();
            String funcao = servidor.getFuncao();
            System.out.println("Nome recebido: " + nome);
            System.out.println("Email recebido: " + email);
            System.out.println("Senha recebida: " + senha);
            System.out.println("Função recebida: " + funcao);

            st.executeUpdate("INSERT INTO usuario (nome,email,senha,tipo, funcao) VALUES ('" + nome + "', '" + email + "', '" + senha + "', 1, '" + funcao + "')");
            System.out.println("Cadastro efetuado.");
            return 1;

        } catch (SQLException ex) {
            System.out.println("Erro no cadastro: " + ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int cadastraPublicacao(Publicacao p, int tipo_recebido) {
        try {
            //variaveis com dados recebidos por parametro

            String titulo = p.getTitulo();
            String descricao = p.getDescricao();
            String data = p.getData(); //formato da data é AAAA-MM-DD

            int status = p.getStatus();
            int tipo = tipo_recebido;
            if (tipo != 0 && tipo != 1) { //testa se o tipo de publicação enviado é válido
                System.out.println("O tipo de publicação é inválido.");
                return 0; //se nao for nenhum dos numeros válidos, finaliza a função e retorna o erro
            }
            int fk_usuario = p.getUsuario_origem();
            Local local = p.getLocal(); //pega o local enviado pelo objeto
            int situacao = 0;
            String nome_foto = "";
            /*try {
                nome_foto = gerarNome();
                System.out.println("Gerou o nome: " + nome_foto);
                //File foto_objeto = p.getArquivo();
                BufferedImage img;
                img = ImageIO.read(p.getArquivo());
                File foto = new File(Main.diretorio_fotos + "\\" + nome_foto + ".png");
                ImageIO.write(img, "PNG", foto);

            } catch (IOException ex) {
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Nenhuma foto recebida");
            } */

            System.out.println("Titulo recebido: " + titulo); //top titulo
            System.out.println("Descricao recebida: " + descricao); //top descrição 
            System.out.println("Data recebida: " + data); //datop
            System.out.println("Status recebido: " + status); //statop
            System.out.println("Tipo recebido: " + tipo); //tipo top
            System.out.println("Usuario origem recebido: " + fk_usuario); //fk_top
            System.out.println("Local recebido: " + local.getNome()); //lolcal top
            String sql = "INSERT INTO publicacao (titulo,descricao,data,status,tipo,fk_usuario,fk_local,situacao, confirmacao) VALUES ('" + titulo + "', '" + descricao + "', '" + data + "', " + status + ", " + tipo + ", " + fk_usuario + ", " + local.getId_local() + "," + situacao + ", 0)";
//            System.out.println ("SQL : "+sql)
            st.executeUpdate("INSERT INTO publicacao (titulo,descricao,data,status,tipo,fk_usuario,fk_local,situacao, confirmacao) VALUES ('" + titulo + "', '" + descricao + "', '" + data + "', " + status + ", " + tipo + ", " + fk_usuario + ", " + local.getId_local() + "," + situacao + ", 0)");
            System.out.println("Cadastro efetuado.");

            return 1;
        } catch (SQLException ex) {
            System.out.println("Erro no cadastro: " + ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int cadastraPublicacaoServidor(Publicacao p, int tipo_recebido) {
        try {
            //variaveis com dados recebidos por parametro

            String titulo = p.getTitulo();
            String descricao = p.getDescricao();
            String data = p.getData(); //formato da data é AAAA-MM-DD

            int status = p.getStatus();
            int tipo = tipo_recebido;
            if (tipo != 0 && tipo != 1) { //testa se o tipo de publicação enviado é válido
                System.out.println("O tipo de publicação é inválido.");
                return 0; //se nao for nenhum dos numeros válidos, finaliza a função e retorna o erro
            }
            int fk_usuario = p.getUsuario_origem();
            Local local = p.getLocal(); //pega o local enviado pelo objeto
            int situacao = 0;
            String nome_foto = "";
            try {
                nome_foto = gerarNome();
                System.out.println("Gerou o nome: " + nome_foto);
                //File foto_objeto = p.getArquivo();
                BufferedImage img;
                img = ImageIO.read(p.getArquivo());
                File foto = new File(Main.diretorio_fotos + "\\" + nome_foto + ".png");
                ImageIO.write(img, "PNG", foto);

            } catch (IOException ex) {
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Nenhuma foto recebida");
            }

            System.out.println("Titulo recebido: " + titulo); //top titulo
            System.out.println("Descricao recebida: " + descricao); //top descrição 
            System.out.println("Data recebida: " + data); //datop
            System.out.println("Status recebido: " + status); //statop
            System.out.println("Tipo recebido: " + tipo); //tipo top
            System.out.println("Usuario origem recebido: " + fk_usuario); //fk_top
            System.out.println("Local recebido: " + local.getNome()); //lolcal top
            String sql = "INSERT INTO publicacao (titulo,descricao,local_foto,data,status,tipo,fk_usuario,fk_local,situacao, confirmacao) VALUES ('" + titulo + "', '" + descricao + "', '" + nome_foto + "', '" + data + "', " + status + ", " + tipo + ", " + fk_usuario + ", " + local.getId_local() + "," + situacao + ", 0)";
//            System.out.println ("SQL : "+sql)
            st.executeUpdate("INSERT INTO publicacao (titulo,descricao,local_foto,data,status,tipo,fk_usuario,fk_local,situacao, confirmacao) VALUES ('" + titulo + "', '" + descricao + "', '" + nome_foto + "','" + data + "', " + status + ", " + tipo + ", " + fk_usuario + ", " + local.getId_local() + "," + situacao + ", 0)");
            System.out.println("Cadastro efetuado.");

            return 1;
        } catch (SQLException ex) {
            System.out.println("Erro no cadastro: " + ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public LinkedList<Publicacao> listaEncontradosAtivosAceitos() {
        LinkedList<Publicacao> listaPublicacoesEncontrados = new LinkedList<Publicacao>();
        int id;
        String titulo;
        String descricao;
        String data;
        int status;
        int situacao;
        int tipo;
        int id_usuario_origem;
        int fk_local;
        Local lc;
        String local_literal;
        String usuario_nome;
        System.out.println("Entrou na funcao de encontrados ativos");
        try {
            st.executeQuery("SELECT pb.id_publicacao, pb.titulo, pb.descricao, pb.data, pb.status, pb.situacao, pb.tipo, pb.fk_usuario, pb.fk_local, pb.fk_usuario_final, lc.nome as local_nome, us.nome as usuario_nome FROM publicacao pb, locais lc, usuario us WHERE lc.id_local = pb.fk_local and pb.`status` = 1 AND pb.fk_usuario = us.id_usuario AND pb.tipo = 1 and pb.confirmacao = 0 and pb.fk_usuario = us.id_usuario order by data desc");
            rs = st.getResultSet();
            System.out.println("Fez a consulta");

            while (rs.next()) {
                System.out.println("Achou um resultado");
                //dados da publicacao
                id = Integer.parseInt(rs.getString("id_publicacao"));
                titulo = rs.getString("titulo");
                descricao = rs.getString("descricao");
                data = rs.getString("data");
                status = Integer.valueOf(rs.getString("status"));
                situacao = Integer.valueOf(rs.getString("situacao"));
                tipo = Integer.valueOf(rs.getString("tipo"));
                id_usuario_origem = Integer.valueOf(rs.getString("fk_usuario"));
                fk_local = Integer.parseInt(rs.getString("fk_local"));
                local_literal = rs.getString("local_nome");
                usuario_nome = rs.getString("usuario_nome");
                int id_usuario_final = rs.getInt("fk_usuario_final");

                //String nome_usuario = rs.getString("nome"); //string com o nome do usuário // descobrir como fazer com o objeto depois
                //lc = null;
                //lc = buscaLocal(fk_local);
                System.out.println("chamou a funcao de busca local");

                Publicacao pb = new Publicacao(titulo, descricao, data, status, tipo, situacao);
                pb.setId_publicacao(id);
                //pb.setArquivo();
                pb.setNome_Usuario(usuario_nome);
                pb.setUsuario_origem(id_usuario_origem);
                pb.setLocalLiteral(local_literal);
                pb.setUsuario_final(id_usuario_final);

                //pb.setLocal(lc);
                listaPublicacoesEncontrados.add(pb);
                System.out.println("Item adicionado na lista. ");

            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return listaPublicacoesEncontrados;

        }
        System.out.println("Lista devolvida com: " + listaPublicacoesEncontrados.size());
        return listaPublicacoesEncontrados;
    }

    public LinkedList<Publicacao> listaPerdidosAtivosAceitos() {
        LinkedList<Publicacao> listaPublicacoesPerdidos = new LinkedList<Publicacao>();
        int id;
        String titulo;
        String descricao;
        String data;
        int status;
        int situacao;
        int tipo;
        int id_usuario_origem;
        int fk_local;
        //Local lc;
        String local_literal;
        String usuario_nome;
        System.out.println("Entrou na funcao de perdidos ativos");
        try {
            st.executeQuery("SELECT pb.id_publicacao, pb.titulo, pb.descricao, pb.data, pb.status, pb.situacao, pb.tipo, pb.fk_usuario, pb.fk_local, pb.fk_usuario_final, lc.nome as local_nome, us.nome as usuario_nome FROM publicacao pb, locais lc, usuario us WHERE lc.id_local = pb.fk_local and pb.`status` = 1 AND pb.fk_usuario = us.id_usuario AND pb.tipo = 0 and pb.confirmacao = 0 and pb.fk_usuario = us.id_usuario order by data desc");
            rs = st.getResultSet();
            System.out.println("Fez a consulta");

            while (rs.next()) {
                System.out.println("Achou um resultado");
                //dados da publicacao
                id = Integer.parseInt(rs.getString("id_publicacao"));
                titulo = rs.getString("titulo");
                descricao = rs.getString("descricao");
                data = rs.getString("data");
                status = Integer.valueOf(rs.getString("status"));
                situacao = Integer.valueOf(rs.getString("situacao"));
                tipo = Integer.valueOf(rs.getString("tipo"));
                id_usuario_origem = Integer.valueOf(rs.getString("fk_usuario"));
                fk_local = Integer.parseInt(rs.getString("fk_local"));
                local_literal = rs.getString("local_nome");
                usuario_nome = rs.getString("usuario_nome");
                int usuario_final = rs.getInt("fk_usuario_final");

                //lc = null;
                //lc = buscaLocal(fk_local);
                Publicacao pb = new Publicacao(titulo, descricao, data, status, tipo, situacao);
                pb.setId_publicacao(id);
                pb.setNome_Usuario(usuario_nome);
                pb.setUsuario_origem(id_usuario_origem);
                pb.setLocalLiteral(local_literal);
                pb.setUsuario_final(usuario_final);

                //pb.setLocal(lc);
                listaPublicacoesPerdidos.add(pb);
                System.out.println("Item adicionado na lista. ");

            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return listaPublicacoesPerdidos;

        }
        System.out.println("Lista devolvida com: " + listaPublicacoesPerdidos.size());
        return listaPublicacoesPerdidos;
    }

    public LinkedList<Publicacao> listaPublicacoesPendentes(int tipo_publicacao) {
        LinkedList<Publicacao> listaPublicacoes = new LinkedList<Publicacao>();
        int id;
        String titulo;
        String descricao;
        String data;
        String local_literal;
        int status;
        int situacao;
        int tipo;
        int id_usuario_origem;
        int fk_local;
        String local_foto;
        String usuario_nome;
        //Local lc;
        System.out.println("Antes de entrar no treco: ");

        try {
            if (tipo_publicacao == 1) {
                System.out.println("fez a consulta por achados");
                st.executeQuery("SELECT p.id_publicacao, p.local_foto, p.titulo, p.descricao, p.data, p.status, p.tipo, p.fk_usuario, p.fk_local, p.situacao, l.nome as nome_local, us.nome as usuario_nome FROM publicacao p, locais l, usuario us where p.fk_local = l.id_local  AND p.status = 0 AND p.tipo = 1 and p.fk_usuario = us.id_usuario");
            } else if (tipo_publicacao == 0) {
                System.out.println("fez a consulta por perdidos");
                st.executeQuery("SELECT p.id_publicacao, p.local_foto, p.titulo, p.descricao, p.data, p.status, p.tipo, p.fk_usuario, p.fk_local, p.situacao, l.nome as nome_local, us.nome as usuario_nome FROM publicacao p, locais l, usuario us where p.fk_local = l.id_local  AND p.status = 0 AND p.tipo = 0 and p.fk_usuario = us.id_usuario");
            }
            System.out.println("Fez a consulta");
            rs = st.getResultSet();
            while (rs.next()) {
                System.out.println("Entrou no while");
                //dados da publicacao
                id = Integer.parseInt(rs.getString("id_publicacao"));
                System.out.println("Deu um id: " + id);

                titulo = rs.getString("titulo");
                System.out.println("Deu um titulo: " + titulo);

                descricao = rs.getString("descricao");
                System.out.println("Deu uma descricao: " + descricao);

                data = rs.getString("data");
                System.out.println("Deu uma data: " + data);

                status = Integer.valueOf(rs.getString("status"));
                System.out.println("Deu um status: " + status);

                situacao = Integer.valueOf(rs.getString("situacao"));
                System.out.println("Deu uma situacao: " + situacao);

                tipo = Integer.valueOf(rs.getString("tipo"));
                System.out.println("Deu um tipo: " + tipo
                );
                id_usuario_origem = Integer.valueOf(rs.getString("fk_usuario"));
                System.out.println("Deu um fk_usuario: " + id_usuario_origem);

                fk_local = Integer.parseInt(rs.getString("fk_local"));
                System.out.println("Deu um local: " + fk_local);

                local_literal = rs.getString("nome_local");
                System.out.println("Deu um local literal: " + local_literal);
                usuario_nome = rs.getString("usuario_nome");

                local_foto = rs.getString("local_foto");

                System.out.println("Achou um resultado");
                //String nome_usuario = rs.getString("nome"); //string com o nome do usuário // descobrir como fazer com o objeto depois
                //lc = null;
                //lc = buscaLocal(fk_local);

                System.out.println("Local nao é null");
                Publicacao pb = new Publicacao(titulo, descricao, data, status, tipo, situacao);
                pb.setId_publicacao(id);
                System.out.println("Vai procurar pela foto em: " + Main.diretorio_fotos + "\\" + local_foto + ".png");
                File foto = new File(Main.diretorio_fotos + "\\" + local_foto + ".png");

                pb.setArquivo(foto);
                pb.setUsuario_origem(id_usuario_origem);
                pb.setLocalLiteral(local_literal);
                pb.setNome_Usuario(usuario_nome);
                //pb.setLocal(lc);
                listaPublicacoes.add(pb);

                System.out.println("Achou algo");

            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return listaPublicacoes;
        }

        System.out.println("Tamanho da lista: " + listaPublicacoes.size());
        return listaPublicacoes;

    }
    //public LinkedList<Publicacao> listaEncontradosAtivosAceitos

    // -------- LISTA DOS LOCAIS CADASTRADOS ------- //
    public ArrayList<Local> getListaLocais() {
        ArrayList<Local> listaLocais = new ArrayList<>();
        try {
            st.executeQuery("SELECT * FROM locais");
            rs = st.getResultSet();
            while (rs.next() == true) {
                int id_local = Integer.parseInt(rs.getString("id_local")); //pega o id do banco e transforma em int
                String nome = rs.getString("nome");
                Local l = new Local(id_local, nome); //cria o objeto com o id e o nome
                System.out.println("Nome: " + nome);
                System.out.println("Id: " + id_local);
                listaLocais.add(l);
            }

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        System.out.println("Tamanho da lista de locais: " + listaLocais.size());
        return listaLocais;
    }

    public String getNomeUsuario(int id_usuario) {
        String nomeUsuario;
        try {
            st.executeQuery("SELECT nome from usuario where id_usuario = " + id_usuario);
            rs = st.getResultSet();
            while (rs.next()) {
                nomeUsuario = rs.getString("nome");
                return nomeUsuario;
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        return "erro";
    }

    public void getUsuario(String usuario) {
        System.out.println("Entrou na pesquisa");
        try {
            System.out.println("Vai fazer o select");
            st.executeQuery("SELECT * FROM USUARIO where matricula='" + usuario + "' or email = '" + usuario + "'");
            System.out.println("Fez o select");
            rs = st.getResultSet();
            while (rs.next()) {
                System.out.println("Encontrou resultado");
                String nome = rs.getString("nome");
                System.out.println("Nome: " + nome);
                String email = rs.getString("email");
                System.out.println("Email: " + email);
                int tipo = rs.getInt("tipo");
                int id = rs.getInt("id_usuario");
                if (tipo == 0) {
                    System.out.println("Entrou no tipo 1");
                    String matricula = rs.getString("matricula");
                    int turma = rs.getInt("fk_turma");
                    System.out.println("Vai entrar no busca turma");
                    Turma t = buscaTurma(turma);
                    out.writeObject("ok aluno");
                    Aluno a = new Aluno(nome, matricula, email);
                    a.setId_usuario(id);
                    a.setTurma(t);
                    out.writeObject(a);
                    System.out.println("Enviou o aluno");
                } else if (tipo == 1) {
                    System.out.println("Entrou no tipo 2");
                    out.writeObject("ok servidor");
                    String funcao = rs.getString("funcao");
                    Servidor s = new Servidor(nome, email, funcao);
                    s.setId_usuario(id);
                    out.writeObject(s);
                }

            }
        } catch (SQLException ex) {
            System.out.println("Erro na consulta: " + ex);
        } catch (IOException ex) {
            System.out.println("Erro IO: " + ex);
            System.out.println("Ta saindo da funcao");
        }
    }

    public ArrayList<Aluno> getListaAlunos() {
        ArrayList<Turma> listaTurmas = new ArrayList<>();
        listaTurmas = getListaTurmas();
        ArrayList<Aluno> listaAlunos = new ArrayList<>();
        try {
            st.execute("SELECT * FROM usuario WHERE tipo = 0");
            rs = st.getResultSet();
            while (rs.next()) {
                int id_usuario = Integer.parseInt(rs.getString("id_usuario"));
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String matricula = rs.getString("matricula");
                int fk_turma = Integer.parseInt(rs.getString("fk_turma"));
                Turma t = null;
                for (int x = 0; x < listaTurmas.size(); x++) {
                    t = listaTurmas.get(x);
                    int id_turma = t.getId_turma();

                    if (id_turma == fk_turma) {
                        t = listaTurmas.get(x);
                    }
                }
                Aluno a = new Aluno(nome, matricula, email);
                a.setTurma(t);

            }
        } catch (SQLException ex) {

        }
        return null;
    }

    public ArrayList<Servidor> getListaServidores() {
        ArrayList<Servidor> listaServidores = new ArrayList<>();

        try {
            st.execute("SELECT * FROM usuario where tipo = 1");
            rs = st.getResultSet();

            while (rs.next()) {
                int id_usuario = Integer.parseInt(rs.getString("id_usuario"));
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String funcao = rs.getString("funcao");

                Servidor s = new Servidor(nome, email, funcao);
                listaServidores.add(s);
            }
        } catch (SQLException ex) {

        }
        return listaServidores;
    }

    public ArrayList<Turma> getListaTurmas() {
        ArrayList<Turma> listaTurmas = new ArrayList<>();
        try {
            st.execute("SELECT * FROM turma");
            rs = st.getResultSet();
            while (rs.next()) {
                int id_turma = Integer.parseInt(rs.getString("id_turma"));
                String nome = rs.getString("nome");
                Turma t = new Turma(nome);
                t.setId_turma(id_turma);

                listaTurmas.add(t);
            }

        } catch (SQLException ex) {
            System.out.println("Erro no select");
        }
        return listaTurmas;
    }

    public LinkedList<Recados> getListaRecados(int id_usuario, int id_turma) {
        LinkedList<Recados> listaRecados = new LinkedList<>();

        try {
            st.executeQuery("SELECT * FROM recados where fk_aluno = " + id_usuario + " or fk_turma = " + id_turma + " order by data desc");
            rs = st.getResultSet();
            while (rs.next()) {
                int id_recado = Integer.parseInt(rs.getString("id_recados"));
                String data = rs.getString("data");
                String conteudo = rs.getString("conteudo");
                int fk_turma = Integer.parseInt(rs.getString("fk_turma"));
                int fk_aluno = Integer.parseInt(rs.getString("fk_aluno"));

                System.out.println("ID recebido: " + id_recado);
                System.out.println("Data recebida: " + data);
                System.out.println("Conteudo recebido: " + conteudo);
                System.out.println("FK turma recebido: " + fk_turma);
                System.out.println("FK aluno recebido: " + fk_aluno);

                Recados r = new Recados(data, conteudo);
                r.setFk_aluno(fk_aluno);
                r.setFk_turma(fk_turma);
                listaRecados.add(r);
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        return listaRecados;
    }

    public Local buscaLocal(int fk_local) { //retorna o objeto do local de acordo com o id enviado

        ArrayList<Local> listaBusca = getListaLocais();
        Local lc = null;
        if (listaBusca != null) {
            for (int x = 0; x < listaBusca.size(); x++) {
                lc = listaBusca.get(x);
                int id_local = lc.getId_local();
                System.out.println("Comparou o local " + fk_local + " com o local" + id_local);

                if (id_local == fk_local) {
                    System.out.println("Encontrou o local.");
                    System.out.println("Vai retornar o local:" + lc.getNome());

                    return lc;
                }

            }
        } else {
            System.out.println("Lista vazia ");
        }
        return lc;
    }

    public Turma buscaTurma(int fk_turma) {

        ArrayList<Turma> listaBusca = getListaTurmas();
        Turma t = null;

        if (listaBusca != null) {
            for (int x = 0; x < listaBusca.size(); x++) {
                t = listaBusca.get(x);
                int id_turma = t.getId_turma();

                if (id_turma == fk_turma) {
                    return t;
                }
            }
        } else {
            System.out.println("Lista vazia");
        }
        return null;
    }

    public int buscaAluno(String matricula) {
        System.out.println("Entrou no busca aluno");
        int id = -5;
        try {
            System.out.println("Entrou no try do busca aluno");
            st.executeQuery("SELECT id_usuario from usuario where matricula = " + matricula);
            System.out.println("Fez a consulta");
            rs = st.getResultSet();
            while (rs.next()) {
                System.out.println("Achou o resultado");
                id = rs.getInt("id_usuario");
                return id;
            }
        } catch (SQLException ex) {
            System.out.println("erro sql: " + ex);
            return id;
        }
        return id;
    }

    public ArrayList<Servidor> buscaServidor(String pesquisa) {
        ArrayList<Servidor> listaServidores = new ArrayList<>();

        try {
            st.executeQuery("SELECT * FROM usuario where nome like '%" + pesquisa + "%' or email like '%" + pesquisa + "%' or matricula like '%" + pesquisa + "%' AND tipo = 1");
            rs = st.getResultSet();

            while (rs.next()) {
                int id_usuario = Integer.parseInt(rs.getString("id_usuario"));
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String funcao = rs.getString("funcao");

                Servidor s = new Servidor(nome, email, funcao);
                listaServidores.add(s);
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        return listaServidores;
    }

    private static String gerarNome() {
        String nomeNovo = "";
        long millis = System.currentTimeMillis();
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        nomeNovo = c + "_" + millis;
        return nomeNovo;
    }

    public int aceitaPublicacao(int id_publicacao) {
        int id = id_publicacao;

        try {
            st.executeUpdate("UPDATE publicacao SET status = 1 WHERE id_publicacao = " + id);
            return 1;
        } catch (SQLException ex) {
            System.out.println("Erro na alteração: " + ex);
            return 0;
        }
    }

    public int rejeitaPublicacao(int id_publicacao) {
        int id = id_publicacao;

        try {
            st.executeUpdate("UPDATE publicacao set status = 2 where id_publicacao = " + id);
            return 1;

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return 0;
        }
    }

    public int alteraSituacao(int id_publicacao, int id_usuario_final) {
        try {
            System.out.println("Entrou no altera situacao. Recebeu o id da publicacao: " + id_publicacao + ", id do usuario: " + id_usuario_final);
            int result = st.executeUpdate("UPDATE publicacao SET situacao = 1, fk_usuario_final = " + id_usuario_final + " where id_publicacao = " + id_publicacao);
            if (result == 1) {
                System.out.println("Consulta deu certo");
                notificaAlteracaoSituacao(id_publicacao);
                System.out.println("Retornou depois de chamar a notifica");
                return 1;
            } else {
                System.out.println("Consulta nao deu certo");
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return 0;
        }
    }

    public void notificaAlteracaoSituacao(int id_publicacao) {
        System.out.println("Chamou o notifica alteração");
        try {
            System.out.println("Vai fazer a consulta");
            st.executeQuery("SELECT pb.titulo, pb.fk_usuario, pb.fk_usuario_final, us.email from publicacao pb, usuario us where id_publicacao =" + id_publicacao + " and pb.fk_usuario = us.id_usuario");
            rs = st.getResultSet();
            System.out.println("Fez a consulta");
            while (rs.next()) {
                System.out.println("Encontrou resultado");
                String titulo = rs.getString("titulo");
                System.out.println("Titulo: " + titulo);
                String email = rs.getString("email");
                System.out.println("Email: " + email);

                System.out.println("Vai chamar a funcao do email");
                int result = JavaMailApp.enviaEmailNotificacao(email, titulo);
                if (result == 1) {
                    System.out.println("Enviou o email");
                } else if (result == 0) {
                    System.out.println("Nao enviou o email");

                } else {
                    System.out.println("Result: " + result);
                }
            }
        } catch (SQLException ex) {

        }

    }

    private LinkedList<Publicacao> publicacoesUsuario(int id_usuario) {
        System.out.println("Entrou na funcao de publicações do usuario");
        LinkedList<Publicacao> publicacoesUsuario = new LinkedList();
        try {
            System.out.println("Entrou no try");
            st.executeQuery("SELECT pb.id_publicacao, pb.titulo, pb.descricao, pb.local_foto, pb.data, pb.status, pb.tipo, pb.fk_usuario, pb.fk_local, pb.situacao, pb.fk_usuario_final, pb.confirmacao, l.nome, us.nome as usuario_nome from publicacao pb, locais l, usuario us where pb.fk_usuario = " + id_usuario + " and pb.fk_local = l.id_local and pb.fk_usuario = us.id_usuario order by data");
            System.out.println("Fez a consulta");
            rs = st.getResultSet();
            while (rs.next()) {
                System.out.println("Encontrou resultado");
                int id = rs.getInt("id_publicacao");
                System.out.println("ID da publicacao: " + id);
                String titulo = rs.getString(("titulo"));
                String descricao = rs.getString("descricao");
                String data = rs.getString("data");
                int status = rs.getInt("status");
                int tipo = rs.getInt("tipo");
                int situacao = rs.getInt("situacao");
                int confirmacao = rs.getInt("confirmacao");
                String local_literal = rs.getString("nome");
                String usuario_nome = rs.getString("usuario_nome");
                int id_Usuario_final = rs.getInt("fk_usuario_final");
                String local_foto = rs.getString("local_foto");

                Publicacao p = new Publicacao(titulo, descricao, data, status, tipo, situacao);
                System.out.println("Criou o objeto da publicacao");
                //File foto = new File(Main.diretorio_fotos + "\\" + local_foto + ".png");

                //p.setArquivo(foto);
                p.setSituacao(situacao);
                p.setConfirmacao(confirmacao);
                p.setLocalLiteral(local_literal);
                p.setNome_Usuario(usuario_nome);
                p.setId_publicacao(id);
                p.setUsuario_final(id_Usuario_final);

                publicacoesUsuario.add(p);

                System.out.println("Adicionou publicacao na lista");

            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        return publicacoesUsuario;
    }

    public int confirmaSituacao(int id_publicacao) {
        System.out.println("Entrou no confirma situacao");
        try {

            System.out.println("Recebeu o id: " + id_publicacao);
            System.out.println("SQL: UPDATE publicacao set confirmacao = 1 where id_publicacao = " + id_publicacao);
            int op = st.executeUpdate("UPDATE publicacao set confirmacao = 1 where id_publicacao = " + id_publicacao);
            if (op == 1) {
                System.out.println("Deu certo");
                return 1;
            } else {
                System.out.println("Nao deu certo");
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return 0;
        }
    }

    public int apagaPublicacao(int id_pb) {
        System.out.println("Entrou no apaga publicacao");
        try {
            System.out.println("Recebeu o id da publicacao para apagar: " + id_pb);
            int op = st.executeUpdate("delete from publicacao where id_publicacao = " + id_pb);
            if (op == 1) {
                System.out.println("Deu certo");
                return 1;
            } else {
                System.out.println("Nao deu certo");
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        }
        return 0;

    }

    public int excluirUsuario(String mensagemRecebida) {
        String pesquisa = mensagemRecebida;
        System.out.println("Recebeu para excluir: " + mensagemRecebida);
        try {
            int result = st.executeUpdate("DELETE FROM usuario where matricula = '" + mensagemRecebida + "' or email = '" + mensagemRecebida + "'");
            if (result == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {

        }
        return 0;
    }

    public int cadastraMensagem(String destinatario, String mensagem, int i) {
        if (i == 1) { //mensagem para aluno
            int id = buscaAluno(destinatario);
            try {
                Date dataAtual = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String data = sdf.format(dataAtual);
                System.out.println("SQL: INSERT INTO recados (data, conteudo, fk_turma) values ( '" + data +"','" + mensagem + "'," + id + ")");
                st.executeUpdate("INSERT INTO recados (data, conteudo, fk_aluno) values ('" + data + "','" + mensagem + "'," + id + ")");
                return 1;
            } catch (SQLException ex) {
                System.out.println("Erro sql: " + ex);
                return 0;
            }
        } else if (i == 2) { //mensagem para turma
            try {
                Date dataAtual = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String data = sdf.format(dataAtual);
                System.out.println("SQL: INSERT INTO recados (data, conteudo, fk_turma) values ( '" + data+ "','" + mensagem + "'," + destinatario + ")");
                st.executeUpdate("INSERT INTO recados (data, conteudo, fk_turma) values ('" + data + "','" + mensagem + "'," + destinatario + ")");
                return 1;
            } catch (SQLException ex) {
                System.out.println("Erro sql: " + ex);
                return 0;
            }
        } else {
            return 0;
        }
    }

}
