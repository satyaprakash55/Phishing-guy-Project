import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import com.formdev.flatlaf.FlatLightLaf;

public class PhishingURLDetectorGUI extends JFrame {
    private JTextField urlField;
    private JTextArea outputArea;
    private Map<String, Integer> summaryStats = new HashMap<>();

    private static final List<String> suspiciousTLDs = Arrays.asList(".tk", ".ml", ".ga", ".cf", ".gq");
    private static final List<String> sensitiveKeywords = Arrays.asList(
        "facebook", "gmail", "instagram", "apple", "amazon",
        "login", "verify", "secure", "update", "account", "password"
    );

    public PhishingURLDetectorGUI() {
        setTitle("Phishing URL Detector 🔍");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        urlField = new JTextField();
        JButton scanBtn = new JButton("Scan URL");
        JButton fileBtn = new JButton("Scan from File");
        JButton chartBtn = new JButton("Show Summary Chart");

        topPanel.add(urlField, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.add(scanBtn);
        btnPanel.add(fileBtn);
        btnPanel.add(chartBtn);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        scanBtn.addActionListener(e -> scanURL(urlField.getText().trim()));
        fileBtn.addActionListener(e -> scanFromFile());
        chartBtn.addActionListener(e -> showChart());
    }

    private void scanURL(String url) {
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a URL", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        outputArea.append("[SCAN] " + url + "\n");
        List<String> result = processURL(url);
        for (String line : result) outputArea.append(line + "\n");
        outputArea.append("\n");
    }

    private void scanFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String url;
                while ((url = br.readLine()) != null) {
                    url = url.trim();
                    if (!url.isEmpty()) scanURL(url);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private List<String> processURL(String url) {
        List<String> issues = new ArrayList<>();
        try {
            String scheme = url.split("://")[0];
            String withoutScheme = url.split("://", 2)[1];
            String domain = withoutScheme.split("/|")[0].toLowerCase();
            String path = withoutScheme.contains("/") ? withoutScheme.substring(withoutScheme.indexOf("/")).toLowerCase() : "";

            if (!scheme.equals("https")) {
                issues.add("[WARNING] Missing HTTPS (Not secure)");
                incrementStat("Missing HTTPS");
            }

            for (String tld : suspiciousTLDs) {
                if (domain.endsWith(tld)) {
                    issues.add("[WARNING] Suspicious TLD used");
                    incrementStat("Suspicious TLD");
                    break;
                }
            }

            for (String keyword : sensitiveKeywords) {
                if (domain.contains(keyword) || path.contains(keyword)) {
                    issues.add("[WARNING] Keyword found: " + keyword);
                    incrementStat("Sensitive Keyword");
                }
            }

            if (domain.length() > 40 || domain.matches(".*[-_]{2,}.*")) {
                issues.add("[WARNING] Domain looks suspicious or too long");
                incrementStat("Suspicious Domain");
            }

        } catch (Exception e) {
            issues.add("[ERROR] Could not parse URL");
        }
        if (issues.isEmpty()) {
            issues.add("[OK] Safe");
            incrementStat("Safe");
        }
        return issues;
    }

    private void incrementStat(String key) {
        summaryStats.put(key, summaryStats.getOrDefault(key, 0) + 1);
    }

    private void showChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : summaryStats.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Scan Summary", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSimpleLabels(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame chartFrame = new JFrame("Report");
        chartFrame.setSize(500, 400);
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.add(chartPanel);
        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }
        SwingUtilities.invokeLater(() -> new PhishingURLDetectorGUI().setVisible(true));
    }
}
