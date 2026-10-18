package lista;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Interface grafica (Swing) para o sistema de arquivos.
 * A tela so le os campos, chama os metodos do SistemaArquivo e mostra o resultado.
 * Arquivos sao identificados por nome + formato. O tamanho e digitado em bytes
 * e mostrado em B/KB/MB/GB. O botao "Importar..." cadastra arquivos reais do
 * computador (so os dados: nome, formato e tamanho; o arquivo em si nao e mexido).
 */
public class InterfaceArquivos extends JFrame {

    // Limites das colunas da tabela do MySQL
    private static final int TAM_MAX_NOME = 255;
    private static final int TAM_MAX_FORMATO = 50;

    // Resultado de importar um arquivo
    private static final int INSERIDO = 0;
    private static final int REPETIDO = 1;
    private static final int IGNORADO = 2;

    private final SistemaArquivo sistema;

    private final JTextField campoNome = new JTextField(15);
    private final JTextField campoFormato = new JTextField(8);
    private final JTextField campoTamanho = new JTextField(8);
    private final JTextArea areaSaida = new JTextArea(12, 40);
    private final JLabel status = new JLabel(" ");

    // Cria o proprio SistemaArquivo (que carrega os dados do banco)
    public InterfaceArquivos() {
        this(new SistemaArquivo());
    }

    // Permite compartilhar o mesmo SistemaArquivo com outra classe (ex.: Principal)
    public InterfaceArquivos(SistemaArquivo sistema) {
        super("Sistema de Arquivos");
        this.sistema = sistema;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Campos de entrada
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Nome:"));
        form.add(campoNome);
        form.add(new JLabel("Formato:"));
        form.add(campoFormato);
        form.add(new JLabel("Tamanho (bytes):"));
        form.add(campoTamanho);

        // Botoes
        JButton bInserir = new JButton("Inserir");
        JButton bRemover = new JButton("Remover");
        JButton bBuscar = new JButton("Buscar");
        JButton bListar = new JButton("Exibir todos");
        JButton bImportar = new JButton("Importar...");
        bInserir.addActionListener(e -> inserir());
        bRemover.addActionListener(e -> remover());
        bBuscar.addActionListener(e -> buscar());
        bListar.addActionListener(e -> listar());
        bImportar.addActionListener(e -> importar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(bInserir);
        botoes.add(bRemover);
        botoes.add(bBuscar);
        botoes.add(bListar);
        botoes.add(bImportar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.NORTH);
        norte.add(botoes, BorderLayout.SOUTH);

        // Area de resultado (somente leitura)
        areaSaida.setEditable(false);
        areaSaida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(areaSaida), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        listar();   // ao abrir, mostra o que ja esta salvo no banco
    }

    private void inserir() {
        String nome = campoNome.getText().trim();
        String formato = campoFormato.getText().trim();

        if (nome.isBlank() || formato.isBlank()) {
            erro("Preencha o nome e o formato.");
            return;
        }

        long tamanho;
        try {
            tamanho = Long.parseLong(campoTamanho.getText().trim());
        } catch (NumberFormatException e) {
            erro("Tamanho invalido. Digite um numero inteiro de bytes, por exemplo 2048.");
            return;
        }
        if (tamanho < 0) {
            erro("O tamanho deve ser maior ou igual a zero.");
            return;
        }

        Arquivo arq = new Arquivo();
        arq.setNome(nome);
        arq.setFormato(formato);
        arq.setTamanho(tamanho);

        try {
            if (sistema.inserirOrdenado(arq)) {
                sucesso("Arquivo inserido: " + nome + "." + formato);
                limparCampos();
                listar();
            } else {
                erro("Esse arquivo ja existe: " + nome + "." + formato);
            }
        } catch (RuntimeException e) {
            erro("Erro no banco de dados: " + e.getMessage());
        }
    }

    private void remover() {
        String nome = campoNome.getText().trim();
        String formato = campoFormato.getText().trim();
        if (nome.isBlank() || formato.isBlank()) {
            erro("Digite o nome e o formato do arquivo a remover.");
            return;
        }

        try {
            if (sistema.remover(nome, formato)) {
                sucesso("Arquivo removido: " + nome + "." + formato);
                listar();
            } else {
                erro("Arquivo nao encontrado: " + nome + "." + formato);
            }
        } catch (RuntimeException e) {
            erro("Erro no banco de dados: " + e.getMessage());
        }
    }

    private void buscar() {
        String nome = campoNome.getText().trim();
        String formato = campoFormato.getText().trim();
        if (nome.isBlank() || formato.isBlank()) {
            erro("Digite o nome e o formato do arquivo a buscar.");
            return;
        }

        Arquivo a = sistema.buscar(nome, formato);
        if (a == null) {
            erro("Arquivo nao encontrado: " + nome + "." + formato);
        } else {
            areaSaida.setText(formatar(a));
            sucesso("Arquivo encontrado.");
        }
    }

    private void listar() {
        ArrayList<Arquivo> lista = sistema.getArquivos();
        if (lista.isEmpty()) {
            areaSaida.setText("Lista vazia.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Arquivo a : lista) {
            sb.append(formatar(a)).append("\n");
        }
        areaSaida.setText(sb.toString());
    }

    // ---------- Importar arquivos reais do computador ----------

    private void importar() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Escolha os arquivos para importar");
        seletor.setMultiSelectionEnabled(true);
        seletor.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;   // o usuario cancelou
        }

        int importados = 0;
        int repetidos = 0;
        int ignorados = 0;

        try {
            for (File arquivo : seletor.getSelectedFiles()) {
                int resultado = importarUm(arquivo);
                if (resultado == INSERIDO) {
                    importados++;
                } else if (resultado == REPETIDO) {
                    repetidos++;
                } else {
                    ignorados++;
                }
            }
        } catch (RuntimeException e) {
            erro("Erro no banco de dados: " + e.getMessage());
            listar();
            return;
        }

        String resumo = importados + " importado(s), "
                + repetidos + " ja existia(m), "
                + ignorados + " ignorado(s) (sem formato ou nome grande demais).";
        if (importados > 0) {
            sucesso(resumo);
        } else {
            erro(resumo);
        }
        listar();
    }

    // Cadastra UM arquivo real. Devolve INSERIDO, REPETIDO ou IGNORADO.
    private int importarUm(File arquivo) {
        if (!arquivo.isFile()) {
            return IGNORADO;
        }

        // "relatorio.final.pdf": o formato e o que vem depois do ULTIMO ponto
        String nomeCompleto = arquivo.getName();
        int ponto = nomeCompleto.lastIndexOf('.');

        // ponto <= 0: sem ponto ou arquivo oculto (".gitignore"); ultimo caractere: "arquivo."
        if (ponto <= 0 || ponto == nomeCompleto.length() - 1) {
            return IGNORADO;
        }

        String nome = nomeCompleto.substring(0, ponto);
        String formato = nomeCompleto.substring(ponto + 1);

        if (nome.length() > TAM_MAX_NOME || formato.length() > TAM_MAX_FORMATO) {
            return IGNORADO;
        }

        Arquivo arq = new Arquivo();
        arq.setNome(nome);
        arq.setFormato(formato);
        arq.setTamanho(arquivo.length());   // tamanho real, em bytes

        return sistema.inserirOrdenado(arq) ? INSERIDO : REPETIDO;
    }

    // ---------- Apoio ----------

    private String formatar(Arquivo a) {
        return String.format("%-20s %-8s %s",
                a.getNome(), a.getFormato(), Arquivo.formatarTamanho(a.getTamanho()));
    }

    private void limparCampos() {
        campoNome.setText("");
        campoFormato.setText("");
        campoTamanho.setText("");
        campoNome.requestFocus();
    }

    private void erro(String mensagem) {
        status.setForeground(Color.RED);
        status.setText(mensagem);
    }

    private void sucesso(String mensagem) {
        status.setForeground(new Color(0, 128, 0));
        status.setText(mensagem);
    }

    // So para abrir a tela sozinha; se o banco estiver fora do ar, mostra o erro.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new InterfaceArquivos().setVisible(true);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null,
                        "Nao foi possivel conectar ao banco:\n" + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}