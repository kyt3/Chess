package game;


import controller.Controller;
import model.Model;
import view.View;

import java.io.IOException;

/**
 * Created by kot on 01.04.18.
 */

public class MainGame {

    public static void main(String[] args) throws IOException {
        Model model = new Model();
        View view = new View();
        Controller controller = Controller.getInstance(model, view);
        model.addModelChangedListener(view);
        model.addGameFinishedListener(view);
        model.addPawnHasComeToEndOfChessBoardListener(view);
        model.addNotPossibleMoveListeners(view);
    }
}
