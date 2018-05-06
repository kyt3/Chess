package view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class MyFrame extends JFrame {
    private ChessBoardView chessBoardView;

    MyFrame() throws HeadlessException, IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessBoardView = new ChessBoardView();
        setContentPane(chessBoardView);

        setSize(500, 500);
        setVisible(true);
    }



    @Override
    public void repaint() {
        try {
            chessBoardView.moveFigures();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.repaint();
    }

    void deleteBackground() {
        chessBoardView.deleteBackground();
    }
}
