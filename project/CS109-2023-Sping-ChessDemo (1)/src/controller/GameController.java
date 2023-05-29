package controller;



import AI.AIplayer;
import listener.GameListener;
import model.*;
import view.*;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {
    private AIplayer aIplayer;
    private boolean AIexist = false;
    private static boolean AIexist1 = false;
    public void AIexists(){
        AIexist = true;
    }
    public void AIexist1(){
        AIexist1 = true;
    }
    public static void changeAI(){
        if(AIexist1 == true){
            AIexist1 = false;
        }else {
            AIexist1 = true;
        }
    }
    public static ObjectOutputStream oos;

    {
        try {
            oos = new ObjectOutputStream(new FileOutputStream("gameStep.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObjectInputStream ois;
    {try {ois = new ObjectInputStream(new FileInputStream("gameStep.txt"));} catch (IOException e) {e.printStackTrace();}}

    public static void resetFile(){
        {try {oos = new ObjectOutputStream(new FileOutputStream("gameStep.txt"));} catch (IOException e) {e.printStackTrace();}}
    }

    public static void deleteLastStep(){
        try {
            ArrayList<String> using = new ArrayList<>();
            for (int i = 0; i < Count.getCOUNT(); i++) {
                using.add((String) ois.readObject());
            }
            resetFile();
            for (int i = 0; i < Count.getCOUNT()-1; i++) {
                oos.writeObject(using.get(i));
            }
        } catch (IOException e) {e.printStackTrace();} catch (ClassNotFoundException e) {e.printStackTrace();}
    }

    private Chessboard model;
    private ChessboardComponent view;
    private static PlayerColor currentPlayer;
    private ChessGameFrame chessGameFrame;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    public GameController(ChessGameFrame chessGameFrame,ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        this.chessGameFrame = chessGameFrame;
        aIplayer = new AIplayer(chessGameFrame);
        Count.resetCOUNT();

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    public static void resetCurrentPlayer(){
        currentPlayer = PlayerColor.BLUE;
    }

    public static PlayerColor getCurrentPlayer(){
        return currentPlayer;
    }
    // after a valid move swap the player
    public static void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private int win() {
        System.out.println(Count.getCOUNT()/2);
        ChessboardPoint bluegoal = new ChessboardPoint(0,3);
        int redanimal = 8;
        int blueanimal = 8;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint c = new ChessboardPoint(i,j);
                if(view.getGridComponentAt(c).getComponents().length!=0){
                    if(model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.RED)){redanimal--;}
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint c = new ChessboardPoint(i,j);
                if(view.getGridComponentAt(c).getComponents().length!=0){
                    if(model.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)){blueanimal--;}
                }
            }
        }
        if(view.getGridComponentAt(bluegoal).getComponentCount() != 0 && model.getChessPieceOwner(bluegoal).equals(PlayerColor.BLUE)){
            System.out.println("Blue team win the game!");
            try {oos.writeObject(Count.getCOUNT());} catch (IOException e) {e.printStackTrace();}
            Count.resetCOUNT();return 1;
        }else if(redanimal==8){
            System.out.println("Blue team win the game!");
            try {oos.writeObject(Count.getCOUNT());} catch (IOException e) {e.printStackTrace();}
            Count.resetCOUNT();return 1;
        }
        ChessboardPoint redgoal = new ChessboardPoint(8,3);
        if(view.getGridComponentAt(redgoal).getComponentCount() != 0 && model.getChessPieceOwner(redgoal).equals(PlayerColor.RED)){
            System.out.println("Red team win the game!");
            try {oos.writeObject(Count.getCOUNT());} catch (IOException e) {e.printStackTrace();}
            Count.resetCOUNT();return 2;
        }else if(blueanimal == 8){
            System.out.println("Red team win the game!");
            try {oos.writeObject(Count.getCOUNT());} catch (IOException e) {e.printStackTrace();}
            Count.resetCOUNT();return 2;
        }
        // TODO: Check the board if there is a winner
        return 0;
    }

    public Chessboard getModel(){
        return model;
    }

    private void checkwin(){
        if(win()==0){}else if(win()==1){
            System.out.println("Game has ended");
            chessGameFrame.afterWin(PlayerColor.BLUE);
            JOptionPane.showMessageDialog(view,"Blue team win, game will be restart");
            view.resetAllChessComponent(model);resetFile();selectedPoint = null;return;
        }else{
            System.out.println("Game has ended");
            chessGameFrame.afterWin(PlayerColor.RED);
            JOptionPane.showMessageDialog(view,"Red team win,game will be restart");
            view.resetAllChessComponent(model);resetFile();selectedPoint = null;return;
        }
    }

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            try {
                oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                +","+model.getGrid()[point.getRow()][point.getCol()].toString()
                +","+selectedPoint+","+point);
                ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                                +","+model.getGrid()[point.getRow()][point.getCol()].toString()
                                +","+selectedPoint+","+point);
                System.out.println("method1 act");
                Count.plusCOUNT();
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.moveChessPiece(selectedPoint, point);
            if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
            }// TODO:剩下所有种类的棋子，已写完

            checkwin();
            if(view.getBlueTrapCell().contains(point)&&model.getChessPieceOwner(point).equals(PlayerColor.RED)){
                model.getGrid()[point.getRow()][point.getCol()].getPiece().setRank(0);
            }else if(view.getRedTrapCell().contains(point)&&model.getChessPieceOwner(point).equals(PlayerColor.BLUE)){
                model.getGrid()[point.getRow()][point.getCol()].getPiece().setRank(0);
            } // TODO: if the chess enter Dens,已写完
            if(view.getBlueTrapCell().contains(selectedPoint)&&model.getChessPieceOwner(point).equals(PlayerColor.RED)){
                model.getGrid()[point.getRow()][point.getCol()].getPiece().setRank(model.getGrid()[point.getRow()][point.getCol()].getPiece().getRANK());
            }else if(view.getRedTrapCell().contains(selectedPoint)&&model.getChessPieceOwner(point).equals(PlayerColor.BLUE)){
                model.getGrid()[point.getRow()][point.getCol()].getPiece().setRank(model.getGrid()[point.getRow()][point.getCol()].getPiece().getRANK());
            }
            // TODO: if the chess enter Traps,已写完
            selectedPoint = null;
            swapColor();
            AIwilldo();
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, ElephantChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                    +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                    +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子,已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//已写完
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, LionChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//已写，待查验
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, TigerChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();

                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//已写，待查验
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, LeopardChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, WolfChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, DogChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, CatChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, RatChessComponent component) {//point为被选中,selected为动作发出者
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);//代表红圈
            component.repaint();
        } else {
            if(model.isValidCapture(selectedPoint,point)){
                try {
                    oos.writeObject(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    ChessGameFrame.steps.add(model.getGrid()[selectedPoint.getRow()][selectedPoint.getCol()].getPiece().toString()
                            +","+model.getGrid()[point.getRow()][point.getCol()].getPiece().toString()
                            +","+selectedPoint+","+point);
                    System.out.println("method2 act");
                    Count.plusCOUNT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.captureChessPiece(selectedPoint,point);
                view.removeChessComponentAtGrid(point);
                if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof ElephantChessComponent){
                    view.setChessComponentAtGrid(point, (ElephantChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LionChessComponent){
                    view.setChessComponentAtGrid(point, (LionChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof TigerChessComponent){
                    view.setChessComponentAtGrid(point, (TigerChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof LeopardChessComponent){
                    view.setChessComponentAtGrid(point, (LeopardChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof WolfChessComponent){
                    view.setChessComponentAtGrid(point, (WolfChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof DogChessComponent){
                    view.setChessComponentAtGrid(point, (DogChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof CatChessComponent){
                    view.setChessComponentAtGrid(point, (CatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }else if(view.getGridComponentAt(selectedPoint).getComponents()[0] instanceof RatChessComponent){
                    view.setChessComponentAtGrid(point, (RatChessComponent) view.removeChessComponentAtGrid(selectedPoint));
                }// TODO:剩下所有种类的棋子，已写完
                selectedPoint = null;
                component.setSelected(false);//代表红圈
                component.repaint();
                view.repaint();
                checkwin();
                swapColor();
                AIwilldo();
            }else{
                System.out.println("can't go there since target is bigger");
            }
        }
        // TODO: Implement capture function//
    }

    private void AIwilldo(){
        if(currentPlayer.equals(PlayerColor.RED)&&AIexist==true){
            ChessboardPoint[] aIbehave;
            if(AIexist1==true){
                aIbehave = aIplayer.greedyGo();
            }else{
                aIbehave = aIplayer.randomGo();
            }
            int behave = aIplayer.eatORmove;
            selectedPoint = aIbehave[0];
            if(behave==0){
                Point p = new Point(aIbehave[1].getRow(),aIbehave[1].getCol());
                JComponent jc = (JComponent) chessGameFrame.getChessboardComponent().getComponentAt(p);
                onPlayerClickCell(aIbehave[1],(CellComponent)jc);
                view.repaint();
            }else if(behave==1){
                //model.captureChessPiece(aIbehave[0],aIbehave[1]);
                if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof ElephantChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (ElephantChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof LionChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (LionChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof TigerChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (TigerChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof LeopardChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (LeopardChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof WolfChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (WolfChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof DogChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (DogChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof CatChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (CatChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                else if(chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0] instanceof RatChessComponent){
                    onPlayerClickChessPiece(aIbehave[1], (RatChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0]);}
                chessGameFrame.getChessboardComponent().getGridComponentAt(aIbehave[1]).getComponents()[0].repaint();
                checkwin();
                selectedPoint = null;
                view.repaint();
            }
        }else{view.repaint();}
    }

}
