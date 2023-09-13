package de.tum.in.ase.fop;

import java.util.Arrays;
import java.util.HashMap;

// simple wrapper for java hashmap structure
public class BoardStorage {
    private final HashMap<Integer, StateInfo> scoreStorage = new HashMap<>();

    public void put(char[][] board, StateInfo info) {
        scoreStorage.put(Arrays.hashCode(flatten(board)), info);
    }

    // get the score for the given board, returns null if no score is found
    public StateInfo get(char[][] board) {
        return scoreStorage.get(Arrays.hashCode(flatten(board)));
    }

    // small storage class for the values which should be stored for each registered board
    public static class StateInfo {
        // the calculated score
        private float score;
        // either the score is a final one, only one for the alpha, or only one for the beta value
        private char type;
        // at which depth was the score determined?
        private int depth;

        public float getScore() {
            return score;
        }

        public char getType() {
            return type;
        }

        public int getDepth() {
            return depth;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public void setType(char type) {
            this.type = type;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }

    // convert 2d-array to 1d
    private char[] flatten(char[][] board) {
        char[] output = new char[board.length * board[0].length];
        int x = 0;
        while (x < board.length) {
            System.arraycopy(board[x], 0, output, x * board[0].length, board[0].length);
            x++;
        }
        return output;
    }
}
