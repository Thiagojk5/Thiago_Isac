/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lista;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author admin
 */
public class Principal {

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceArquivos().setVisible(true));
        SistemaArquivo sistema;
        try{
            sistema = new SistemaArquivo();
        }catch(RuntimeException e){
            System.out.println("Erro ao criar a conexao: " + e.getMessage());
            return;
        }
        Scanner leitor = new Scanner(System.in);
        leitor.useLocale(Locale.US); // Agora aceita ponto: 1.5
        int opcao;
        do{
            System.out.println("Escolha uma opcao:\n");
            System.out.println("1 - Inserir Ordenado\n"+
                               "2 - Remover\n"+
                               "3 - Buscar\n"+
                               "4 - Exibir Todos\n"+
                               "5 - Exibir um Especifico\n"+
                               "6 - Exibir por Formato\n"+
                               "0 - Sair");
            opcao = leitor.nextInt();
       
            switch(opcao){
                case 1->{
                  Arquivo arq = new Arquivo();
                  long tamanho = -1;
                    System.out.println("Nome:");
                    String nome = leitor.next().trim();
                    System.out.println("Formato");
                    String formato = leitor.next().trim();
                    System.out.println("Tamanho em bytes:");
                    try{
                        tamanho = leitor.nextLong();
                    }catch(InputMismatchException e){
                        leitor.next();
                    }
                    
                    if(!nome.isBlank() && !formato.isBlank() && tamanho >= 0){
                        arq.setFormato(formato);
                        arq.setNome(nome);
                        arq.setTamanho(tamanho);
                        if(sistema.inserirOrdenado(arq)){
                            System.out.println("Arquivo adicionado!");
                        }else{
                            System.out.println("Esse Arquivo ja existe!");
                        }
                    } else{
                        System.out.println("Dados invalidos!");
                    }
                }
                case 2->{
                    System.out.println("Nome:");
                    String nome = leitor.next();
                    System.out.println("Formato:");
                    String formato = leitor.next();
                    sistema.remover(nome,formato);
                    if(sistema.remover(nome, formato)){
                        System.out.println("Arquivo Removido!");
                    } else{
                        System.out.println("Arquivo Nao Encontrado!");
                    }
                }
                case 3->{
                    System.out.println("Verifica se o Arquivo existe!");
                    System.out.println("Nome:");
                    String nome = leitor.next();
                    System.out.println("Formato:");
                    String formato = leitor.next();
                    sistema.buscar(nome,formato);
                    if(sistema.buscar(nome, formato) != null){
                        System.out.println("O arquivo existe.");
                    } else{
                        System.out.println("O arquivo nao existe.");
                    }
                }
                case 4->{
                    sistema.exibir();
                }
                case 5->{
                    System.out.println("Nome:");
                    String nome = leitor.next();
                    System.out.println("Formato:");
                    String formato = leitor.next();
                    sistema.exibirArquivo(nome,formato);
                }
                case 6->{
                    System.out.println("Digite o Formato do Arquivo:");
                    String formato = leitor.next();
                    sistema.exibirPorFormato(formato);
                }
            }    
        }while(opcao != 0);    
    }
}