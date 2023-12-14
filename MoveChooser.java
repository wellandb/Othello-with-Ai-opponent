import java.util.ArrayList;  

public class MoveChooser {
    //cost of each square on the board
    static int[][] squareCost = {{120, -20, 20, 5, 5, 20, -20, 120}, {-20, -40, -5, -5, -5, -5, -40, -20}, {20, -5, 15, 3, 3, 15, -5, 20}, {5, -5, 3, 3, 3, 3, -5, 5}, {5, -5, 3, 3, 3, 3, -5, 5}, {20, -5, 15, 3, 3, 15, -5, 20}, {-20, -40, -5, -5, -5, -5, -40, -20}, {120, -20, 20, 5, 5, 20, -20, 120}};

    public static int evalutation(BoardState board){
        // Works out cost of current boardstate using the given squareCost and the colour of the piece on that square
        int boardTotal = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board.getContents(i, j) != 0){
                    boardTotal = boardTotal + board.getContents(i, j) * squareCost[i][j]; // content is -1 for black (player) and 1 for white (computer) so will work out board cost for white
                }
            }
        }
        return boardTotal;
    }

    // Minimax with alpha Beta pruning
    public static int minimax(BoardState Node, int Depth, int a, int B){
        // base case
        if(Depth == 0){
            return evalutation(Node);
        } else if(Node.colour == 1) {
            // White's turn
            ArrayList<Move> nextMoves= Node.getLegalMoves();
            if(nextMoves.isEmpty()){
                return 100000;
            }
            a = -100000;
            for(Move nextMove: nextMoves){
                BoardState nextNode = Node.deepCopy(); // create copy of board to make move on
                nextNode.makeLegalMove(nextMove.x, nextMove.y);
                int tempAlpha = minimax(nextNode, Depth - 1, a, B); // recursive call
                if(tempAlpha>a){
                    a = tempAlpha;
                }
                if(a>=B){
                    return 100000;
                }
            }
            return a;
        }else{
            // Black's turn
            ArrayList<Move> nextMoves= Node.getLegalMoves();
            if(nextMoves.isEmpty()){
                return -100000;
            }
            B = 100000;
            for(Move nextMove: nextMoves){
                BoardState nextNode = Node.deepCopy(); // create copy of board to make move on
                nextNode.makeLegalMove(nextMove.x, nextMove.y);
                int tempBeta = minimax(nextNode, Depth - 1, a, B); // recursive call
                if(tempBeta<B){
                    B = tempBeta;
                }
                if(a>=B){
                    return -100000;
                }
            }
            return B;
        }
    }

    public static Move chooseMove(BoardState boardState){

	int searchDepth= Othello.searchDepth;

        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
	    }
        // select best move
        Move bestMove = moves.get(0);
        int highestTotal = -100000;
        // check each move
        for(Move nextMove: moves){
            BoardState boardState1 = boardState.deepCopy();
            boardState1.makeLegalMove(nextMove.x,nextMove.y);
            int nextTotal = minimax(boardState1, searchDepth, -10000, 10000); // returns value of boardstate and future boardstates for that move
            // checks if the move is the current best move
            if(nextTotal > highestTotal){
                highestTotal = nextTotal;
                bestMove = nextMove; 
            }
        }
        return bestMove;
    }
}
