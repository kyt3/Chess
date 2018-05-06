package view;

import model.abstract_.Figure;
import model.real.Cell;
import model.real.ChessBoard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class ChessBoardView extends JPanel {
    private CellView[][] cellViews;

    public ChessBoardView() throws IOException {
        cellViews = new CellView[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        Cell[][] cells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[j][i].getFigure() != null) {
                    cellViews[j][i] = new CellView(cells[j][i].isColor(),
                            new FigureView(cells[j][i].getFigure()));
                } else cellViews[j][i] = new CellView(cells[j][i].isColor());
            }

        }

        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                add(cellViews[j][i]);
            }
        }
    }

    public void moveFigures() throws IOException {
        //переопределение доски
        Cell[][] chessBoardCells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell chessBoardCell = chessBoardCells[j][i];
                CellView cellView = cellViews[j][i];
                cellView.setFigureView(null);
                cellView.removeAll();
                cellView.setBackground();
                Figure figure = chessBoardCell.getFigure();
                if (figure != null) {
                    cellView.setFigureView(new FigureView(figure));
                }


            }
        }

        revalidate();
        repaint();
    }


    public void deleteRedBackground() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellView cellView = cellViews[j][i];

                if (cellView.getBackground() == Color.green) {
                    cellView.setDefaultBackground();
                }
            }
        }
    }
}
