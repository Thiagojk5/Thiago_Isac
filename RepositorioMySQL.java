/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lista;
import java.sql.Connection;
import java.sql.DriverManager; //Responsavel por criar a Conexão entre o BD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Responsavel para tratar erros
import java.sql.Statement;
import java.util.ArrayList;
/**
 * Guarda e le os arquivos em uma tabela do MySQL.
 * Antes de usar: crie o banco no Workbench com
 *   CREATE DATABASE IF NOT EXISTS sistema_arquivos;
 */
public class RepositorioMySQL {
   private static final String URL = "jdbc:mysql://localhost:3306/sistema_arquivos";
   private static final String USUARIO = "root";
   private static final String SENHA = "sua_senha"; // coloque sua senha
   
   // throws SQLException: vai dar erro se os dados forem invalidos
   // Criar conexão com o BD
   public Connection conectar() throws SQLException{
       return DriverManager.getConnection(URL,USUARIO,SENHA);
   }
   
   // Cria a tabela se ela ainda nao existir
   public RepositorioMySQL(){
       String sql = "CREATE TABLE IF NOT EXISTS arquivo ("
               +    "nome varchar(255) not null, "
               +    "formato varchar(50) not null, "
               +    "tamanho bigint not null, "
               +    "primary key(nome,formato))";
       try(Connection c = conectar(); Statement st = c.createStatement()){
           st.execute(sql);
       }catch(SQLException e){
           throw new RuntimeException("Erro ao criar a tabela: " + e.getMessage(), e);
       }
   }
   
   // Le todos os arquivos, ja ordenados por nome e formato
   public ArrayList<Arquivo> carregar(){
       ArrayList<Arquivo> lista = new ArrayList<>();
       String sql = "Select nome, formato, tamanho From arquivo ORDER BY nome, formato";
       
       try(Connection c = conectar();
           Statement st = c.createStatement();
           ResultSet rs = st.executeQuery(sql)){
          
           while(rs.next()){
               Arquivo a  = new Arquivo();
               a.setNome(rs.getString("nome"));
               a.setFormato(rs.getString("formato"));
               a.setTamanho(rs.getLong("tamanho"));
               lista.add(a);
           }
       }catch(SQLException e){
           throw new RuntimeException("Erro ao carregar os arquivos: " + e.getMessage(), e);
       }
       return lista;
   }
   
   public void inserir(Arquivo a){
       String sql = "INSERT INTO arquivo (nome,formato,tamanho) VALUES (?, ?, ?)";
       try(Connection c = conectar();
           PreparedStatement ps = c.prepareStatement(sql)){
           ps.setString(1, a.getNome());
           ps.setString(2, a.getFormato());
           ps.setLong(3, a.getTamanho());
           ps.executeUpdate();
       }catch(SQLException e){
        throw new RuntimeException("Erro a inserir o arquivo: " + e.getMessage(), e);
       }
   }
   
   public boolean remover(String nome,String formato){
       String sql = "DELETE FROM arquivo WHERE nome = ? AND formato = ?";
       try(Connection c = conectar();
           PreparedStatement ps = c.prepareStatement(sql)){
           ps.setString(1, nome);
           ps.setString(2, formato);
           return ps.executeUpdate() > 0;
       }catch(SQLException e){
           throw new RuntimeException("Erro ao remover arquivo: " + e.getMessage(), e);
       }
   }
}