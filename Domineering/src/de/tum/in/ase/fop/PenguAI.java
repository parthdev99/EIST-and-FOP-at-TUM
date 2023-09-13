package de.tum.in.ase.fop;

import java.util.Arrays;

public class PenguAI extends AI {
    private float[] factors = null;
    private static final int FIX0 = 0;
    private static final int FIX1 = 1;
    private static final int FIX2 = 2;
    private static final int FIX3 = 3;
    private static final int FIX4 = 4;
    private static final int FIX5 = 5;
    private static final int FIX6 = 6;
    private static final int FIX7 = 7;
    private static final int FIX8 = 8;
    private static final int FIX9 = 9;
    private static final int FIX10 = 10;
    private static final int FIX11 = 11;
    private static final int FIX12 = 12;
    private static final int FIX13 = 13;
    private static final float IDX0 = 6.141892f;     // lower bound
    private static final float IDX1 = 3.323705f;
    private static final float IDX2 = 1.5304062f;     // upper bound
    private static final float IDX3 = 2.5675583f;
    private static final float IDX4 = 10.425653f;     // safe areas
    private static final float IDX5 = -15.922241f;
    private static final float IDX6 = 2.0729046f;     // vuln areas
    private static final float IDX7 = -3.497818f;
    private static final float IDX8 = -3.3107972f;    // protective areas
    private static final float IDX9 = -11.012934f;
    private static final float IDX10 = -0.85778457f;    // unavailable areas
    private static final float IDX11 = 5.7875576f;
    private static final float IDX12 = 0.80485183f;     // unplayable areas
    private static final float IDX13 = 1.9488539f;
    private static final int FIX20 = 20;
    private static final int FIX95 = 95;
    private static final float FIX1_035 = 1.035f;


    // store the already calculated scores for each board configuration for the ultimate performance boost
    private static BoardStorage scoreMapHorizontalStarter = new BoardStorage();
    private static BoardStorage scoreMapVerticalStarter = new BoardStorage();

    @Override
    public synchronized Coordinate playMove(char[][] board, Player player, Mode mode) {
        // play an opening -> for better performance (an empty board is pretty expensive to calculate)
        Coordinate opening = BoardAnalyser.trySimpleOpening(board, player);
        if (opening != null) {
            return opening;
        }

        // for medium and hard mode, we use our minimax-algorithm
        return findBestMove(anonymizeBoard(board), player);
    }

    /*
        Because of the nature of our board layout, we only take into consideration whether a square is occupied (X) or
        empty (E). This greatly increases the performance!
     */
    private char[][] anonymizeBoard(char[][] board) {
        char[][] boardCopy = new char[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                boardCopy[x][y] = board[x][y] == 'V' || board[x][y] == 'H' ? 'X' : 'E';
            }
        }
        return boardCopy;
    }

    // tests how far the game has already commenced and adjusts the depth limit accordingly
    private int depthForBoardState(char[][] board) {
        int blocked = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (c != 'E') {
                    blocked++;
                }
            }
        }

        // for the dynamic depth adjustment, I used a sigmoid-like curve which was fitted by probing values in geogebra
        return (int) (FIX20 / (FIX1 + Math.pow(FIX1_035, -blocked + FIX95))) + FIX1;
    }

    /*
        Recursively tests all possible moves (really slow for early board stages)

        The approach is based on the concept of a minimax tree. The root configuration is the current board. Each branch
        is a possible move for either us or the opponent. As soon as one of the players looses or the depth limit is
        reached, the current board configuration is evaluated. Each player (stage in the tree) tries to play the best
        strategy. This means is the "score" is how well we (Player A) are doing, than Player B tries to play the move
        which has the lowest score associated with it. Player A tries to play the move with the maximum score and thus
        the resulting tree is composed of alternating minimum and maximum phases.
     */
    private Coordinate findBestMove(char[][] board, Player player) {
        Coordinate[] possibleMoves = generateNextPossibleMoves(board, player, null, false);
        Coordinate currentBestMove = null;

        // this "currentBestScore" should be updated as soon as a score higher than the lowest possible score is found
        float currentBestScore = Float.NEGATIVE_INFINITY;

        int maxDepth = depthForBoardState(board);

        // just try these possibleMoves in their natural order
        for (Coordinate move : possibleMoves) {
            applyMove(board, move, player);
            float nextBestScore = minimaxAlphaBeta(
                    board,
                    player.getOtherPlayer(),
                    player,
                    maxDepth,
                    currentBestScore,
                    Float.POSITIVE_INFINITY
            );
            undoMove(board, move, player);
            // because the current player is always the maximizing player and we can't prune, we have to go through
            // each entry and always update the current maximum score and the associated move
            if (nextBestScore > currentBestScore) {
                currentBestScore = nextBestScore;
                currentBestMove = move;
            }
        }
        if (currentBestMove == null) {
            // if all fails and all scores are somehow equal to Integer.MIN_VALUE, we take the first item from the
            // generated possible moves and return it.
            return generateNextPossibleMoves(board, player, null, true)[0];
        }
        return currentBestMove;
    }

    /*
        The key in efficient solution finding is reducing the unnecessary calculations in our tree. The "scoreSituation"
        method always tests if winning is still possible and otherwise returns a low score.

        Alpha-Beta-Pruning:
            -> alpha: currently the highest score (reference for the maximising player)
            -> beta: currently the lowest score (reference for the minimising player)

            Case 1:
            -------
            We are currently the maximizing player. The previous player (minimizing) gave us his updated "beta"-score.
            Only if our score is lower than the given beta score, our branch is of interest, otherwise there exists
            another branch with a lower score and because the previous player was minimizing, he would then take the other
            branch. This means that if our current score is higher than the beta score, we can break the loop and return.

            Case 2:
            -------
            We are the minimizing player. The previous player was maximizing and gave the current maximum score in the
            alpha parameter. If our "best score" (the minimum) drops below the alpha score, we can break the search and
            return, because even though this situation would be better for us, the previous player wouldn't take our
            branch into consideration.

        This special implementation of the minimax algorithm which negates the next call with changed alpha and beta
        values is described in the doctoral thesis of Prof. dr. H.J. van den Herik: "Memory versus Search in Games".
        It also involves storing the values which were calculated for even higher performance. Some more ideas came
        from the already often cited master thesis of Nathan Bullock about the game domineering. Both papers didn't
        contain actual code or the code was not reviewed by me.
     */
    private float minimaxAlphaBeta(char[][] board, Player currentPlayer, Player startingPlayer, int depth,
                                   float alpha, float beta) {
        float oldAlpha = alpha;
        float oldBeta = beta;

        // first, try to load the score from the scoreMap
        BoardStorage.StateInfo saveState = loadScore(board, startingPlayer);

        if (saveState != null && saveState.getDepth() >= depth) {
            if (saveState.getType() == '-' && saveState.getScore() > alpha) {
                alpha = saveState.getScore();
            } else if (saveState.getType() == '+' && saveState.getScore() < beta) {
                beta = saveState.getScore();
            }

            if (saveState.getType() == '=' || alpha >= beta) {
                return saveState.getScore();
            }
        }

        // The new BoardAnalyser object is used by both the scoring function and the possible moves generator. For
        // performance improvement, it is only created once.
        BoardAnalyser bA = new BoardAnalyser(board, false);
        float score = scoreSituation(
                depth,
                (startingPlayer == Player.VERTICAL) ? bA.getVertical() : bA.getHorizontal(),
                (startingPlayer == Player.VERTICAL) ? bA.getHorizontal() : bA.getVertical()
        );

        if (score != Float.NEGATIVE_INFINITY) {
            return score;
        }

        Coordinate[] possibleMoves = generateNextPossibleMoves(board, currentPlayer, bA, false);
        float nextBestScore;

        // if true -> current player tries to maximize the score
        boolean max = currentPlayer == startingPlayer;

        // never go down / up with the score, but set the given best to be the lowest score for the "current best"
        float currentBestScore = max ? alpha : beta;

        // goes through all possible moves which are basically just placing tiles in all calculated board cover
        // regions. These regions are generated using a BoardAnalyser object.
        for (Coordinate move : possibleMoves) {
            applyMove(board, move, currentPlayer);
            nextBestScore = minimaxAlphaBeta(
                    board,
                    currentPlayer.getOtherPlayer(),
                    startingPlayer,
                    depth - 1,
                    max ? currentBestScore : alpha,
                    max ? beta : currentBestScore
            );
            undoMove(board, move, currentPlayer);

            // either we try to maximize the score, then update if the new score is higher than the current best
            // or we try to minimize the score and therefore only update if the new score is lower than the current best
            if (max && nextBestScore > currentBestScore || !max && nextBestScore < currentBestScore) {
                currentBestScore = nextBestScore;
            }

            // this is the important alpha-beta-pruning improvement over the classic minimax-algorithm. The details
            // of the implementations are written in the comment above this method.
            if (max && currentBestScore >= beta || !max && currentBestScore <= alpha) {
                break;
            }
        }

        BoardStorage.StateInfo boardState = new BoardStorage.StateInfo();
        boardState.setScore(currentBestScore);
        boardState.setDepth(depth);

        if (currentBestScore <= oldAlpha) {
            boardState.setType('+');
        } else if (currentBestScore >= oldBeta) {
            boardState.setType('-');
        } else {
            boardState.setType('=');
        }

        saveScore(board, boardState, startingPlayer);
        return currentBestScore;
    }

    private BoardStorage.StateInfo loadScore(char[][] board, Player starter) {
        return starter == Player.VERTICAL ? scoreMapVerticalStarter.get(board) : scoreMapHorizontalStarter.get(board);
    }

    private void saveScore(char[][] board, BoardStorage.StateInfo state, Player starter) {
        if (starter == Player.VERTICAL) {
            scoreMapVerticalStarter.put(board, state);
        } else {
            scoreMapHorizontalStarter.put(board, state);
        }
    }

    /*
        This "exit-function" calculates the current score if a tree leaf is reached. The current method call
        represents a tree leaf if either the maximum recursion depth is reached or either one of the players can't
        play another tile or the current player already won the game because he could always place more tiles than
        the other player.

        Most switches contained in this function are based on the calculated board cover (BoardAnalyser) and the
        findings of Nathan Bullock's master thesis "Domineering: Solving Large Combinatorial Search Spaces".

        The current approach is to assign weights to each parameter we have available and figure out the optimal values.
     */
    private float scoreSituation(int currentDepth, BoardLayout starter, BoardLayout opponent) {
        if (currentDepth <= 0
                || starter.getLowerBound() <= 0                       // we already lost
                || opponent.getLowerBound() <= 0                             // the opponent already lost
                || starter.getLowerBound() > opponent.getUpperBound()             // win for the starting player (NB)
                || opponent.getLowerBound() >= starter.getUpperBound()            // win for the opponent (NB)
        ) {
            /*
            Available values and proposed weights / factors

            (these are just first guesses and still have to be refined)
            Basically we "punish" the ai / give worse scores for good situations of the opponent than for good
            situations for the current player.

            The concrete values were determined using an evolutional approach where two instances of
            the HardMinMax each with different factors played against each other over multiple round, with the winner
            proceeding.
             */

            if (factors == null) {
                factors = new float[]{
                        IDX0,     // lower bound
                        IDX1,
                        IDX2,     // upper bound
                        IDX3,
                        IDX4,     // safe areas
                        IDX5,
                        IDX6,      // vuln areas
                        IDX7,
                        IDX8,    // protective areas
                        IDX9,
                        IDX10,    // unavailable areas
                        IDX11,
                        IDX12,     // unplayable areas
                        IDX13
                };
            }

            return starter.getLowerBound() * factors[FIX0]
                    + opponent.getLowerBound() * factors[FIX1]
                    + starter.getUpperBound() * factors[FIX2]
                    + opponent.getUpperBound() * factors[FIX3]
                    + starter.numSafeAreas() * factors[FIX4]
                    + opponent.numSafeAreas() * factors[FIX5]
                    + (starter.numVulnAreasOne() + starter.numVulnAreasTwo()) * factors[FIX6]
                    + (opponent.numVulnAreasOne() + opponent.numVulnAreasTwo()) * factors[FIX7]
                    + starter.numProtectiveAreas() * factors[FIX8]
                    + opponent.numProtectiveAreas() * factors[FIX9]
                    + starter.getUnavailableSquares() * factors[FIX10]
                    + opponent.getUnavailableSquares() * factors[FIX11]
                    + starter.getUnplayableSquares() * factors[FIX12]
                    + opponent.getUnplayableSquares() * factors[FIX13];
        }
        // if the recursion should not be stopped, return a score that would never be calculated
        // I don't know if NEGATIVE_INFINITY is better than MIN_VALUE
        return Float.NEGATIVE_INFINITY;
    }

    // returns the entered board configuration with the given move applied
    private void applyMove(char[][] board, Coordinate move, Player player) {
        // set the first square occupied
        board[move.getX()][move.getY()] = 'X';

        // based on the player, set the second square occupied
        if (player != Player.VERTICAL) {
            board[move.getX() + 1][move.getY()] = 'X';
        } else {
            board[move.getX()][move.getY() + 1] = 'X';
        }
    }

    private void undoMove(char[][] board, Coordinate move, Player player) {
        // set the first square unoccupied
        board[move.getX()][move.getY()] = 'E';

        // based on the player, set the second square unoccupied
        if (player != Player.HORIZONTAL) {
            board[move.getX()][move.getY() + 1] = 'E';
        } else {
            board[move.getX() + 1][move.getY()] = 'E';
        }
    }

    // runs an entered board analyzer or creates a new one. Returns the concatenated board cover areas.
    private Coordinate[] generateNextPossibleMoves(char[][] board, Player player, BoardAnalyser bA, boolean include) {
        if (bA == null) {
            bA = new BoardAnalyser(board, true);
        }
        // select the required BoardLayout object
        BoardLayout bL = player == Player.VERTICAL ? bA.getVertical() : bA.getHorizontal();

        // generate a new Array with approximately the maximum size required
        Coordinate[] outputMoves = new Coordinate[2 * bL.numProtectiveAreas()
                + bL.numVulnAreasOne()
                + bL.numVulnAreasTwo()
                + bL.numSafeAreas() * FIX3];
        /*
        The areas are concatenated to be filled in the following order:

        1. Protect spots of protective areas -> placing a tile here converts the protective area to a safe area
        2. Vulnerable areas type II
        3. Vulnerable areas type I
        4. The part of protective areas which is not the protect spot
        5. Safe areas: first the option area possibilities and then the normal safe areas
         */
        int index = 0;
        for (ProtectiveArea area : bL.getProtectiveAreas()) {
            outputMoves[index++] = area.getProtectSpot().getCornerUL();
        }
        for (VulnArea area : bL.getVulnAreasTwo()) {
            outputMoves[index++] = area.getCornerUL();
        }
        for (VulnArea area : bL.getVulnAreasOne()) {
            outputMoves[index++] = area.getCornerUL();
        }
        for (ProtectiveArea area : bL.getProtectiveAreas()) {
            VulnArea[] split = area.splitIntoVulnTwo(player);
            // always only add the part of the protective are which is NOT the protect spot
            if (include) {
                outputMoves[index++] = split[0].getCornerUL().equals(area.getProtectSpot().getCornerUL())
                        ? split[1].getCornerUL()
                        : split[0].getCornerUL();
            }
        }
        for (SafeArea area : bL.getSafeAreas()) {
            if (area.getOptionSafeLower() != null) {
                outputMoves[index++] = area.getOptionSafeLower().getCornerUL();
            }
            if (area.getOptionSafeUpper() != null) {
                outputMoves[index++] = area.getOptionSafeUpper().getCornerUL();
            }
            if (include) {
                outputMoves[index++] = area.getCornerUL();
            }
        }
        return Arrays.copyOf(outputMoves, index);
    }

}
    /*public Coordinate alphabeta(int recursivity, Player dir) {
        int i = 0;
        int j = 0;
        //std::cout << "Computer turn using alphabeta..." << std::endl;
        alphabeta(RECURSIVITY, dir, i, j, -N * N, N * N);
        //std::cout << "Placed item on column " << j + 1 << ", row " << i + 1 << std::endl;
        i=RI;
        j=RJ;
        return new Coordinate(i,j);
    }

    public int alphabeta(int recursivity, Player dir, int ri, int rj, int alpha, int beta) {
        if (recursivity == 0)
            return get_possibilities(dir) - ((dir == Player.HORIZONTAL) ? get_possibilities(Player.VERTICAL) : get_possibilities(Player.HORIZONTAL));

        int fi = 0;
        int fj = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (place_item(i, j, dir, true)) {
                    int e = -alphabeta(recursivity - 1, ((dir == Player.HORIZONTAL) ? Player.VERTICAL : Player.HORIZONTAL), fi, fj, -beta, -alpha);
                    remove_item(i, j, dir);
                    if (e > alpha) {
                        alpha = e;
                        RI = i;
                        RJ = j;
                        if (alpha >= beta) {
                            return beta;
                        }
                    }
                }
            }
        }
        return alpha;
    }

    public int get_possibilities(Player dir) {
        int sum = 0;
        int row_m = 0;
        int col_m = 0;
        if (dir == Player.VERTICAL) {
            row_m = 1;
        } else {
            col_m = 1;
        }

        for (int i = 0; i < N - row_m; i++) {
            for (int j = 0; j < N - col_m; j++) {
                if (board[i][j] == 'E' && board[i + row_m][j + col_m] == 'E') {
                    sum += 1;
                }
            }
        }

        return sum;
    }

    public boolean place_item(int row, int col, Player dir, boolean silent) {
        int col_m = 0;
        int row_m = 0;
        if (dir == Player.VERTICAL) {
            row_m = 1;
        } else {
            col_m = 1;
        }

        if (row + row_m >= N || col + col_m >= N || board[row + row_m][col] != 'E' || board[row][col + col_m] != 'E') {
            //if(!silent)
            //std::cout << "Please check your input and try again." << std::endl;
            return false;
        } else {
            board[row][col] = dir == Player.VERTICAL ? 'V' : 'H';
            board[row + row_m][col + col_m] = dir == Player.VERTICAL ? 'V' : 'H';
        }

        return true;
    }

    void remove_item(int row, int col, Player dir) {
        if (dir == Player.VERTICAL) {
            board[row][col] = 'E';
            board[row + 1][col] = 'E';
        } else {
            board[row][col] = 'E';
            board[row][col + 1] = 'E';
        }
    }
    int alphabetakillerhistory(int recursivity, PLayer dir, int ri, int rj, int alpha, int beta){
        if(recursivity == 0)
            return get_possibilities(dir) - ((dir==Player.HORIZONTAL)?get_possibilities(Player.VERTICAL):get_possibilities(Player.HORIZONTAL));


        int fj, fi = 0;
        if(history_moves.size() == 0){
            history_moves.resize(row_count);
            for(int i = 0; i < col_count; i++){
                history_moves[i].resize(col_count);
                history_moves[i].assign(col_count, 0);
            }
        }else{
            std::vector<killer_move> history;
            for(int i = 0; i < row_count; i++){
                for(int j = 0; j < col_count; j++){
                    if(history_moves[i][j] != 0){
                        history.push_back({i, j, dir, history_moves[i][j]});
                    }
                }
            }

            for(unsigned int i = 0; i < history.size(); i++){
                for(unsigned int j = 0; j < history.size(); j++){
                    if(history[i].rate > history[j].rate){
                        killer_move tmp = history[i];
                        history[i] = history[j];
                        history[j] = tmp;
                    }
                }
            }

            for(unsigned int i = 0; i < history.size(); i++){
                if(place_item(history[i].i, history[i].j, dir, true)){
                    int l = -alphabetakiller(recursivity-1, ((dir==HORIZONTAL)?VERTICAL:HORIZONTAL), fi, fj, -beta, -alpha);
                    history_moves[history[i].i][history[i].j] += 4;
                    remove_item(history[i].i, history[i].j, dir);
                    if(l > alpha){
                        alpha = l;
                        ri = history[i].i;
                        rj = history[i].j;
                        history_moves[history[i].i][history[i].j] += pow(4, recursivity);
                        if(alpha >= beta){
                            return beta;
                        }
                    }
                }
            }
            return alpha;
        }

        for(int i = 0; i < row_count; i ++){
            for(int j = 0; j < col_count; j ++){
                if(place_item(i, j, dir, true)){
                    int l = -alphabetakiller(recursivity-1, ((dir==HORIZONTAL)?VERTICAL:HORIZONTAL), fi, fj, -beta, -alpha);
                    history_moves[i][j] += 4;
                    remove_item(i, j, dir);
                    if(l > alpha){
                        alpha = l;
                        ri = i;
                        rj = j;
                        history_moves[i][j] += pow(4, recursivity);
                        if(alpha >= beta){
                            return beta;
                        }
                    }
                }
            }
        }
        return alpha;
    }
}*/
