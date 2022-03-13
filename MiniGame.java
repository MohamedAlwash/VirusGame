import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MiniGame {

    private final Player[][] miniField;
    private final int fieldSize;
    private Player currentPlayer;
    private boolean firstTime = true;
    private VirusMove ownList;

    private enum Msg {
        FIELD,
        CANTMOVE,
        WINNER,
        MOVE
    }

    public MiniGame(int fieldSize, Player[][] test, VirusMove movesList){
        this.fieldSize = fieldSize;
        this.miniField = test;
        this.ownList = movesList;
    }

    private void initField(){
        for (int i = 0; i < fieldSize; i++){
            for (int j = 0; j < fieldSize; j++){
                miniField[i][j] = Player.EMPTY;
                if (i <= 1){
                    if (j >= fieldSize -2){
                        miniField[i][j] = Player.GREEN;
                    }
                }
                else if (i >= fieldSize - 2){
                    if (j <= 1){
                        miniField[i][j] = Player.RED;
                    }
                }
            }
        }
    }

    private Player getWinner(){
        int countRed = 0;
        int countGreen = 0;
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (miniField[i][j] == Player.RED){
                    countRed++;
                }
                else if (miniField[i][j] == Player.GREEN){
                    countGreen++;
                }
            }
        }
        return countRed > countGreen?Player.RED:Player.GREEN;
    }

    private boolean checkDone(){
        int countRed = 0;
        int countGreen = 0;
        boolean emptyFound = false;
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (miniField[i][j] == Player.EMPTY){
                    emptyFound = true;
                }
                else if (miniField[i][j] == Player.RED){
                    countRed++;
                }
                else{
                    countGreen++;
                }
            }
        }

        return countRed == 0 || countGreen == 0 || !emptyFound;

    }

    private void switchPlayer(){
        if (currentPlayer == Player.RED){
            currentPlayer = Player.GREEN;
        }
        else{
            currentPlayer = Player.RED;
        }
    }

    private boolean canMove(ArrayList<VirusMove> moveList){
        int numberOfMoves = 0;
        Point from;
        Point to;
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (miniField[i][j] == currentPlayer){
                    from = new Point(i, j);
                    for (int x = i - 2; x <= i + 2;x++){
                        for (int y = j - 2; y <= j + 2; y++){
                            if (y >= 0 && x >= 0 && y < fieldSize && x < fieldSize && miniField[x][y] == Player.EMPTY){
                                to = new Point(x, y);
                                moveList.add(new VirusMove(from,to));
                                numberOfMoves++;
                            }
                        }
                    }
                }
            }
        }
        return numberOfMoves > 0;
    }

    private void doMove(VirusMove move){
        int fromX = (int) move.from.getX();
        int fromY = (int) move.from.getY();
        int toX = (int) move.to.getX();
        int toY = (int) move.to.getY();
        if (Math.abs(fromX-toX) > 1 || Math.abs(fromY - toY) > 1){
            miniField[fromX][fromY] = Player.EMPTY;
        }
        miniField[toX][toY] = currentPlayer;
        //take enemy viruses
        for (int x = toX - 1; x <= toX + 1; x++){
            for (int y = toY - 1; y <= toY + 1; y++){
                if (y >= 0 && x >= 0 && y < fieldSize && x < fieldSize && miniField[x][y] != Player.EMPTY){
                    miniField[x][y] = currentPlayer;
                }
            }
        }
    }

    private Player game(HashMap<Player, VirusStrategy> strategy){
        initField();
        currentPlayer = Player.GREEN;
//        printMsg(Msg.FIELD);
        while (!checkDone()){
            ArrayList<VirusMove> moveList = new ArrayList<>();
            if (canMove(moveList)) {
                VirusMove move = setMove(moveList);
                doMove(move);
//                printMsg(Msg.MOVE,currentPlayer,move);
//                printMsg(Msg.FIELD);
            }
//            else{
//                printMsg(Msg.CANTMOVE,currentPlayer);
//            }
            switchPlayer();
        }
        Player winner = getWinner();
//        printMsg(Msg.WINNER, winner, strategy.get(winner));
        return winner;
    }

    public VirusMove setMove(ArrayList<VirusMove> moveList) {
        if (firstTime) {
            firstTime = false;
            return this.ownList;
        }
        Random rand = new Random();
        int chosenMove = rand.nextInt(moveList.size());
        return moveList.get(chosenMove);
    }

    public Player virusSingleGame(){
        HashMap<Player,VirusStrategy> strategy= new HashMap<>();
//        strategy.put(Player.RED, redPlayer);
//        strategy.put(Player.GREEN, greenPlayer);
        return game(strategy);
    }
}
