package view;


import controller.GameController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class represents the checkerboard component object on the panel
 */
public class ChessboardComponent extends JComponent {
    private final CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];
    private final int CHESS_SIZE;
    private final Set<ChessboardPoint> riverCell = new HashSet<>();
    private final Set<ChessboardPoint> RedTrapCell = new HashSet<>();
    private final Set<ChessboardPoint> BlueTrapCell = new HashSet<>();
    private final Set<ChessboardPoint> BlueDenCell = new HashSet<>();
    private final Set<ChessboardPoint> RedDenCell = new HashSet<>();
    private PlayerColor currentPlayer;

    public Set<ChessboardPoint> getRiverCell(){
        return riverCell;
    }

    public Set<ChessboardPoint> getBlueTrapCell(){
        return BlueTrapCell;
    }

    public  Set<ChessboardPoint> getRedTrapCell() {
        return RedTrapCell;
    }

    public Set<ChessboardPoint> getBlueDenCell(){
        return BlueDenCell;
    }

    public Set<ChessboardPoint> getRedDenCell(){
        return RedDenCell;
    }

    private GameController gameController;

    public ChessboardComponent(int chessSize) {
        CHESS_SIZE = chessSize;
        int width = CHESS_SIZE * 7;
        int height = CHESS_SIZE * 9;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);// Allow mouse events to occur
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        System.out.printf("chessboard width, height = [%d : %d], chess size = %d\n", width, height, CHESS_SIZE);
        currentPlayer = PlayerColor.BLUE;
        initiateGridComponents();
    }

    /**
     * This method represents how to initiate ChessComponent
     * according to Chessboard information
     */
    public void initiateChessComponent(Chessboard chessboard) {//初始化棋子的位置？
        Cell[][] grid = chessboard.getGrid();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                // TODO: Implement the initialization checkerboard//?
                if (grid[i][j].getPiece() != null) {
                    ChessPiece chessPiece = grid[i][j].getPiece();
                    switch (grid[i][j].getPiece().getRank()){
                        case 8:
                            gridComponents[i][j].add(
                                    new ElephantChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 1:
                            gridComponents[i][j].add(
                                    new RatChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 2:
                            gridComponents[i][j].add(
                                    new CatChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 3:
                            gridComponents[i][j].add(
                                    new DogChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 4:
                            gridComponents[i][j].add(
                                    new WolfChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 5:
                            gridComponents[i][j].add(
                                    new LeopardChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 6:
                            gridComponents[i][j].add(
                                    new TigerChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                        case 7:
                            gridComponents[i][j].add(
                                    new LionChessComponent(
                                            chessPiece.getOwner(),
                                            CHESS_SIZE));break;
                    }
                }
            }
        }

    }



    public void initiateGridComponents() {//棋盘形状颜色

        riverCell.add(new ChessboardPoint(3,1));
        riverCell.add(new ChessboardPoint(3,2));
        riverCell.add(new ChessboardPoint(4,1));
        riverCell.add(new ChessboardPoint(4,2));
        riverCell.add(new ChessboardPoint(5,1));
        riverCell.add(new ChessboardPoint(5,2));

        riverCell.add(new ChessboardPoint(3,4));
        riverCell.add(new ChessboardPoint(3,5));
        riverCell.add(new ChessboardPoint(4,4));
        riverCell.add(new ChessboardPoint(4,5));
        riverCell.add(new ChessboardPoint(5,4));
        riverCell.add(new ChessboardPoint(5,5));

        RedTrapCell.add(new ChessboardPoint(0,2));
        RedTrapCell.add(new ChessboardPoint(1,3));
        RedTrapCell.add(new ChessboardPoint(0,4));

        BlueTrapCell.add(new ChessboardPoint(8,2));
        BlueTrapCell.add(new ChessboardPoint(7,3));
        BlueTrapCell.add(new ChessboardPoint(8,4));

        BlueDenCell.add(new ChessboardPoint(8,3));
        RedDenCell.add(new ChessboardPoint(0,3));
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint temp = new ChessboardPoint(i, j);
                CellComponent cell;
                if (riverCell.contains(temp)) {
                    cell = new CellComponent(Color.CYAN, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cell);
                } else if(BlueTrapCell.contains(temp)){
                    cell = new CellComponent(Color.orange, calculatePoint(i,j), CHESS_SIZE);
                    this.add(cell);
                }else if(RedTrapCell.contains(temp)){
                    cell = new CellComponent(Color.orange, calculatePoint(i,j), CHESS_SIZE);
                    this.add(cell);
                }else if(RedDenCell.contains(temp)){
                    cell = new CellComponent(Color.pink, calculatePoint(i,j), CHESS_SIZE);
                    this.add(cell);
                }else if(BlueDenCell.contains(temp)){
                    cell = new CellComponent(Color.GREEN, calculatePoint(i,j), CHESS_SIZE);
                    this.add(cell);
                }else{
                    cell = new CellComponent(Color.LIGHT_GRAY, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cell);
                }
                gridComponents[i][j] = cell;
            }
        }
    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setChessComponentAtGrid(ChessboardPoint point, ElephantChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, LionChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, TigerChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point,LeopardChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, WolfChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, DogChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, CatChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point, RatChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }
    public void setChessComponentAtGrid(ChessboardPoint point) {
        getGridComponentAt(point).removeAll();
    }

    public  void resetAllChessComponent(Chessboard chessboard){
        chessboard.resetPieces();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i,j);
                setChessComponentAtGrid(point);
                getGridComponentAt(point).revalidate();
            }
        }
        initiateChessComponent(chessboard);
        GameController.resetCurrentPlayer();
        GameController.resetFile();
        repaint();
    }

    public void replayAllChessComponent(Chessboard chessboard, ArrayList<String> gameSteps){
        chessboard.resetPieces();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i,j);
                setChessComponentAtGrid(point);
                getGridComponentAt(point).revalidate();
            }
        }
        initiateChessComponent(chessboard);
        GameController.resetCurrentPlayer();
        for (int i = 0; i < Count.getCOUNT(); i++) {
            String aim = gameSteps.get(i);
            String[] aims = aim.split(",");
            if (aims[1].equals("noChess")) {
                ReplayMove(aims, chessboard);
            } else {
                ReplayCapture(aims, chessboard);
            }
        }
        if(Count.getCOUNT()%2==0){
            GameController.resetCurrentPlayer();
        }else{
            GameController.resetCurrentPlayer();
            GameController.swapColor();
        }
    }

    public JComponent removeChessComponentAtGrid(ChessboardPoint point) {
        // Note re-validation is required after remove / removeAll.
        if(getGridComponentAt(point).getComponents()[0] instanceof ElephantChessComponent){
            ElephantChessComponent chess = (ElephantChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof LionChessComponent) {
            LionChessComponent chess = (LionChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof TigerChessComponent){
            TigerChessComponent chess = (TigerChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof LeopardChessComponent){
            LeopardChessComponent chess = (LeopardChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof WolfChessComponent){
            WolfChessComponent chess = (WolfChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof DogChessComponent){
            DogChessComponent chess = (DogChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof CatChessComponent){
            CatChessComponent chess = (CatChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        }else if(getGridComponentAt(point).getComponents()[0] instanceof RatChessComponent){
            RatChessComponent chess = (RatChessComponent) getGridComponentAt(point).getComponents()[0];
            getGridComponentAt(point).removeAll();
            getGridComponentAt(point).revalidate();
            chess.setSelected(false);
            return chess;
        } else{return null;}
    }

    public CellComponent  getGridComponentAt(ChessboardPoint point) {
        return gridComponents[point.getRow()][point.getCol()];
    }

    private ChessboardPoint getChessboardPoint(Point point) {
        System.out.println("[" + point.y/CHESS_SIZE +  ", " +point.x/CHESS_SIZE + "] Clicked");
        return new ChessboardPoint(point.y/CHESS_SIZE, point.x/CHESS_SIZE);
    }

    public void ReplayCapture(String[] aims,Chessboard chessboard){
        String[] chessInfor1 = aims[0].split(" ");//发出者
        String[] chessInfor2 = aims[0].split(" ");
        String[] before = aims[2].split(" ");
        String[] after  = aims[3].split(" ");

        for (int i = 0; i < chessboard.getGrid().length; i++) {
            for (int j = 0; j < chessboard.getGrid()[0].length; j++) {
                if(chessboard.getGrid()[i][j].getPiece()!=null){
                    if(chessboard.getGrid()[i][j].getPiece().getOwnerString().equals(chessInfor1[0])
                            && chessboard.getGrid()[i][j].getPiece().getName().equals(chessInfor1[1])){
                        ChessboardPoint bef = new ChessboardPoint(Integer.valueOf(before[0]),Integer.valueOf(before[1]));
                        ChessboardPoint aft = new ChessboardPoint(Integer.valueOf(after[0]),Integer.valueOf(after[1]));
                        try {
                            GameController.oos.writeObject(chessboard.getGrid()[bef.getRow()][bef.getCol()].getPiece().toString()+","+
                                    chessboard.getGrid()[aft.getRow()][aft.getCol()].getPiece().toString()+","+bef+","+aft);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        chessboard.captureChessPiece(bef,aft);
                        removeChessComponentAtGrid(aft);
                        if(getGridComponentAt(bef).getComponents()[0] instanceof ElephantChessComponent){
                            setChessComponentAtGrid(aft, (ElephantChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof LionChessComponent){
                            setChessComponentAtGrid(aft, (LionChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof TigerChessComponent){
                            setChessComponentAtGrid(aft, (TigerChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof LeopardChessComponent){
                            setChessComponentAtGrid(aft, (LeopardChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof WolfChessComponent){
                            setChessComponentAtGrid(aft, (WolfChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof DogChessComponent){
                            setChessComponentAtGrid(aft, (DogChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof CatChessComponent){
                            setChessComponentAtGrid(aft, (CatChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof RatChessComponent){
                            setChessComponentAtGrid(aft, (RatChessComponent) removeChessComponentAtGrid(bef));
                        }
                        repaint();
                        GameController.swapColor();return;
                    }
                }
            }
        }
    }

    public void withdrawCapture (String[] aims,Chessboard chessboard){
        String[] chessInfor1 = aims[0].split(" ");//发出者
        String[] chessInfor2 = aims[1].split(" ");
        ChessPiece eaten = null;
        if(chessInfor2[0].equals("BLUE")){
            eaten = new ChessPiece(PlayerColor.BLUE,chessInfor2[1],Integer.valueOf(chessInfor2[2]));
        }else if(chessInfor2[0].equals("RED")){
            eaten = new ChessPiece(PlayerColor.RED,chessInfor2[1],Integer.valueOf(chessInfor2[2]));
        }
        String[] before = aims[2].split(" ");
        String[] after  = aims[3].split(" ");

        for (int i = 0; i < chessboard.getGrid().length; i++) {
            for (int j = 0; j < chessboard.getGrid()[0].length; j++) {
                if(chessboard.getGrid()[i][j].getPiece()!=null){
                    if(chessboard.getGrid()[i][j].getPiece().getOwnerString().equals(chessInfor1[0])
                            && chessboard.getGrid()[i][j].getPiece().getName().equals(chessInfor1[1])){
                        ChessboardPoint bef = new ChessboardPoint(Integer.valueOf(before[0]),Integer.valueOf(before[1]));
                        ChessboardPoint aft = new ChessboardPoint(Integer.valueOf(after[0]),Integer.valueOf(after[1]));
                        chessboard.moveChessPiece(aft,bef);
                        chessboard.setChessPiece(aft,eaten);
                        //chessboard.captureChessPiece(aft,bef);
                        if(getGridComponentAt(aft).getComponents()[0] instanceof ElephantChessComponent){
                            setChessComponentAtGrid(bef, (ElephantChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof LionChessComponent){
                            setChessComponentAtGrid(bef, (LionChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof TigerChessComponent){
                            setChessComponentAtGrid(bef, (TigerChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof LeopardChessComponent){
                            setChessComponentAtGrid(bef, (LeopardChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof WolfChessComponent){
                            setChessComponentAtGrid(bef, (WolfChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof DogChessComponent){
                            setChessComponentAtGrid(bef, (DogChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof CatChessComponent){
                            setChessComponentAtGrid(bef, (CatChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof RatChessComponent){
                            setChessComponentAtGrid(bef, (RatChessComponent) removeChessComponentAtGrid(aft));
                        }
                        switch (chessInfor2[1]){
                            case "Elephant":
                                ElephantChessComponent eatenElephant = new ElephantChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenElephant);break;
                            case "Lion":
                                LionChessComponent eatenLion = new LionChessComponent(eaten.getOwner(),eaten.getRank());
                            setChessComponentAtGrid(aft,eatenLion);break;
                            case "Tiger":
                                TigerChessComponent eatenTiger = new TigerChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenTiger);break;
                            case "Leopard":
                                LeopardChessComponent eatenLeopard = new LeopardChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenLeopard);break;
                            case "Wolf":
                                WolfChessComponent eatenWolf = new WolfChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenWolf);break;
                            case "Dog":
                                DogChessComponent eatenDog = new DogChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenDog);break;
                            case "Cat":
                                CatChessComponent eatenCat = new CatChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenCat);break;
                            case "Rat":
                                RatChessComponent eatenRat = new RatChessComponent(eaten.getOwner(),eaten.getRank());
                                setChessComponentAtGrid(aft,eatenRat);break;
                        }
                        repaint();
                        GameController.swapColor();return;
                    }
                }
            }
        }
    }

    public void withdrawMove (String[] aims,Chessboard chessboard){
        String[] chessInfor = aims[0].split(" ");
        String[] before = aims[2].split(" ");
        String[] after  = aims[3].split(" ");
        for (int i = 0; i < chessboard.getGrid().length; i++) {
            for (int j = 0; j < chessboard.getGrid()[0].length; j++) {
                if(chessboard.getGrid()[i][j].getPiece()!=null){
                    if(chessboard.getGrid()[i][j].getPiece().getOwnerString().equals(chessInfor[0])
                            && chessboard.getGrid()[i][j].getPiece().getName().equals(chessInfor[1])){
                        ChessboardPoint bef = new ChessboardPoint(Integer.valueOf(before[0]),Integer.valueOf(before[1]));
                        ChessboardPoint aft = new ChessboardPoint(Integer.valueOf(after[0]),Integer.valueOf(after[1]));
                        chessboard.moveChessPiece(aft,bef);
                        if(getGridComponentAt(aft).getComponents()[0] instanceof ElephantChessComponent){
                            setChessComponentAtGrid(bef, (ElephantChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof LionChessComponent){
                            setChessComponentAtGrid(bef, (LionChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof TigerChessComponent){
                            setChessComponentAtGrid(bef, (TigerChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof LeopardChessComponent){
                            setChessComponentAtGrid(bef, (LeopardChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof WolfChessComponent){
                            setChessComponentAtGrid(bef, (WolfChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof DogChessComponent){
                            setChessComponentAtGrid(bef, (DogChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof CatChessComponent){
                            setChessComponentAtGrid(bef, (CatChessComponent) removeChessComponentAtGrid(aft));
                        }else if(getGridComponentAt(aft).getComponents()[0] instanceof RatChessComponent) {
                            setChessComponentAtGrid(bef, (RatChessComponent) removeChessComponentAtGrid(aft));
                        }
                        repaint();
                        GameController.deleteLastStep();
                        GameController.swapColor();return;
                    }
                }
            }
        }
    }

    public void ReplayMove (String[] aims,Chessboard chessboard){
        String[] chessInfor = aims[0].split(" ");
        String[] before = aims[2].split(" ");
        String[] after  = aims[3].split(" ");
        for (int i = 0; i < chessboard.getGrid().length; i++) {
            for (int j = 0; j < chessboard.getGrid()[0].length; j++) {
                if(chessboard.getGrid()[i][j].getPiece()!=null){
                    if(chessboard.getGrid()[i][j].getPiece().getOwnerString().equals(chessInfor[0])
                            && chessboard.getGrid()[i][j].getPiece().getName().equals(chessInfor[1])){
                        ChessboardPoint bef = new ChessboardPoint(Integer.valueOf(before[0]),Integer.valueOf(before[1]));
                        ChessboardPoint aft = new ChessboardPoint(Integer.valueOf(after[0]),Integer.valueOf(after[1]));
                        try {
                            GameController.oos.writeObject(chessboard.getGrid()[bef.getRow()][bef.getCol()].getPiece().toString()+","+"noChess"+","+bef+","+aft);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        chessboard.moveChessPiece(bef,aft);
                        if(getGridComponentAt(bef).getComponents()[0] instanceof ElephantChessComponent){
                           setChessComponentAtGrid(aft, (ElephantChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof LionChessComponent){
                            setChessComponentAtGrid(aft, (LionChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof TigerChessComponent){
                            setChessComponentAtGrid(aft, (TigerChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof LeopardChessComponent){
                            setChessComponentAtGrid(aft, (LeopardChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof WolfChessComponent){
                            setChessComponentAtGrid(aft, (WolfChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof DogChessComponent){
                            setChessComponentAtGrid(aft, (DogChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof CatChessComponent){
                            setChessComponentAtGrid(aft, (CatChessComponent) removeChessComponentAtGrid(bef));
                        }else if(getGridComponentAt(bef).getComponents()[0] instanceof RatChessComponent) {
                            setChessComponentAtGrid(aft, (RatChessComponent) removeChessComponentAtGrid(bef));
                        }
                        repaint();
                        GameController.swapColor();
                        return;
                    }
                }
            }
        }
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            if (clickedComponent.getComponentCount() == 0) {
                System.out.print("None chess here and ");
                gameController.onPlayerClickCell(getChessboardPoint(e.getPoint()), (CellComponent) clickedComponent);
            } else {
                System.out.print("One chess here and ");
                if (clickedComponent.getComponents()[0] instanceof ElephantChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (ElephantChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof LionChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (LionChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof TigerChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (TigerChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof LeopardChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (LeopardChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof WolfChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (WolfChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof DogChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (DogChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof CatChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (CatChessComponent) clickedComponent.getComponents()[0]);
                }else if(clickedComponent.getComponents()[0] instanceof RatChessComponent){
                    gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (RatChessComponent) clickedComponent.getComponents()[0]);
                }//TODO:所有种类,已写完
            }
        }
    }
}
