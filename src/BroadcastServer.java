import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastServer {
    private static ConcurrentHashMap<String, PrintWriter> clientWriters = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7);
        System.out.println("Serwer oczekuje na połączenie...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private String clientName;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                clientName = in.readLine();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.put(clientName, out);
                System.out.println("Klient " + clientName + " połączył się.");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("bye")) {
                        break;
                    }

                    if (!inputLine.contains(":")) {
                        System.out.println("Nieprawidłowy format wiadomości.");
                        continue;
                    }

                    String[] parts = inputLine.split(":", 2);
                    String recipient = parts[0];
                    String message = parts[1];

                    System.out.println(clientName + " do " + recipient + ": " + message);

                    if (recipient.equals("ALL")) {
                        broadcast(clientName + ": " + message);
                    } else {
                        sendTo(recipient, clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            clientWriters.remove(clientName);
            System.out.println("Klient " + clientName + " rozłączył się.");
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters.values()) {
                writer.println(message);
            }
        }

        private void sendTo(String recipient, String message) {
            PrintWriter writer = clientWriters.get(recipient);
            if (writer != null) {
                writer.println(message);
            } else {
                out.println("Nie można wysłać wiadomości do " + recipient);
            }
        }
    }
}
