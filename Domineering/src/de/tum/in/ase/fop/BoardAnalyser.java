package de.tum.in.ase.fop;

import java.util.Arrays;
import java.util.Random;

public final class BoardAnalyser {
    private final Random generator;
    private static final int FIX3 = 3;
    private static final int FIX4 = 4;
    private static final int BOARD_LENGTH = 13;

    private final BoardLayout vertical;
    private final BoardLayout horizontal;

    protected BoardLayout getVertical() {
        return vertical;
    }

    protected BoardLayout getHorizontal() {
        return horizontal;
    }

    private char[][] board = new char[BOARD_LENGTH][];

    public BoardAnalyser(char[][] board, boolean noBounds) {
        this.generator = new Random();
        for (int i = 0; i < board.length; i++) {
            this.board[i] = Arrays.copyOf(board[i], board[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        this.vertical = new BoardLayout();
        this.horizontal = new BoardLayout();

        // initialize the internal variables
        analyseBoard();
        if (!noBounds) {
            calcUnplayable();
            calcLowerBounds();
            calcUpperBounds();
        }
    }

    /*
     We try to play one of four openings for domineering:
     ------------------
     |              # |
     |##            # |
     |                |
     |                |
     |                |
     | #            ##|
     | #              |
     ------------------
     */
    // to be able to play an opening without the performance loss of analyzing the entire board, this is a static method
    public static Coordinate trySimpleOpening(char[][] board, Player player) {
        // Horizontal
        if (player == Player.HORIZONTAL) {
            if (board[board.length - 1][board[0].length - 2] == 'E'
                    && board[board.length - 2][board[0].length - 2] == 'E') {
                return new Coordinate(board.length - 2, board[0].length - 2);
            }
            if (board[0][1] == 'E' && board[1][1] == 'E') {
                return new Coordinate(0, 1);
            }
        } else {
            // Vertical
            if (board[board.length - 2][0] == 'E' && board[board.length - 2][1] == 'E') {
                return new Coordinate(board.length - 2, 0);
            }
            if (board[1][board[0].length - 2] == 'E' && board[1][board[0].length - 1] == 'E') {
                return new Coordinate(1, board[0].length - 2);
            }
        }
        // no simple opening possible
        return null;
    }

    // scan board for special "areas" which contain all playable moves
    public void analyseBoard() {
        // copy board
        char[][] boardCloneVertical = new char[board.length][];
        char[][] boardCloneHorizontal = new char[board.length][];

        for (int i = 0; i < board.length; i++) {
            boardCloneVertical[i] = Arrays.copyOf(board[i], board[i].length);
            boardCloneHorizontal[i] = Arrays.copyOf(board[i], board[i].length);
        }

        /* 
        Go through board and scan for "safe areas" and "protective areas":
        
        Safe areas are spots where the current player could place his tile, but the other player can never obstruct.
        We should keep these areas as they can't be blocked by the other player.
        
        Protective areas are 2x2 areas which can be converted to "safe areas" by placing a tile on the internally saved
        "protectSpot". This move converts a protective area to a safe area. No two protective areas are allowed to be
        adjacent to each other. Otherwise the other player could destroy two protective areas by placing one tile.
        */
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (y < board[0].length - 1 && SafeArea.isSafeArea(boardCloneVertical, Player.VERTICAL, x, y)) {
                    vertical.getSafeAreas().add(new SafeArea(x, y, x, y + 1));

                    // mark the "used spaces" in the board clone so they aren't reused for other areas
                    boardCloneVertical[x][y] = 'S';
                    boardCloneVertical[x][y + 1] = 'S';
                }
                if (x < board.length - 1 && SafeArea.isSafeArea(boardCloneHorizontal, Player.HORIZONTAL, x, y)) {
                    horizontal.getSafeAreas().add(new SafeArea(x, y, x + 1, y));

                    // mark the "used spaces" in the board clone so they aren't reused for other areas
                    boardCloneHorizontal[x][y] = 'S';
                    boardCloneHorizontal[x + 1][y] = 'S';
                }

                if (y < board[0].length - 1
                        && ProtectiveArea
                        .isProtectiveArea(boardCloneVertical, Player.VERTICAL, x, y, vertical.getProtectiveAreas())) {
                    vertical.getProtectiveAreas().add(new ProtectiveArea(x, y, x + 1, y + 1, board, Player.VERTICAL));

                    // mark the 2x2 protective area on the board copy
                    boardCloneVertical[x][y] = 'P';
                    boardCloneVertical[x + 1][y] = 'P';
                    boardCloneVertical[x][y + 1] = 'P';
                    boardCloneVertical[x + 1][y + 1] = 'P';
                }

                if (x < board.length - 1
                        && ProtectiveArea
                        .isProtectiveArea(boardCloneHorizontal, Player.HORIZONTAL, x, y, horizontal.getProtectiveAreas())) {
                    horizontal.getProtectiveAreas().add(new ProtectiveArea(x, y, x + 1, y + 1, board, Player.HORIZONTAL));

                    // mark the 2x2 protective area on the board copy
                    boardCloneHorizontal[x][y] = 'P';
                    boardCloneHorizontal[x + 1][y] = 'P';
                    boardCloneHorizontal[x][y + 1] = 'P';
                    boardCloneHorizontal[x + 1][y + 1] = 'P';
                }
            }
        }

        // scan all safe areas for the so called "option areas". This means player could place the piece into one
        // square contained in the safe area and one square which is outside. This mustn't reduce the number of safe
        // moves or destroy another protective area. It is really useful to reduce the opponent's possible moves.
        for (SafeArea safeArea : vertical.getSafeAreas()) {
            int x = safeArea.getCornerUL().getX();
            int y = safeArea.getCornerUL().getY();

            if (y + 2 < board[0].length) {
                OptionArea oaLower = OptionArea
                        .getOptionArea(boardCloneVertical, Player.VERTICAL, x, y + 2);
                if (oaLower != null) {
                    vertical.getOptionAreas().add(oaLower);
                    safeArea.addOptionAreaLower(oaLower);
                }
            }
            if (y - 1 > 0) {
                OptionArea oaHigher = OptionArea
                        .getOptionArea(boardCloneVertical, Player.VERTICAL, x, y - 1);
                if (oaHigher != null) {
                    vertical.getOptionAreas().add(oaHigher);
                    safeArea.addOptionAreaHigher(oaHigher);
                }
            }
        }

        for (SafeArea safeArea : horizontal.getSafeAreas()) {
            int x = safeArea.getCornerUL().getX();
            int y = safeArea.getCornerUL().getY();

            if (x + 2 < board.length) {
                OptionArea oaLower = OptionArea
                        .getOptionArea(boardCloneHorizontal, Player.HORIZONTAL, x + 2, y);
                if (oaLower != null) {
                    horizontal.getOptionAreas().add(oaLower);
                    safeArea.addOptionAreaLower(oaLower);
                }
            }
            if (x - 1 > 0) {
                OptionArea oaHigher = OptionArea
                        .getOptionArea(boardCloneHorizontal, Player.HORIZONTAL, x - 1, y);
                if (oaHigher != null) {
                    horizontal.getOptionAreas().add(oaHigher);
                    safeArea.addOptionAreaHigher(oaHigher);
                }
            }
        }

        // used for temporary storage for the vulnerable area type

        // all places which are left and aren't single 1x1 fields are vulnerable areas
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (y < board[0].length - 1) {
                    int typeCV = VulnArea.isVulnArea(boardCloneVertical, x, y, Player.VERTICAL);
                    if (typeCV != 0) {
                        if (typeCV == 1) {
                            vertical.getVulnAreasOne().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            boardCloneVertical[x][y] = 'D';
                            boardCloneVertical[x][y + 1] = 'D';
                        } else if (typeCV == 2) {
                            vertical.getVulnAreasTwo().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            boardCloneVertical[x][y] = 'D';
                            boardCloneVertical[x][y + 1] = 'D';
                        } else if (typeCV == FIX3) {
                            // the vulnerable areas which contain a protected square are counted twice
                            vertical.getVulnAreasOne().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            vertical.getVulnAreasProtectedOne().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            boardCloneVertical[x][y] = 'D';
                            boardCloneVertical[x][y + 1] = 'D';
                        } else if (typeCV == FIX4) {
                            vertical.getVulnAreasTwo().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            vertical.getVulnAreasProtectedTwo().add(VulnArea.getVulnArea(x, y, Player.VERTICAL));
                            boardCloneVertical[x][y] = 'D';
                            boardCloneVertical[x][y + 1] = 'D';
                        }
                    }
                }
                if (x < board.length - 1) {

                    int typeCH = VulnArea.isVulnArea(boardCloneHorizontal, x, y, Player.HORIZONTAL);
                    if (typeCH != 0) {
                        if (typeCH == 1) {
                            horizontal.getVulnAreasOne().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            boardCloneHorizontal[x][y] = 'D';
                            boardCloneHorizontal[x + 1][y] = 'D';
                        } else if (typeCH == 2) {
                            horizontal.getVulnAreasTwo().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            boardCloneHorizontal[x][y] = 'D';
                            boardCloneHorizontal[x + 1][y] = 'D';
                        } else if (typeCH == FIX3) {
                            // the vulnerable areas which contain a protected square are counted twice
                            horizontal.getVulnAreasOne().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            horizontal.getVulnAreasProtectedOne().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            boardCloneHorizontal[x][y] = 'D';
                            boardCloneHorizontal[x + 1][y] = 'D';
                        } else if (typeCH == FIX4) {
                            horizontal.getVulnAreasTwo().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            horizontal.getVulnAreasProtectedTwo().add(VulnArea.getVulnArea(x, y, Player.HORIZONTAL));
                            boardCloneHorizontal[x][y] = 'D';
                            boardCloneHorizontal[x + 1][y] = 'D';
                        }
                    }
                }
            }
        }

        // the last thing to do is to sum up all squares which were available from the start and also which could be
        // played by either player.
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 'E') {
                    int ho = horizontal.getStartAvailableSquares() + 1;
                    horizontal.setStartAvailableSquares(ho);
                    int vo = vertical.getStartAvailableSquares() + 1;
                    vertical.setStartAvailableSquares(vo);
                }

                if (boardCloneVertical[x][y] == 'E'
                        && (x <= 0 || board[x - 1][y] != 'E') && (x + 1 >= board.length || board[x + 1][y] != 'E')) {
                    int ho = horizontal.getUnavailableSquares() + 1;
                    horizontal.setUnavailableSquares(ho);
                }

                if (boardCloneHorizontal[x][y] == 'E'
                        && (y <= 0 || board[x][y - 1] != 'E') && (y + 1 >= board[0].length
                        || board[x][y + 1] != 'E')) {
                    int vo = vertical.getUnavailableSquares() + 1;
                    vertical.setUnavailableSquares(vo);
                }
            }
        }
    }

    // this lower bound denotes the minimum number of moves the current player is able to play
    // the main strategy and calculation is based on Nathan Bullock's master thesis (theorem 3.5.1)
    private void calcLowerBounds() {
        if (vertical.numProtectiveAreas() % 2 != 0) {
            ProtectiveArea convertibleArea =
                    vertical.getProtectiveAreas().get(generator.nextInt(vertical.numProtectiveAreas()));
            vertical.getProtectiveAreas().remove(convertibleArea);

            VulnArea[] newVulnAreas = convertibleArea.splitIntoVulnTwo(Player.VERTICAL);

            vertical.getVulnAreasTwo().add(newVulnAreas[0]);
            vertical.getVulnAreasTwo().add(newVulnAreas[1]);
        }

        int addMove = vertical.numVulnAreasTwo() % FIX3 != 0 && vertical.numVulnAreasOne() % 2 != 0 ? 1 : 0;

        vertical.setLowerBound(vertical.numProtectiveAreas()
                + vertical.numVulnAreasTwo() / FIX3
                + vertical.numVulnAreasOne() / 2
                + vertical.numSafeAreas()
                + addMove
        );

        if (horizontal.numProtectiveAreas() % 2 != 0) {
            ProtectiveArea convertibleArea =
                    horizontal.getProtectiveAreas().get(generator.nextInt(horizontal.numProtectiveAreas()));
            horizontal.getProtectiveAreas().remove(convertibleArea);

            VulnArea[] newVulnAreas = convertibleArea.splitIntoVulnTwo(Player.HORIZONTAL);

            horizontal.getVulnAreasTwo().add(newVulnAreas[0]);
            horizontal.getVulnAreasTwo().add(newVulnAreas[1]);
        }

        addMove = horizontal.numVulnAreasTwo() % FIX3 != 0 && horizontal.numVulnAreasOne() % 2 != 0 ? 1 : 0;

        horizontal.setLowerBound(horizontal.numProtectiveAreas()
                + horizontal.numVulnAreasTwo() / FIX3
                + horizontal.numVulnAreasOne() / 2
                + horizontal.numSafeAreas()
                + addMove
        );
    }

    // get the upper bound of moves which could be played by the corresponding player.
    private void calcUpperBounds() {
        // first, calculate the upper bound for vertical
        // number of playable squares after the opponent has played his lower bound of moves
        int squaresCurr = vertical.getStartAvailableSquares() - 2 * horizontal.getLowerBound();
        // the upper bound is the number of playable squares divided by 2 -> minimum number of playable tiles
        vertical.setUpperBound((squaresCurr - vertical.getUnavailableSquares() - vertical.getUnplayableSquares()) / 2);

        // calculate the upper bound for horizontal
        squaresCurr = horizontal.getStartAvailableSquares() - 2 * vertical.getLowerBound();
        horizontal.setUpperBound((squaresCurr - horizontal.getUnavailableSquares() - horizontal.getUnplayableSquares()) / 2);
    }

    private void calcUnplayable() {
        // first, calculate the unplayable squares for vertical (use the data from horizontal player)
        BoardLayout current = vertical;
        BoardLayout opponent = horizontal;

        for (int i = 0; i < 2; i++) {
            int o1 = 0;
            int o2 = 0;
            int o3 = 0;

            for (OptionArea o : opponent.getOptionAreas()) {
                switch (o.getWeight()) {
                    case 1 -> o1++;
                    case 2 -> o2++;
                    case FIX3 -> o3++;
                    default -> {
                    }
                }
            }

            int addMove1 = opponent.numVulnAreasTwo() % FIX3 != 0
                    && opponent.numVulnAreasOne() % 2 != 0
                    && (opponent.numVulnAreasProtectedTwo() > 0 || opponent.numVulnAreasProtectedOne() > 0)
                    ? -1 : 0;

            int addMove2 = 0;
            if (!(opponent.numVulnAreasTwo() % FIX3 != 0 && opponent.numVulnAreasOne() % 2 != 0)
                    && !(opponent.numVulnAreasTwo() % FIX3 == 0 && opponent.numVulnAreasOne() % 2 == 0)) {
                if (o3 % 2 != 0) {
                    addMove2 = FIX3;
                } else if (o2 % 2 != 0) {
                    addMove2 = 2;
                } else if (o1 % 2 != 0) {
                    addMove2 = 1;
                }
            }

            current.setUnplayableSquares((opponent.numVulnAreasProtectedTwo()
                    - (opponent.numVulnAreasTwo() / FIX3
                    - (opponent.numVulnAreasTwo() - opponent.numVulnAreasProtectedTwo()) / FIX3))
                    + (opponent.numVulnAreasProtectedOne()
                    - (opponent.numVulnAreasOne() / 2
                    - (opponent.numVulnAreasOne() - opponent.numVulnAreasProtectedOne()) / 2))
                    + FIX3 * (o3 / 2) + 2 * (o2 / 2) + o1 / 2 + addMove1 + addMove2);

            // then, do the same thing for horizontal
            current = horizontal;
            opponent = vertical;
        }
    }
}
