package controller;

import model.Model;
import model.real.Cell;
import model.real.ChessBoard;
import view.View;
import view.CellView;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by kot on 01.04.18.
 */
public class Controller implements MouseListener {
    private Cell startCell = null;
    private Cell endCell = null;
    private final Model model;
    private final View view;
    private static Controller instance;

    private Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public static Controller getInstance() {
        return instance;
    }

    public static Controller getInstance(Model model, View view) {
        if (instance == null) {
            instance = new Controller(model, view);
        }
        return instance;
    }

    private Cell getCell(int x, int y) {
        Cell[][] cells = ChessBoard.getInstance().getCells();

        return cells[x][y];
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof CellView) {
            JComponent component = (JComponent) mouseEvent.getComponent();
            if (startCell == null) {
                startCell = getCell(getIndexFromCoordinatesX(component.getX()),
                        getIndexFromCoordinatesY(component.getY()));
            } else {
                endCell = getCell(getIndexFromCoordinatesX(component.getX()),
                        getIndexFromCoordinatesY(component.getY()));
                model.doMove(startCell, endCell);
                startCell = null;
                endCell = null;
            }
        } else {
            int x = mouseEvent.getX();
            int width = mouseEvent.getComponent().getSize().width;
            String nameFigure;
            if (x < width / 4) {
                nameFigure = "Rook";
            } else if (x < width / 2) {
                nameFigure = "Knight";
            } else if (x < 3 * width / 4) {
                nameFigure = "Bishop";
            } else {
                nameFigure = "Queen";
            }

            model.changePawnToFigure(nameFigure);
        }
    }

    private int getIndexFromCoordinatesX(int number) {
        int width = view.getMyFrame().getSize().width;
        int howMuchPixelsOnCell = width / 8;
        return number / howMuchPixelsOnCell;
    }

    private int getIndexFromCoordinatesY(int number) {
        int height = view.getMyFrame().getSize().height;
        int howMuchPixelsOnCell = height / 8;
        int reverseValue = number / howMuchPixelsOnCell;
        switch (reverseValue) {
            case 0:
                return 7;
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 2;
            case 6:
                return 1;
            case 7:
                return 0;

        }
        return -1;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
