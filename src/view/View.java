package view;

import model.events.*;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class View implements ModelChangedListener, GameFinishedListener,
        PawnHasComeToEndOfChessBoardListener, NotPossibleMoveListener {
    private MyFrame myFrame;
    public View() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    myFrame = new MyFrame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void modelChangedActions(ModelChangedEvent event) {
        myFrame.repaint();
//        int width = myFrame.getSize().width;
//        int height = myFrame.getSize().height;
//        myFrame.setSize(width + 100, height + 100);
//        myFrame.setSize(width, height);
        myFrame.update(myFrame.getGraphics());
    }

    public MyFrame getMyFrame() {
        return myFrame;
    }

    @Override
    public void gameFinished(GameFinishedEvent event) {
        myFrame.setEnabled(false);
    }


    @Override
    public void changePawnToOtherFigure(PawnHasComeToEndOfChessBoardEvent event, boolean color) {
        try {
            new FrameChangeFigure(color);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionsWhenNotPossibleMove(NotPossibleMoveEvent event) {
        myFrame.deleteRedBackground();
    }
}
