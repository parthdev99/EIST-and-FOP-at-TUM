package de.tum.in.ase.fop;

import java.util.Arrays;

public class Game {
    private AI verticalAI;
    private AI horizontalAI;
    private Mode mode;
    //char [][] board=new char[13][13];
    private Player winner;
    private boolean winnerCall = false;
    private static final int BOARD_LENGTH = 13;

    public Game(AI verticalAI, AI horizontalAI, Mode mode) {
        this.verticalAI = verticalAI;
        this.horizontalAI = horizontalAI;
        this.mode = mode;
        /*for (char[] line : board) {
            Arrays.fill(line, 'E');
        }*/

    }

    public void runGame() {
        // start by initializing a new game, this way, runGame() could potentially be run more than once
        char[][] board = new char[BOARD_LENGTH][BOARD_LENGTH];
        for (char[] line : board) {
            Arrays.fill(line, 'E');
        }
        Coordinate move;

        // starting player -> always the vertical player for our game version
        Player currentPlayer = Player.VERTICAL;

        //if (visual) GameVisualizer.printBoard(board);
        while (true) {
            // is there another valid move possible for the current player
            int check = canPlay(board, currentPlayer);
            if (check == 0) {
                //if (visual) System.out.println("" + currentPlayer + " can't place another piece, he lost!");
                break;
            }
            //if (visual) System.out.println("" + currentPlayer + "'s move:");
            // ask the AI for a new move (coordinates for a new piece)
            if (currentPlayer == Player.HORIZONTAL) {
                move = horizontalAI.playMove(board, Player.HORIZONTAL, mode);
                if (move.getX() < 0
                        || move.getY() >= BOARD_LENGTH
                        || move.getX() + 1 >= BOARD_LENGTH
                        || move.getX() >= BOARD_LENGTH
                        || move.getY() < 0
                        || board[move.getX()][move.getY()] != 'E'
                        || board[move.getX() + 1][move.getY()] != 'E') {
                    break;
                }
                board[move.getX()][move.getY()] = 'H';
                board[move.getX() + 1][move.getY()] = 'H';

            } else {

                move = verticalAI.playMove(board, Player.VERTICAL, mode);
                if (move.getX() < 0
                        || move.getY() >= BOARD_LENGTH
                        || move.getY() < 0
                        || move.getY() + 1 >= BOARD_LENGTH
                        || move.getX() >= BOARD_LENGTH
                        || board[move.getX()][move.getY()] != 'E'
                        || board[move.getX()][move.getY() + 1] != 'E') {
                    break;
                }
                board[move.getX()][move.getY()] = 'V';
                board[move.getX()][move.getY() + 1] = 'V';


            }

            //if (visual) System.out.println("Places a piece on " + move);

            // is the returned move actually valid
            /*if (checkInvalidMoveSimple(board, move, currentPlayer)) {
                // if (visual) System.out.println("!!!! INVALID MOVE BY " + currentPlayer + " !!!!");
                break;
            }*/

            //makeMove(board, move, currentPlayer);
            //if (visual) GameVisualizer.printBoard(board);

            // change the play for the next round
            currentPlayer = currentPlayer.getOtherPlayer();
        }
        winnerCall = true;
        winner = currentPlayer.getOtherPlayer();
    }

    /*// generate an empty board filled with 'E'
    private char[][] generateEmptyBoard() {
        char[][] board = new char[BOARD_LENGTH][BOARD_LENGTH];
        for (char[] column : board) {
            Arrays.fill(column, 'E');
        }
        return board;
    }

    // fulfill a returned move if it is valid
    private void makeMove(char[][] board, Coordinate move, Player p) {
        board[move.getX()][move.getY()] = p == Player.HORIZONTAL ? 'H' : 'V';
        board[move.getX() + (p == Player.HORIZONTAL ? 1 : 0)][move.getY() + (p == Player.HORIZONTAL ? 0 : 1)] =
                p == Player.HORIZONTAL ? 'H' : 'V';
    }

    // test if a returned move is valid
    private boolean checkInvalidMoveSimple(char[][] board, Coordinate move, Player p) {
        return move.getX() < 0
                || p == Player.HORIZONTAL && board[move.getX() + 1][move.getY()] != 'E'
                || p == Player.VERTICAL && board[move.getX()][move.getY() + 1] != 'E'
                || move.getY() >= BOARD_LENGTH
                || p == Player.HORIZONTAL && move.getX() + 1 >= BOARD_LENGTH
                || p == Player.VERTICAL && move.getY() + 1 >= BOARD_LENGTH
                || move.getX() >= BOARD_LENGTH
                || move.getY() < 0
                || board[move.getX()][move.getY()] != 'E';

    }*/

    // is there any move left for the current player? If not, the other player has one!
    private int canPlay(char[][] board, Player p) {
        if (p == Player.VERTICAL) {
            for (char[] column : board) {
                for (int y = 0; y < column.length - 1; y++) {
                    if (column[y] == 'E' && column[y + 1] == 'E') {
                        return 1;
                    }
                }
            }
        } else {
            for (int y = 0; y < board[0].length; y++) {
                for (int x = 0; x < board.length - 1; x++) {
                    if (board[x][y] == 'E' && board[x + 1][y] == 'E') {
                        return 1;
                    }
                }
            }

        }
        return 0;
    }

    public AI getWinner() {
        if (winnerCall) {
            return winner == Player.VERTICAL ? verticalAI : horizontalAI;
        } else {
            return null;
        }
    }


    /*public void runGame() {
        Coordinate ans;
        while(true) {
            ans=verticalAI.playMove(board,Player.HORIZONTAL,mode);
            if(ans==null) {
                break;
            }
            board[ans.getX()][ans.getY()]='V';
            board[ans.getX()][ans.getY()+1]='V';
            turn=true;

            ans=horizontalAI.playMove(board,Player.VERTICAL,mode);
            if(ans==null) {
                break;
            }
            board[ans.getX()][ans.getY()]='H';
            board[ans.getX()+1][ans.getY()]='H';
            turn=false;

        }
    }

    public AI getWinner() {
        return turn?verticalAI:horizontalAI;
    }*/

    public static void main(String[] args) {
        AI A1 = new PenguAI();
        AI a2 = new PenguAI();
        Game g1 = new Game(A1, a2, Mode.EASY);
        g1.runGame();
        System.out.println(g1.getWinner());
        /*char[][] board = new char[13][13];
        for (char[] line : board) {
            Arrays.fill(line, 'E');
        }
        board[11][0] = 'V';
        board[12][0] = 'V';
        Coordinate temp = a1.playMove(board, Player.VERTICAL, Mode.EASY);*/
    }
}
