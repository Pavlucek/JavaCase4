import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BroadcastClient3 {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 7);

        new Sender(clientSocket).start();
        new Receiver(clientSocket).start();
    }

    static class Sender extends Thread {
        private Socket clientSocket;
        private String myName;

        public Sender(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.print("Podaj swoje imię: ");
                myName = keyboard.readLine();
                out.println(myName);

                String inputLine;
                while (!(inputLine = keyboard.readLine()).equals("bye")) {
                    if (!inputLine.contains(":")) {
                        System.out.println("Nieprawidłowy format wiadomości. Poprawny format to: Odbiorca:Wiadomość");
                        continue;
                    }

                    out.println(inputLine);
                }
                out.println(inputLine);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Połączenie z serwerem zakończone.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver extends Thread {
        private Socket clientSocket;
        private String myName;

        public Receiver(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith(myName + ":")) {
                        System.out.println("Wiadomość od " + inputLine.split(":")[0] + ": " + inputLine.split(":")[1]);
                    } else if (inputLine.startsWith("ALL:")) {
                        System.out.println("Wiadomość dla wszystkich: " + inputLine.substring(4));
                    } else {
                        System.out.println(inputLine);
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
        }
    }
}
