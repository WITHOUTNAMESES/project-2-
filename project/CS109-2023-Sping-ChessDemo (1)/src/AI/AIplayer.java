package AI;

import controller.GameController;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Random;

public class AIplayer {

    private static final long serialVersionUID = 6496098798146410884L;


    private ChessGameFrame chessGameFrame;
    private Chessboard chessboard;
    public AIplayer( ChessGameFrame chessGameFrame){
        this.chessboard = chessGameFrame.getChessboard();
        this.chessGameFrame = chessGameFrame;
    }

    public int eatORmove = 0;

    public  ChessboardPoint[] greedyGo(){
        ArrayList<JComponent> chessComponents = new ArrayList<>();
        ArrayList<ChessPiece> chesses = new ArrayList<>();
        ArrayList<ChessboardPoint> beforePoints = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint cp = new ChessboardPoint(i,j);
                if(chessboard.getGrid()[i][j].getPiece()!=null && chessboard.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.RED)){
                    chessComponents.add(chessGameFrame.getChessboardComponent().getGridComponentAt(cp));
                    chesses.add(chessboard.getGrid()[i][j].getPiece());
                    beforePoints.add(cp);
                }
            }
        }
        int[][] direction = {
                {1,0},{0,1},{-1,0},{0,-1}
        };
        ChessboardPoint[] goal = new ChessboardPoint[2];
        for (int k = 0; k < chessComponents.size(); k++) {
            chessComponents.get(k);
            for (int i = 0; i < 4; i++) {
                ChessboardPoint afterPoint = new ChessboardPoint(beforePoints.get(k).getRow()+direction[i][0],beforePoints.get(k).getCol()+direction[i][1]);
                if(afterPoint.getRow()<= 8 && afterPoint.getCol()<= 6 && afterPoint.getRow()>= 0 && afterPoint.getCol()>= 0){
                    if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents().length!=0 &&
                            chessboard.getGrid()[afterPoint.getRow()][afterPoint.getCol()].getPiece().getOwner().equals(PlayerColor.BLUE) &&
                            chessboard.isValidCapture(beforePoints.get(k),afterPoint)){//若有棋子，判断能否捕食
                        eatORmove = 1;
                        goal[0] = beforePoints.get(k);goal[1] = afterPoint;return goal;
                        //返回两个格子和捕食
                    }
                }
            }
        }
        ChessPiece[] chessPieces = new ChessPiece[chesses.size()];
        for (int i = 0; i < chesses.size(); i++) {
            chessPieces[i] = chesses.get(i);
        }
        for (int i = 0; i < chessPieces.length; i++) {
            for (int j = 1; j < chessPieces.length-i; j++) {
                if(chessPieces[j-1].getRANK()>chessPieces[j].getRANK()){
                    ChessboardPoint m = beforePoints.get(j-1);
                    beforePoints.set(j-1,beforePoints.get(j));
                    beforePoints.set(j,m);
                    JComponent mid = chessComponents.get(j-1);
                    chessComponents.set(j-1,chessComponents.get(j));
                    chessComponents.set(j,mid);
                    ChessPiece middle = chessPieces[j-1];
                    chessPieces[j-1] = chessPieces[j];
                    chessPieces[j] = middle;
                }
            }
        }
        for (int i = chessPieces.length-1; i >=0 ; i--) {
            for (int j = 0; j < 4; j++) {
                ChessboardPoint afterPoint = new ChessboardPoint(beforePoints.get(i).getRow()+direction[j][0],
                        beforePoints.get(i).getCol()+direction[j][1]);
                if(afterPoint.getRow()<= 8 && afterPoint.getCol()<= 6 && afterPoint.getRow()>= 0 && afterPoint.getCol()>= 0){//判断afterpoint存在
                    if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents().length == 0 && chessboard.isValidMove(beforePoints.get(i),afterPoint)){
                        eatORmove = 0;
                        goal[0] = beforePoints.get(i);goal[1] = afterPoint;return goal;
                        //返回两个格子和move
                    }else{}
                }else{}
            }
        }

        return null;

    }

    public ChessboardPoint[] randomGo(){
        ArrayList<JComponent> chessComponents = new ArrayList<>();
        ArrayList<ChessPiece> chesses = new ArrayList<>();
        ArrayList<ChessboardPoint> beforePoints = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint cp = new ChessboardPoint(i,j);
                if(chessboard.getGrid()[i][j].getPiece()!=null && chessboard.getGrid()[i][j].getPiece().getOwner().equals(PlayerColor.RED)){
                    chessComponents.add(chessGameFrame.getChessboardComponent().getGridComponentAt(cp));
                    chesses.add(chessboard.getGrid()[i][j].getPiece());
                    beforePoints.add(cp);
                }
            }
        }

        Random random = new Random();
        int m = random.nextInt(chessComponents.size());
        Random random1 = new Random();
        int direct = random1.nextInt(4);
        int[][] direction = {
                {-1,0},{0,1},{1,0},{0,-1}
        };
        ChessboardPoint[] goal = new ChessboardPoint[2];
        for (int i = 0; i < 4; i++) {
            ChessboardPoint afterPoint = new ChessboardPoint(beforePoints.get(m).getRow()+direction[direct][0],beforePoints.get(m).getCol()+direction[direct][1]);
            if(afterPoint.getRow()<= 8 && afterPoint.getCol()<= 6 && afterPoint.getRow()>= 0 && afterPoint.getCol()>= 0){//判断afterpoint存在
                if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents().length!=0 &&
                        chessboard.getGrid()[afterPoint.getRow()][afterPoint.getCol()].getPiece().getOwner().equals(PlayerColor.BLUE) &&
                        chessboard.isValidCapture(beforePoints.get(m),afterPoint)){//若有棋子，判断能否捕食
                    eatORmove = 1;
                     goal[0] = beforePoints.get(m);goal[1] = afterPoint;return goal;
                    //返回两个格子和捕食
                }else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents().length == 0 && chessboard.isValidMove(beforePoints.get(m),afterPoint)){
                    Point p = new Point(afterPoint.getRow(),afterPoint.getCol());
                    eatORmove = 0;
                    goal[0] = beforePoints.get(m);goal[1] = afterPoint;return goal;
                    //返回两个格子和move
                }else{}
            }else{}
            direct++;if(direct>3){direct=0;}
        }
        return null;
    }
    //调用所有棋子
}

//JComponent jc = (JComponent) chessGameFrame.getChessboardComponent().getComponentAt(p);
//                    gameController.onPlayerClickCell(beforePoints.get(m),(CellComponent)jc);
//                    chessGameFrame.repaint();chessGameFrame.getChessboardComponent().repaint();
//                    return;


//if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof ElephantChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (ElephantChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof LionChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (LionChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof TigerChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (TigerChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof LeopardChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (LeopardChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof WolfChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (WolfChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof DogChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (DogChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof CatChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (CatChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
//                    else if(chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0] instanceof RatChessComponent){
//                        gameController.onPlayerClickChessPiece(beforePoints.get(m), (RatChessComponent) chessGameFrame.getChessboardComponent().getGridComponentAt(afterPoint).getComponents()[0]);}
