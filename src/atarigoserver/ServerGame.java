package atarigoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGame extends Thread {

    private Socket firstSocket = null;
    BufferedReader firstInput = null;
    PrintWriter firstOutput = null;
    private Socket secondSocket = null;
    BufferedReader secondInput = null;
    PrintWriter secondOutput = null;
    boolean isSinglePlayer = true;
    int size;
    AtariGo game;
    Player first;
    Player second;

    public ServerGame(Socket singleplayer) {
        try {
            firstSocket = singleplayer;
            firstInput = new BufferedReader(new InputStreamReader(firstSocket.getInputStream()));
            firstOutput = new PrintWriter(firstSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        isSinglePlayer = true;
    }

    public ServerGame(Socket firstPlayer, Socket secondPlayer, String gameName) {
        try {
            firstSocket = firstPlayer;
            firstInput = new BufferedReader(new InputStreamReader(firstSocket.getInputStream()));
            firstOutput = new PrintWriter(firstSocket.getOutputStream(), true);

            secondSocket = secondPlayer;
            secondInput = new BufferedReader(new InputStreamReader(secondSocket.getInputStream()));
            secondOutput = new PrintWriter(secondSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        isSinglePlayer = false;

        first = new HumanPlayer("Bob");
        second = new HumanPlayer("Bill");
        String token = gameName.substring(gameName.lastIndexOf(": ") + 2, gameName.length() - 1);
        size = Integer.parseInt(token);
        game = new AtariGo(first, second, size);

    }

    @Override
    public void run() {
        if (isSinglePlayer) {
            singlePlayerGame();
        } else {
            multiPlayerGame();
        }
        try {
            firstSocket.close();
            secondSocket.close();
            firstInput.close();
            secondInput.close();
        } catch (IOException ex) {
            System.err.println("Socketul nu poate fi inchis");
        }

    }

    public void writeMessage(PrintWriter player, String message) {
        player.println("1");
        player.println(message);
    }

    public void writeResult(PrintWriter player, String message) {
        player.println("0");
        player.println(message);
    }

    public void writeTable(PrintWriter player) {
        player.println("2");
        int a[][] = game.getTable();
        int size = a[0].length;
        player.println(Integer.toString(size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                player.println(Integer.toString(a[i][j]));
            }
        }
    }

    public void singlePlayerGame() {
        try {
            int newsize;
            writeMessage(firstOutput, "Care este marimea tablei de joc?");
            while (true) {
                String option;
                option = firstInput.readLine();
                if (option.matches("[3-9]|([1-9][0-9]*)")) {
                    newsize = Integer.parseInt(option);
                    break;
                } else {
                    writeMessage(firstOutput, "Valoare nerecunoscuta!");
                }
            }
            size = newsize;
            first = new HumanPlayer("Bob");
            second = new RandomComputerPlayer("CPU");

            game = new AtariGo(first, second, size);

            while (true) {
                writeTable(firstOutput);

                Integer line;
                Integer column;

                while (true) {
                    String option;
                    writeMessage(firstOutput, "Introdu linia: ");
                    option = firstInput.readLine();
                    if (option.matches("0|([1-9][0-9]*)")) {
                        line = Integer.parseInt(option);
                    } else {
                        line = -1;
                    }
                    if (line < 1 || line > size) {
                        writeMessage(firstOutput, "Valoare nerecunoscuta! Mai incearca o data!");
                    } else {
                        break;
                    }
                }
                while (true) {
                    String option;
                    writeMessage(firstOutput, "Introdu coloana: ");
                    option = firstInput.readLine();
                    if (option.matches("0|([1-9][0-9]*)")) {
                        column = Integer.parseInt(option);
                    } else {
                        column = -1;
                    }
                    if (line < 1 || line > size) {
                        writeMessage(firstOutput, "Valoare nerecunoscuta! Mai incearca o data!");
                    } else {
                        break;
                    }
                }
                Pair<Integer, Integer> nextmove = new Pair<>(line, column);
                int result = game.validate(nextmove);
                if (result == 0) {
                    int comresult = -1;
                    game.changePlayer();
                    while (comresult < 0) {
                        comresult = game.validate(second.generatemove(size));
                    }
                    if (comresult == 0) {
                        game.changePlayer();
                    } else {
                        writeResult(firstOutput, "Calculatorul a castigat!");
                        return;
                    }
                } else if (result == 1) {
                    writeResult(firstOutput, "Ai castigat!");
                    return;
                } else if (result == -1) {
                    writeMessage(firstOutput, "Mutare invalida!");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPlayerMove(BufferedReader playerinput, PrintWriter playeroutput) throws IOException {
        int result;
        Integer line;
        Integer column;
        while (true) {
            String option;
            writeMessage(playeroutput, "Introdu linia: ");
            option = playerinput.readLine();
            if (option.matches("0|([1-9][0-9]*)")) {
                line = Integer.parseInt(option);
            } else {
                line = -1;
            }
            if (line < 1 || line > size) {
                writeMessage(playeroutput, "Valoare nerecunoscuta! Mai incearca o data!");
            } else {
                break;
            }
        }
        while (true) {
            String option;
            writeMessage(playeroutput, "Introdu coloana: ");
            option = playerinput.readLine();
            if (option.matches("0|([1-9][0-9]*)")) {
                column = Integer.parseInt(option);
            } else {
                column = -1;
            }
            if (line < 1 || line > size) {
                writeMessage(playeroutput, "Valoare nerecunoscuta! Mai incearca o data!");
            } else {
                break;
            }
        }
        Pair<Integer, Integer> nextmove = new Pair<>(line, column);
        result=game.validate(nextmove);
        if(result==-1){
            writeMessage(playeroutput, "Mutare invalida!");
        }
        return result;

    }

    public void multiPlayerGame() {
        boolean isFirstPlayer = true;
        try {
            while (true) {
                int result;
                writeTable(firstOutput);
                writeTable(secondOutput);
                if (isFirstPlayer) {
                    writeMessage(firstOutput, "Randul tau!");
                    writeMessage(secondOutput, "Randul adversarului!");
                    result=getPlayerMove(firstInput, firstOutput);

                } else {
                    writeMessage(secondOutput, "Randul tau!");
                    writeMessage(firstOutput, "Randul adversarului!");
                    result=getPlayerMove(secondInput, secondOutput);
                }
                if(result==0){
                    game.changePlayer();
                    isFirstPlayer=!isFirstPlayer;
                } else if (result==1){
                    writeResult(firstOutput,"Ai castigat!!!");
                    writeResult(secondOutput,"Ai pierdut!!!");
                    return;
                } else if (result==2){
                    writeResult(secondOutput,"Ai castigat!!!");
                    writeResult(firstOutput,"Ai pierdut!!!");
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
