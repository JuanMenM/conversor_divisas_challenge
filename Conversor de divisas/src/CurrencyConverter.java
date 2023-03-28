import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class CurrencyConverter extends JFrame {

    private JLabel inputLabel;
    private JTextField inputAmount;
    private JComboBox<String> inputCurrency;
    private JLabel outputLabel;
    private JLabel outputResult;
    private JComboBox<String> outputCurrency;
    private JButton convertButton;

    public CurrencyConverter() {
        super("Currency Converter");

        // Campos input
        inputLabel = new JLabel("Amount:");
        inputAmount = new JTextField(10);
        inputCurrency = new JComboBox<>(new String[] {"USD", "EUR", "GBP", "JPY", "MXN"});
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(inputLabel);
        inputPanel.add(inputAmount);
        inputPanel.add(inputCurrency);

        //Campos output
        outputLabel = new JLabel("Converted Amount:");
        outputResult = new JLabel("");
        outputCurrency = new JComboBox<>(new String[] {"USD", "EUR", "GBP", "JPY", "MXN"});
        JPanel outputPanel = new JPanel(new FlowLayout());
        outputPanel.add(outputLabel);
        outputPanel.add(outputResult);
        outputPanel.add(outputCurrency);

        // Boton para convertir
        convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convert();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(convertButton);

        // Componentes
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Propiedades
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private double getExchangeRate(String fromCurrency, String toCurrency) throws IOException {
        String urlString = "https://api.exchangeratesapi.io/latest?base=" + fromCurrency + "&symbols=" + toCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();
        JSONObject json = new JSONObject(response);
        return json.getJSONObject("rates").getDouble(toCurrency);
    }

    private void convert() {
        try {
            double amount = Double.parseDouble(inputAmount.getText());
            double exchangeRate = getExchangeRate(inputCurrency.getSelectedItem().toString(), outputCurrency.getSelectedItem().toString());
            double convertedAmount = amount * exchangeRate;
            outputResult.setText(String.format("%.2f %s", convertedAmount, outputCurrency.getSelectedItem().toString()));
        } catch (IOException | JSONException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while converting currencies: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CurrencyConverter();
    }

}
