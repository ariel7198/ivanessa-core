/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author ariel
 */
public abstract class Criptografia {
    private static final long serialVersionUID = 123L;
    
    public static String Criptografa(String palavra_origem) { //método que criptografa

        char vetor[] = new char[70];
        char vetor1[] = new char[70];
        char vetor2[] = new char[70];

        String alfabeto = "abcdefghijklmnopqrstuvwyzABCDEFGHIJKLMNOPQRSTUVWYXZ123456789@#$%&*-+";
        vetor = alfabeto.toCharArray();

        String alfabeto1 = "ip@#u$%&*hmt0qkn14br23oxIPUH5MTQKNB6ROXFCfc7azlv8gwyed9jsAZLV-GWYED+JS";
        vetor1 = alfabeto1.toCharArray();

        String alfabeto2 = "wztre9yisb8kphufd7lgWZTRE65YI@#-+SBK1PHUa3qv$%&*2jxncm04oFDLGAQVJXNCMO";
        vetor2 = alfabeto2.toCharArray();

        String palavra = new String(); // a palavra original

        palavra = palavra_origem;

        String srt = "";
        String resultado = "";
        int qtd = palavra.length();

        char pronto[] = new char[qtd];
        char paraCrip[] = new char[qtd];

        for (int i = 0; i < qtd; i++) { //for que percorre a palavra
            System.out.println("Entrou no for da palavra ");

            char letra = palavra.charAt(i);
            System.out.println("Letra da palavra: " + letra);
            int t = -1;
            int contr = 0;
            while (contr == 0) {
                t++;
                System.out.println("\n Entrou na busca nos alfabetos ");
                if (letra == alfabeto.charAt(t)) {
                    System.out.println("Encontrou a letra. A letra é: " + alfabeto.charAt(t));
                    System.out.println("I vale: " + i);
                    System.out.println("T vale: " + t);
                    if (t == 0) {
                        System.out.println("T vale 0, faz a substituição para iniciar no fim do alfabeto");
                        t = 69; //ultima posicao + 1;
                        if (i % 2 == 0) {
                            pronto[i] = alfabeto1.charAt(t);
                            System.out.println("Posição divisivel por 2. Substituição usando o Alfabeto 1");
                        } else if (i % 2 != 0) {
                            System.out.println("Posicao nao divisivel por 2. Substituição usando o Alfabeto 2 voltando 1 casa");
                            pronto[i] = alfabeto2.charAt(t);
                        }

                    } else if (i % 2 == 0) {
                        System.out.println("Posicao divisivel por 2. Substituição usando o Alfabeto 1 voltando 1 casa");
                        pronto[i] = alfabeto1.charAt(t - 1);
                    } else if (i % 2 != 0) {
                        System.out.println("Posicao nao divisivel por 2. Substituição usando o Alfabeto 2 voltando 1 casa");
                        pronto[i] = alfabeto2.charAt(t - 1);
                    }

                    System.out.println("A letra adicionada foi: " + pronto[i]);
                    /*resultado = resultado + pronto[i];*/
                    contr = 1;
                    /*if (pronto[i] == 'a') {
                        pronto[i] = '@';
                    } else if (pronto[i] == '@') {
                        pronto[i] = 'c';
                    } else if (pronto[i] == 'c') {
                        pronto[i] = 'a';
                    } */
                    resultado = resultado + pronto[i];
                    System.out.println("Palavra com as letras trocadas:" + resultado);

                } else {
                    System.out.println("Não encontrou a letra. Comparou a letra " + palavra.charAt(i) + " com a letra " + alfabeto.charAt(t));
                }
            }

        }
        return resultado;
    }

    public static String Descriptografa (String palavra_origem){ //método que descriptografa
        char vetor[] = new char[70];
        char vetor1[] = new char[70];
        char vetor2[] = new char[70];

        String alfabeto = "abcdefghijklmnopqrstuvwyzABCDEFGHIJKLMNOPQRSTUVWYXZ123456789@#$%&*-+";
        vetor = alfabeto.toCharArray();

        String alfabeto1 = "ip@#u$%&*hmt0qkn14br23oxIPUH5MTQKNB6ROXFCfc7azlv8gwyed9jsAZLV-GWYED+JS";
        vetor1 = alfabeto1.toCharArray();

        String alfabeto2 = "wztre9yisb8kphufd7lgWZTRE65YI@#-+SBK1PHUa3qv$%&*2jxncm04oFDLGAQVJXNCMO";
        vetor2 = alfabeto2.toCharArray();

        //DESCRIPTOGRAFIA AQUI | DESCRIPTOGRAFIA AQUI | DESCRIPTOGRAFIA AQUI | DESCRIPTOGRAFIA AQUI | DESCRIPTOGRAFIA AQUI
        String palavra = new String(); // a palavra original

        palavra = palavra_origem;

        int qtd = palavra.length();
        String resultado = " ";

        char pronto[] = new char[qtd];
        char paraCrip[] = new char[qtd];

        for (int i = 0; i < qtd; i++) { //for que percorre a palavra

            System.out.println("Entrou no for da palavra ");

            char letra = palavra.charAt(i);
            System.out.println("Letra da palavra: " + letra);
            int t = -1;
            int contr = 0;
            while (contr == 0) {
                /* if(pronto[i]=='a'){
                                pronto[i]='c';
                            }else{
                                if(pronto[i]=='c'){
                                pronto[i]='@';
                            }
                                else{
                                    if(pronto[i]=='@'){
                                pronto[i]='a';
                            }
                                }
                            } */
                t++;
                System.out.println("\n Entrou na busca nos alfabetos ");
                if (i % 2 == 0) {

                    System.out.println("Valor par. Vai procurar no Alfabeto 1");
                    if (letra == alfabeto1.charAt(t)) {
                        System.out.println("Encontrou a letra. A letra é: " + alfabeto1.charAt(t));
                        System.out.println("I vale: " + i);
                        System.out.println("T vale: " + t);
                        if (t == 69) {
                            System.out.println("T vale 69, faz a substituição para iniciar no fim do alfabeto");
                            t = 0; //primeira posição;
                            pronto[i] = alfabeto.charAt(t);
                            System.out.println("Posição divisivel por 2. Substituição usando o Alfabeto 1");
                            System.out.println("A letra adicionada foi: " + pronto[i]);
                            resultado = resultado + pronto[i];
                            contr = 1;
                        } else {
                            System.out.println("T não vale 69, Não faz substituição");
                            pronto[i] = alfabeto.charAt(t + 1);
                            System.out.println("Posição divisivel por 2. Substituição usando o Alfabeto 1");
                            System.out.println("A letra adicionada foi: " + pronto[i]);
                            resultado = resultado + pronto[i];
                            contr = 1;
                        }
                    } else {
                        System.out.println("Não encontrou a letra. Comparou a letra " + palavra.charAt(i) + " com a letra " + alfabeto1.charAt(t));
                    }
                } else { //caso nao seja par
                    System.out.println("Valor impar. Vai procurar no Alfabeto 2");
                    if (letra == alfabeto2.charAt(t)) {
                        System.out.println("Encontrou a letra. A letra é: " + alfabeto2.charAt(t));
                        System.out.println("I vale: " + i);
                        System.out.println("T vale: " + t);
                        if (t == 69) {
                            System.out.println("T vale 69, faz a substituição para iniciar no fim do alfabeto");
                            t = 0; //primeira posição;
                            pronto[i] = alfabeto.charAt(t);
                            System.out.println("Posição nao divisivel por 2. Substituição usando o Alfabeto 2");
                            System.out.println("A letra adicionada foi: " + pronto[i]);
                            resultado = resultado + pronto[i];
                            contr = 1;
                        } else {
                            System.out.println("T diferente de 69, Nao substitui");
                            pronto[i] = alfabeto.charAt(t + 1);
                            //System.out.println("Posição divisivel por 2. Substituição usando o Alfabeto 1");
                            System.out.println("A letra adicionada foi: " + pronto[i]);
                            resultado = resultado + pronto[i];
                            contr = 1;

                        }

                    } else {
                        System.out.println("Não encontrou a letra. Comparou a letra " + palavra.charAt(i) + " com a letra " + alfabeto2.charAt(t));
                    }
                }
                
                System.out.println("Palavra com as letras trocadas:" + resultado);
            }
        }
        return resultado;
    }
}
