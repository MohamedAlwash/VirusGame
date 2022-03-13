import java.awt.*;
import java.util.ArrayList;

public class OtherStrategy implements VirusStrategy{

    @Override
    public VirusMove doMove(Player currentPlayer, Player[][] playingField, ArrayList<VirusMove> moveList, int FieldSize) {

        int won = 0;
        int highestWinsOfMoveList = 0;
        int position = 0;
        int key = -1;


        for(VirusMove options : moveList) {
            key++;
            for(int i = 0; i < 1000; i++) {
                Player [][] ownField = new Player[playingField.length][];
                for(int k = 0; k < playingField.length; k++)
                    ownField[k] = playingField[k].clone();

                MiniGame miniGame = new MiniGame(FieldSize, ownField, options);
                Player test = miniGame.virusSingleGame();
                if(test == Player.GREEN) {
                    won++;
                }
            }
            if(won > highestWinsOfMoveList) {
                highestWinsOfMoveList = won;
                position = key;
            }
            won = 0;
        }

        System.out.println("klaar");
        return moveList.get(position);
    }

    @Override
    public String getName() {
        return null;
    }
}