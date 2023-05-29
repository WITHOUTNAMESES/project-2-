package model;

import view.*;

import java.util.Objects;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;

    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19

        initGrid();
        initPieces();
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void resetPieces() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];
        initGrid();
        initPieces();
    }

    private void initPieces() {
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));

        //初始化，已写两个象，剩下如法炮制
    }

    private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    //获取棋子
    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    //获取位置
    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }//计算距离？abs取绝对值

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }//移除棋子（吃子），移动棋子（后）

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    //移动棋子（前）
    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        setChessPiece(dest, removeChessPiece(src));
    }

    //                                  暂定为先选中           暂定为后选中
    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        removeChessPiece(dest);
        setChessPiece(dest, removeChessPiece(src));
        // TODO: Finish the method.//已写，待查验
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        if(dest.getRow()==8 && dest.getCol()==3){
            if(getChessPieceOwner(src).equals(PlayerColor.BLUE)){
                System.out.println("can't go its own den");return false;}
        }
        if(dest.getRow()==0 && dest.getCol()==3){
            if(getChessPieceOwner(src).equals(PlayerColor.RED)){
                System.out.println("can't go its own den");return false;}
        }
        if (calculateDistance(src, dest) == 1) {
            if (dest.getRow() == 3 || dest.getRow() == 4 || dest.getRow() == 5) {
                if (dest.getCol() == 1 || dest.getCol() == 2 || dest.getCol() == 4 || dest.getCol() == 5) {
                    if (getChessPieceAt(src).getName().equals("Rat")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        if (calculateDistance(src, dest) == 4) {
            if (getChessPieceAt(src).getName().equals("Tiger") || getChessPieceAt(src).getName().equals("Lion")) {
                if (src.getCol() == 1 || src.getCol() == 2 || src.getCol() == 4 || src.getCol() == 5) {
                    if (src.getRow() == 2 || src.getRow() == 6) {
                        if (getGrid()[3][src.getCol()].getPiece() == null && getGrid()[4][src.getCol()].getPiece() == null && getGrid()[5][src.getCol()].getPiece() == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        if (calculateDistance(src, dest) == 3) {
            if (getChessPieceAt(src).getName().equals("Tiger") || getChessPieceAt(src).getName().equals("Lion")) {
                if (src.getCol() == 0) {
                    if (src.getRow() == 3 || src.getRow() == 4 || src.getRow() == 5) {
                        if (grid[src.getRow()][1].getPiece() == null && grid[src.getRow()][2].getPiece() == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                if (src.getCol() == 3) {
                    if (src.getRow() == 3 || src.getRow() == 4 || src.getRow() == 5) {
                        if (grid[src.getRow()][1].getPiece() == null && grid[src.getRow()][2].getPiece() == null && grid[src.getRow()][4].getPiece() == null && grid[src.getRow()][5].getPiece() == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                if (src.getCol() == 6) {
                    if (src.getRow() == 3 || src.getRow() == 4 || src.getRow() == 5) {
                        if (grid[src.getRow()][4].getPiece() == null && grid[src.getRow()][5].getPiece() == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        if (getChessPieceAt(src).canCapture(getChessPieceAt(dest))) {
            return isValidMove(src, dest);//判断过河暂定
        } else {
            return false;
        }
        // TODO:Fix this method//已写，待查验
    }//与captureChessPiece共同书写
}
