package view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class MyFrame extends JFrame {
    private ChessBoardView chessBoardView;

    public MyFrame() throws HeadlessException, IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessBoardView = new ChessBoardView();
        setContentPane(chessBoardView);

        setSize(500, 500);
        setVisible(true);
    }

//    @Override
//    public void repaint() {
//        try {
//            chessBoardView = new ChessBoardView();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        setContentPane(chessBoardView);
//    }


    @Override
    public void repaint() {
        try {
            chessBoardView.moveFigures();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.repaint();
    }

    public void deleteRedBackground() {
        chessBoardView.deleteRedBackground();
    }
}
