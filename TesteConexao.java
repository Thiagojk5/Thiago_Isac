/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lista;

import java.util.ArrayList;

/**
 * Teste rapido: conecta no MySQL, grava um arquivo de teste, le a tabela
 * e apaga o arquivo de teste. Rode este arquivo sozinho (Run File).
 */
/*
public class TesteConexao {

    public static void main(String[] args) {
        try {
            RepositorioMySQL repo = new RepositorioMySQL();
            System.out.println("Conectou e a tabela esta pronta.");

            repo.remover("teste", "txt");   // limpa sobra de um teste anterior

            Arquivo a = new Arquivo();
            a.setNome("teste");
            a.setFormato("txt");
            a.setTamanho(2048);
            repo.inserir(a);
            System.out.println("Inseriu o arquivo de teste.");

            ArrayList<Arquivo> lista = repo.carregar();
            System.out.println("Arquivos na tabela: " + lista.size());
            for (Arquivo x : lista) {
                System.out.println(x.getNome() + "." + x.getFormato() + " - " + x.getTamanho() + " bytes");
            }

            boolean removeu = repo.remover("teste", "txt");
            System.out.println("Removeu o arquivo de teste: " + removeu);
        } catch (RuntimeException e) {
            System.out.println("FALHOU: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Causa: " + e.getCause().getMessage());
            }
        }
    }
}*/