import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class App extends JFrame {
    private final JTextField txtTipoDoacao;
    private final JTextField txtquantidade;
    private final JTextField txtDate;
    private final JTextArea txtTotaldoacoes;
    private final List<Doacao> doacoes;

    public App() {
        doacoes = new ArrayList<>();
        setTitle("Sistema de Doações - Rio Grande do Sul");
        setSize(550, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Tipo de doação (Roupas, Alimentos, Dinheiro):"));
        txtTipoDoacao = new JTextField();
        add(txtTipoDoacao);

        add(new JLabel("Quantidade:"));
        txtquantidade = new JTextField();
        add(txtquantidade);

        add(new JLabel("Data (dd/MM/yyyy):"));
        txtDate = new JTextField();
        add(txtDate);

        JButton btnAddDoacao = new JButton("Adicionar Doação");
        add(btnAddDoacao);

        txtTotaldoacoes = new JTextArea();
        txtTotaldoacoes.setEditable(false);
        add(new JScrollPane(txtTotaldoacoes));

        JButton btncalcularTotal = new JButton("Cálculo Total");
        add(btncalcularTotal);

        btnAddDoacao.addActionListener((ActionEvent e) -> {
            addDoacao();
        });

        btncalcularTotal.addActionListener((ActionEvent e) -> {
            calcularTotal();
        });

        carregaDoArquivo();
    }

    private void addDoacao() {
        String type = txtTipoDoacao.getText();
        String quantidade = txtquantidade.getText();
        String date = txtDate.getText();

        try {
            Doacao Doacao = new Doacao(type, Double.parseDouble(quantidade), date);
            doacoes.add(Doacao);
            salvarNoArquivo();
            LimparArqv();
            JOptionPane.showMessageDialog(this, "Doação adicionada!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro, Insira um valor válido.");
        }
    }

    private void LimparArqv() {
        txtTipoDoacao.setText("");
        txtquantidade.setText("");
        txtDate.setText("");
    }

    private void calcularTotal() {
        double total = doacoes.stream().mapToDouble(Doacao::getquantidade).sum();
        txtTotaldoacoes.setText("Total doacoes: " + total);
    }

    private void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("doacoes.txt"))) {
            for (Doacao Doacao : doacoes) {
                writer.write(Doacao.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar doação, tente novamente.");
        }
    }

    private void carregaDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("doacoes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Doacao Doacao = new Doacao(parts[0], Double.parseDouble(parts[1]), parts[2]);
                    doacoes.add(Doacao);
                }
            }
        } catch (IOException e) {
            
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App frame = new App();
            frame.setVisible(true);
        });
    }
}

class Doacao {
    private final String type;
    private final double quantidade;
    private final String date;

    public Doacao(String type, double quantidade, String date) {
        this.type = type;
        this.quantidade = quantidade;
        this.date = date;
    }

    public double getquantidade() {
        return quantidade;
    }

    @Override
    public String toString() {
        return type + "," + quantidade + "," + date;
    }
}
