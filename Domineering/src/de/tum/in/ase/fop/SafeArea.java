package de.tum.in.ase.fop;

public class SafeArea {

    private SafeArea optionSafeUpper;
    private SafeArea optionSafeLower;
    private final Coordinate cornerUL;
    private final Coordinate cornerLR;

    public SafeArea(int xOne, int yOne, int xTwo, int yTwo) {
        cornerUL = new Coordinate(xOne, yOne);  // "upper-left"-corner
        cornerLR = new Coordinate(xTwo, yTwo);
    }

    // same as for the other areas, tests if the given coordinates qualify for being a safe area
    public static boolean isSafeArea(char[][] board, Player p, int x, int y) {
        if (p == Player.HORIZONTAL) {
            return board[x][y] == 'E' && board[x + 1][y] == 'E'

                    && (y - 1 < 0
                    || board[x][y - 1] == 'X'
                    && board[x + 1][y - 1] == 'X')

                    && (y + 1 >= board[0].length
                    || board[x][y + 1] == 'X'
                    && board[x + 1][y + 1] == 'X');

        } else {
            return board[x][y] == 'E' && board[x][y + 1] == 'E'

                    && (x - 1 < 0
                    || board[x - 1][y] == 'X'
                    && board[x - 1][y + 1] == 'X')

                    && (x + 1 >= board.length
                    || board[x + 1][y] == 'X'
                    && board[x + 1][y + 1] == 'X');

        }
    }

    public void addOptionAreaHigher(OptionArea oaHigher) {
        optionSafeUpper = new SafeArea(oaHigher.getCornerUL().getX(), oaHigher.getCornerUL().getY(), getCornerUL().getX(),
                getCornerUL().getY()
        );
    }

    public void addOptionAreaLower(OptionArea oaLower) {
        optionSafeLower = new SafeArea(getCornerLR().getX(), getCornerLR().getY(), oaLower.getCornerUL().getX(),
                oaLower.getCornerUL().getY()
        );
    }

    public Coordinate getCornerUL() {
        return cornerUL;
    }

    public Coordinate getCornerLR() {
        return cornerLR;
    }

    protected SafeArea getOptionSafeUpper() {
        return optionSafeUpper;
    }

    protected SafeArea getOptionSafeLower() {
        return optionSafeLower;
    }
}
