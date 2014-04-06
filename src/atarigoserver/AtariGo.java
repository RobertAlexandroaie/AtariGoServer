package atarigoserver;

import java.io.Serializable;

public class AtariGo implements Go, Serializable {

    private static final long serialVersionUID = 1L;
    private Player playerOne;
    private Player playerTwo;
    private int size;
    private int table[][];
    private boolean isPlayer1Turn;
    public boolean singleplayer;

    AtariGo(Player player1, Player player2, int dimension) {
        playerOne = player1;
        playerTwo = player2;
        if (player1.getClass().isInstance(player2)) {
            singleplayer = false;
        } else {
            singleplayer = true;
        }
        size = dimension;
        table = new int[size][size];
        isPlayer1Turn = true;
    }
    /*
     * Schimbarea jucatorului
     */
    public int[][] getTable(){
        return table;
    }
    public void changePlayer() {
        if (isPlayer1Turn) {
            isPlayer1Turn = false;
        } else {
            isPlayer1Turn = true;
        }
        
    }
    /*
     * Verificarea corectitudinii mutarii
     * daca jucatorul vrea o pauza, jocul se opreste temporar
     * altfel se muta piesa
     * se verifica daca jocul s-a terminat
     */

    @Override
    public int validate(Pair<Integer, Integer> playerMove) {
        Integer line;
        Integer column;
        line = playerMove.getFirstValue() - 1;
        column = playerMove.getSecondValue() - 1;
        if (line == -1 || column == -1) {
            return 10;
        } else if (table[line][column] == 0) {
            if (isPlayer1Turn) {
                table[line][column] = 1;
            } else {
                table[line][column] = 2;
            }
            if (gameover(line, column)) {
                if (isPlayer1Turn) {
                    return 1;
                } else {
                    return 2;
                }
            }
            return 0;
        } else {
            if (isPlayer1Turn) {
                return -1;
            } else if (playerTwo.getClass().isInstance(playerOne)) {
                return -1;
            }
        }
        return -2;
    }
    /*
     * Se verifica daca pe langa piesa pusa, exista piese ale celuilalt jucator
     * daca exista, se parcurge tot lantul oponentului pana se gaseste un spatiu liber
     */

    private boolean gameover(Integer line, Integer column) {
        int opponentvalue = table[line][column];
        if (line < size - 1 && table[line + 1][column] != 0 && table[line + 1][column] != opponentvalue) {
            int visit[][] = new int[size][size];
            if (!recursivesearch(visit, line + 1, column, opponentvalue)) {
                return true;
            }
        }
        if (column < size - 1 && table[line][column + 1] != 0 && table[line][column + 1] != opponentvalue) {
            int visit[][] = new int[size][size];
            if (!recursivesearch(visit, line, column + 1, opponentvalue)) {
                return true;
            }
        }
        if (column > 0 && table[line][column - 1] != 0 && table[line][column - 1] != opponentvalue) {
            int visit[][] = new int[size][size];
            if (!recursivesearch(visit, line, column - 1, opponentvalue)) {
                return true;
            }
        }
        if (line > 0 && table[line - 1][column] != 0 && table[line - 1][column] != opponentvalue) {
            int visit[][] = new int[size][size];
            if (!recursivesearch(visit, line - 1, column, opponentvalue)) {
                return true;
            }
        }
        return false;
    }
    /*
     * Verifica daca este vecin cu un spatiu liber
     * Apoi verifica daca mai exista piese in lant nevizitate vecine cu piesa respectiva
     */

    private boolean recursivesearch(int[][] visit, int line, int column, int opponentvalue) {
        visit[line][column] = 1;
        if (line < size - 1 && table[line + 1][column] != opponentvalue) {
            if (table[line + 1][column] == 0) {
                return true;
            } else if (visit[line + 1][column] == 0) {
                if (recursivesearch(visit, line + 1, column, opponentvalue)) {
                    return true;
                }
            }
        }
        if (column < size - 1 && table[line][column + 1] != opponentvalue) {
            if (table[line][column + 1] == 0) {
                return true;
            } else if (visit[line][column + 1] == 0) {
                if (recursivesearch(visit, line, column + 1, opponentvalue)) {
                    return true;
                }
            }
        }
        if (column > 0 && visit[line][column - 1] == 0 && table[line][column - 1] != opponentvalue) {
            if (table[line][column - 1] == 0) {
                return true;
            } else if (visit[line][column - 1] == 0) {
                if (recursivesearch(visit, line, column - 1, opponentvalue)) {
                    return true;
                }
            }
        }
        if (line > 0 && visit[line - 1][column] == 0 && table[line - 1][column] != opponentvalue) {
            if (table[line - 1][column] == 0) {
                return true;
            } else if (visit[line - 1][column] == 0) {
                if (recursivesearch(visit, line - 1, column, opponentvalue)) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
     * Le da voie jucatorilor sa mute, in functie de randul fiecaruia
     */

    public int nextMove() {
        if (isPlayer1Turn) {
            return 0;//validate(playerOne.generatemove(size));
        } else {
            return 0;//validate(playerTwo.generatemove(size));
        }
        
    }
}
