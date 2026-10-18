/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lista;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class SistemaArquivo {
   // private final ArrayList<Arquivo> arquivos =  new ArrayList<>();
    private ArrayList<Arquivo> arquivos;
    private RepositorioMySQL repo;
    // Construtor
    public SistemaArquivo(){
        //arquivos = new ArrayList<>();
        repo = new RepositorioMySQL();
        arquivos = repo.carregar();
    }
    
    private int comparar(Arquivo a, String nome, String formato){
        int cmp =  a.getNome().compareToIgnoreCase(nome);
        return (cmp != 0)? cmp : a.getFormato().compareToIgnoreCase(formato);
    }
    
    public boolean inserirOrdenado(Arquivo arq){
        int i = 0;
         // Percorre a lista até achar a posição correta para manter a ordem
        // alfabética (sem diferenciar maiúsculas de minúsculas): avança enquanto
        // o nome do arquivo atual vem antes do nome do novo arquivo.
        while(i < arquivos.size() && comparar(arquivos.get(i), arq.getNome(), arq.getFormato())<0){
            i++;
        }
        
        if(i < arquivos.size() && comparar(arquivos.get(i), arq.getNome(), arq.getFormato())==0){
            return false;
        }
        
        arquivos.add(i, arq);
        repo.inserir(arq);
        return true;
    }
    
    public boolean remover(String nome,String formato){
        for(int i = 0;i < arquivos.size();i++){
           int cmp = comparar(arquivos.get(i), nome, formato);
           
           if(cmp == 0){
               arquivos.remove(i); // percorreu e achou o arquivo
               repo.remover(nome, formato);
               return true;
           }
           if(cmp > 0){
               return false; // percorreu e não achou o arquivo
           }
        }
        return false;
    }
    
    // Retorna Arquivo em vez de No: como a lista é um ArrayList<Arquivo>, não existe
    // nó, então o método devolve o próprio arquivo (ou null se não for encontrado).
    public Arquivo buscar(String nome,String formato){
        for(int i = 0; i < arquivos.size();i++){
            int cmp = comparar(arquivos.get(i), nome, formato);
            
            if(cmp == 0){
                return arquivos.get(i);
            }
            
            if(cmp > 0){
                return null;
            }
        }
        return null;
    }
    
  
    // Exibe todos os Arquivos presente na lista
    public void exibir(){
        if(arquivos.isEmpty()){
            System.out.println("Lista Vazia!");
            return;
        }
        
        for(Arquivo a : arquivos){
            System.out.println("Nome: " + a.getNome() + "\nFormato: " + a.getFormato() + 
                    "\nTamanho: " + Arquivo.formatarTamanho(a.getTamanho()));
        }
    }
    
    // Exibe somente o Arquivo pedido
    public void exibirArquivo(String nome,String formato){
        Arquivo a = buscar(nome,formato);
        
        if(a == null){
            System.out.println("Arquivo nao encontrado");
            return;
        }
        System.out.println("Nome: " + a.getNome() + "\nFormato: " + a.getFormato() + 
                "\nTamanho: " + Arquivo.formatarTamanho(a.getTamanho()));
    }
    
    public void exibirPorFormato(String formato){
        boolean achou = false;
        for(int i = 0;i < arquivos.size();i++){
            int cmp = arquivos.get(i).getFormato().compareToIgnoreCase(formato);
            if(cmp == 0){
                Arquivo a = arquivos.get(i);
                
                System.out.println("Nome: " + a.getNome() + "\nFormato: " + a.getFormato() + 
                         "\nTamanho: " + Arquivo.formatarTamanho(a.getTamanho()));
                achou = true;
            } 
        }
        
        if(achou == false){
            System.out.println("Arquivo nao encontrado");
        }
    }
    
    public ArrayList<Arquivo> getArquivos() {
        return new ArrayList<>(arquivos);
    }
    
}
