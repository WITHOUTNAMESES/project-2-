import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;
import view.BGsound;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Chessboard chessboard = new Chessboard();
            // ChessGameFrame mainFrame = new ChessGameFrame(1100, 810, chessboard);
            MainFrame enterFrame = new MainFrame();
//            GameController gameController = new GameController(mainFrame.getChessboardComponent(), chessboard);
            enterFrame.setVisible(true);
            //BGsound audioPlayWave = new BGsound("BGM.wav");// 开音乐(冒号里的内容与音乐文件名一致)
            //audioPlayWave.start();
            @SuppressWarnings("unused")
            int musicOpenLab = 1;
        });
    }
}
