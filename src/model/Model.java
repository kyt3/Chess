package model;

import model.events.*;
import model.exceptions.ImpossibleMove;
import model.abstract_.Figure;
import model.real.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kot on 01.04.18.
 */
public class Model {
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean whoIsMove;
    private ArrayList<ModelChangedListener> modelChangedListeners = new ArrayList<>();
    private ArrayList<GameFinishedListener> gameFinishedListeners = new ArrayList<>();
    private ArrayList<PawnHasComeToEndOfChessBoardListener> pawnHasComeToEndOfChessBoardListeners = new ArrayList<>();
    private ArrayList<NotPossibleMoveListener> notPossibleMoveListeners = new ArrayList<>();

    public Model() {
        createChessBoard();

        whoIsMove = false;
        whitePlayer = new Player(false);
        blackPlayer = new Player(true);
    }

    public void doMove(Cell startCell, Cell endCell) {
        try {

            if (!whoIsMove) {
                gameMove(whitePlayer, startCell, endCell);

                if (Logic.isMate(blackPlayer)) {
                    generateGameFinishedEvent();
                }
            } else {
                gameMove(blackPlayer, startCell, endCell);

                if (Logic.isMate(whitePlayer)) {
                    generateGameFinishedEvent();
                }
            }

            if (Logic.existPawnInTheEndOfChessBoard(whoIsMove)) {
                generatePawnHasComeToEndOfChessBoardEvent(whoIsMove);
            }

            if (!whoIsMove) {
                whoIsMove = true;
            } else {
                whoIsMove = false;
            }

            generateModelChangedEvent();

        } catch (ImpossibleMove impossibleMove) {
            generateNotPossibleMoveEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePawnToFigure(String nameFigure) {
        Figure figure = getFigureFromString(nameFigure);
        Cell[][] cells = ChessBoard.getInstance().getCells();
        if (whoIsMove) {
            for (int i = 0; i < 8; i++) {
                if (cells[i][7].getFigure() instanceof Pawn) {
                    cells[i][7].setFigure(figure);
                    generateModelChangedEvent();
                    return;
                }
            }
        } else {

            for (int i = 0; i < 8; i++) {
                if (cells[i][0].getFigure() instanceof Pawn) {
                    cells[i][0].setFigure(figure);
                    generateModelChangedEvent();
                    return;
                }
            }
        }
    }

    private Figure getFigureFromString(String nameFigure) {
        switch (nameFigure) {
            case "Rook":
                return new Rook(!whoIsMove);
            case "Knight":
                return new Knight(!whoIsMove);
            case "Bishop":
                return new Bishop(!whoIsMove);
            case "Queen":
                return new Queen(!whoIsMove);
        }

        return null;
    }

    private void gameMove(Player player, Cell startCell, Cell endCell) throws ImpossibleMove, IOException {

        //если возможен ход
        //сделать его
        if (Logic.isPossibleMove(player, startCell, endCell)) {
            player.move(startCell, endCell);
            return;

        }

        if (startCell.getFigure() != null) {
            if (startCell.getFigure() instanceof King) {
                if (Logic.isPossibleCasterling(player, startCell.getFigure(), endCell)) {
                    player.casterling(startCell, endCell);
                    return;
                }
            }

            if (startCell.getFigure() instanceof Pawn) {
                if (Logic.isPossibleTakeOnTheAisle(player, startCell, endCell)) {
                    player.takeOnTheAisle(startCell, endCell);
                    return;
                }
            }
        }

        throw new ImpossibleMove();
        //в ином случае
        //исключение ход невозможен
    }

//    private Cell getCell() throws IllegalEnter, ImpossibleCell, IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        char[] charCell = bufferedReader.readLine().toCharArray();
//
//        if (charCell.length != 2) {
//            throw new IllegalEnter();
//        }
//
//        int x = parseCharToInt(charCell[0]);
//        int y = Integer.parseInt(String.valueOf(charCell[1])) - 1;
//
//        if (y > 7 || x > 7 || x < 0 || y < 0) {
//            throw new ImpossibleCell();
//        }
//
//        ChessBoard chessBoard = ChessBoard.getInstance();
//
//        for (Cell cell : chessBoard.getCells()) {
//            if (cell.getX() == x && cell.getY() == y) {
//                return cell;
//            }
//        }
//
//        return null;
//    }

    public void addModelChangedListener(ModelChangedListener listener) {
        modelChangedListeners.add(listener);
    }

    public void addGameFinishedListener(GameFinishedListener listener) {
        gameFinishedListeners.add(listener);
    }

    public void addPawnHasComeToEndOfChessBoardListener(PawnHasComeToEndOfChessBoardListener listener) {
        pawnHasComeToEndOfChessBoardListeners.add(listener);
    }

    public void addNotPossibleMoveListeners(NotPossibleMoveListener listener) {
        notPossibleMoveListeners.add(listener);
    }

    private void generateGameFinishedEvent() {
        GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(this);
        for (GameFinishedListener gameFinishedListener : gameFinishedListeners) {
            gameFinishedListener.gameFinished(gameFinishedEvent);
        }
    }

    private void generateModelChangedEvent() {
        ModelChangedEvent event = new ModelChangedEvent(this);
        for (ModelChangedListener listener : modelChangedListeners) {
            listener.modelChangedActions(event);
        }
    }

    private void generateNotPossibleMoveEvent() {
        NotPossibleMoveEvent event = new NotPossibleMoveEvent(this);
        for (NotPossibleMoveListener listener : notPossibleMoveListeners) {
            listener.actionsWhenNotPossibleMove(event);
        }
    }

    private void generatePawnHasComeToEndOfChessBoardEvent(boolean color) {
        for (PawnHasComeToEndOfChessBoardListener listener : pawnHasComeToEndOfChessBoardListeners) {
            listener.changePawnToOtherFigure(new PawnHasComeToEndOfChessBoardEvent(this), color);
        }
    }

    private int parseCharToInt(char ch) {
        switch (ch) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            default:
                return 9;
        }
    }

    private void createChessBoard() {
        Cell[][] cells = createCellsWithFigures();
        ChessBoard chessBoard = ChessBoard.getInstance(cells);
    }

    private Cell[][] createCellsWithFigures() {
        Cell[][] cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            boolean color;
            if (i % 2 == 0) {
                color = true;
            } else color = false;

            for (int j = 0; j < 8; j++) {
                switch (i) {
                    case 0:
                        cells[j][i] = new Cell(j, i, getFigureInPosition(j, false), color);
                        break;
                    case 1:
                        cells[j][i] = new Cell(j, i, new Pawn(false), color);
                        break;
                    case 6:
                        cells[j][i] = new Cell(j, i, new Pawn(true), color);
                        break;
                    case 7:
                        cells[j][i] = new Cell(j, i, getFigureInPosition(j, true), color);
                        break;
                    default:
                        cells[j][i] = new Cell(j, i, color);
                        break;
                }

                if (color) {
                    color = false;
                } else color = true;
            }
        }

        return cells;
    }

    private Figure getFigureInPosition(int position, boolean color) {
        switch (position) {
            case 0:
                return new Rook(color);
            case 1:
                return new Knight(color);
            case 2:
                return new Bishop(color);
            case 3:
                return new Queen(color);
            case 4:
                return new King(color);
            case 5:
                return new Bishop(color);
            case 6:
                return new Knight(color);
            case 7:
                return new Rook(color);
        }

        return null;
    }
}
