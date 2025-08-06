🕵️‍♂️ Phishing Guy – URL Phishing Detector with GUI


Phishing Guy is a Java-based desktop application that detects potentially malicious (phishing) URLs using rule-based heuristics. It features a sleek UI powered by FlatLaf and visual reporting with JFreeChart, allowing users to scan individual or bulk URLs and identify threats in real-time.


🚀 Features
✅ Real-time URL analysis for phishing indicators

🔒 Detects:

Non-HTTPS URLs

Suspicious TLDs like .tk, .ml, etc.

Sensitive keywords (facebook, login, verify, etc.)

Overly long or obfuscated domains

📂 Batch scanning from .txt files

🧾 Auto-logging of scan results in output.txt

🧠 Clean classification: [OK] Safe or [WARNING] based

🖥️ GUI enhanced with FlatLaf Look & Feel

📊 Pie chart reporting using JFreeChart (Safe vs Suspicious)


📂 How to Run

💻 Prerequisites
Java 17 or above (with --enable-preview if using latest Java)

JFreeChart (add to classpath)

FlatLaf (add to classpath)

🧪 Compile & Run

javac -cp ".;jfreechart-x.x.x.jar;flatlaf-x.x.x.jar" PhishingGuy.java
java -cp ".;jfreechart-x.x.x.jar;flatlaf-x.x.x.jar" PhishingGuy

📈 Sample Output
example
[SCAN] http://facebook-login.tk
[WARNING] Missing HTTPS (Not secure)
[WARNING] Suspicious TLD used
[WARNING] Keyword found: facebook
[WARNING] Keyword found: login


📊 Pie Chart Summary
The app generates a pie chart summarizing total safe vs suspicious URLs after each scan.

📄 Technologies Used

| Category        | Tech Stack                  |
| --------------- | --------------------------- |
| Language        | Java                        |
| GUI Toolkit     | Swing + FlatLaf             |
| Reporting       | JFreeChart                  |
| File Handling   | Java I/O                    |
| Regex & Parsing | `Pattern`, `Matcher`, `URL` |


📁 Folder Structure

PhishingGuy/
├── PhishingGuy.java
├── utils/
│   └── URLScanner.java
├── assets/
│   └── logo.png
├── lib/
│   ├── jfreechart-x.x.x.jar
│   └── flatlaf-x.x.x.jar
└── output.txt

📎 License
MIT License — free to use, modify, and share.

🔗 GitHub Repository
👉 Click here to visit the repo



