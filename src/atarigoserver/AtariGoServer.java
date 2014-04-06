package atarigoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtariGoServer {

    public static final int PORT = 6666;
    private LinkedList<Pair<String, Socket>> mygames;

    public AtariGoServer() throws IOException {
        mygames = new LinkedList<>();
    }

    public void acceptClients() throws IOException {
        ServerSocket game = null;
        try {
            game = new ServerSocket(PORT);
            while (true) {
                System.out.println("Astept client");
                Socket socket = game.accept();
                BufferedReader firstInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter firstOutput = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    firstOutput.println("1");
                    firstOutput.println("============");
                    firstOutput.println("1");
                    firstOutput.println("1. Singleplayer");
                    firstOutput.println("1");
                    firstOutput.println("2. Multiplayer");
                    firstOutput.println("1");
                    firstOutput.println("============");
                    String request = firstInput.readLine();
                    if (request.compareTo("1") == 0) {
                        ServerGame newgame = new ServerGame(socket);
                        newgame.start();
                        break;
                    } else if (request.compareTo("2") == 0) {
                        firstOutput.println("1");
                        firstOutput.println("0. Joc nou");
                        for (int i = 0; i < mygames.size(); i++) {
                            firstOutput.println("1");
                            firstOutput.println((i + 1) + ". " + mygames.get(i).getFirstValue());
                        }
                        int index;
                        while (true) {
                            String option = firstInput.readLine();
                            if (option.matches("0|([1-9][0-9]*)")) {
                                index = Integer.parseInt(option);
                            } else {
                                firstOutput.println("1");
                                firstOutput.println("Optiune incorecta!");
                                continue;
                            }
                            if (index == 0) {
                                String result;
                                firstOutput.println("1");
                                firstOutput.println("Introdu codul de identificare:");
                                String name = firstInput.readLine();

                                firstOutput.println("1");
                                firstOutput.println("Introdu marimea tablei de joc:");
                                while (true) {
                                    String mytable;
                                    mytable = firstInput.readLine();
                                    if (mytable.matches("[3-9]|([1-9][0-9][0-9]*)")) {
                                        result = name + "(" + "size: " + mytable + ")";
                                        break;
                                    } else {
                                        firstOutput.println("1");
                                        firstOutput.println("Valoare nerecunoscuta:");
                                    }
                                }
                                Pair newplayer = new Pair<String, Socket>(result, socket);
                                mygames.add(newplayer);
                                break;
                            } else if (index <= mygames.size()) {
                                ServerGame newgame = new ServerGame(socket, mygames.get(index - 1).getSecondValue(), mygames.get(index - 1).getFirstValue());
                                newgame.start();
                                mygames.remove(index - 1);
                                break;
                            }
                            firstOutput.println("1");
                            firstOutput.println("Valoare nerecunoscuta:");
                            continue;
                        }
                        break;
                    } else {
                        firstOutput.println("1");
                        firstOutput.println("Optiune invalida");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Am prins o exceptie: " + e.getMessage());
        } finally {
            game.close();
        }
    }

    public static void main(String[] args) {
        try {
            AtariGoServer server = new AtariGoServer();
            server.acceptClients();
        } catch (IOException ex) {
            Logger.getLogger(AtariGoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
