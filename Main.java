import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        VirusGame game = new VirusGame(5);
        game.virusSingleGame(new OtherStrategy(), new RandomVirusStrategy());
    }
}