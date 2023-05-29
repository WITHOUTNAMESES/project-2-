package view;
import AI.AIplayer;
import controller.GameController;
import model.Chessboard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainFrame extends JFrame implements MouseListener {
    //设置窗体的基本属性	大小
    /**
     *  1.1、设置窗体基本属性大小 居中 边框隐藏 默认关闭按钮 logo图标
     1.2、创建背景面板MainPanel，实现背景图片功能

     2.图片按钮功能
     */
    //2.1创建开始按钮 帮助按钮 离开按钮 组件
    JLabel start, start1,start2,/*help,*/exit;
    private JLabel statusLabel;

    public MainFrame() {//无参构造，创建对象。并在main函数中调用
        //2.2
        this.statusLabel = new JLabel(new ImageIcon("title.png"));
        this.statusLabel.setLocation(10,200);
        this.statusLabel.setSize(1100, 250);
        //this.statusLabel.setFont(new Font("Rockwell", 1, 2000000));
        this.add(this.statusLabel);
        statusLabel.setVisible(true);
        //button.setFont(new Font("Rockwell", 1, 20));

        start = new JLabel("SINGLE-PLAYER");//ImageIcon:图标
        start.setLocation(370, 500);
        start.setSize(500, 120);
        start.setFont(new Font("Rockwell", 1, 50));
        start.setEnabled(false);//false按钮为灰色
        start.addMouseListener(this);
        this.add(start);

        start1 = new JLabel("DOUBLE-PLAYER");//ImageIcon:图标
        //UIManager.put("START", new java.awt.Font("Rockwell", 1, 1000000));
        start1.setLocation(370, 700);
        start1.setSize(500, 120);
        start1.setFont(new Font("Rockwell", 1, 50));
//            start.setBounds(550,400,150,40);
//            start.setFont(new Font("Rockwell", 1, 20));
        start1.setEnabled(false);//false按钮为灰色
        start1.addMouseListener(this);
        this.add(start1);


        start2 = new JLabel("SINGLE-PLAYER2");//ImageIcon:图标
        //UIManager.put("START", new java.awt.Font("Rockwell", 1, 1000000));
        start2.setLocation(370, 600);
        start2.setSize(500, 120);
        start2.setFont(new Font("Rockwell", 1, 50));
//            start.setBounds(550,400,150,40);
//            start.setFont(new Font("Rockwell", 1, 20));
        start2.setEnabled(false);//false按钮为灰色
        start2.addMouseListener(this);
        this.add(start2);

//            help = new JLabel("");
//            help.setBounds(350,420,150,40);
//            help.setEnabled(false);
//            help.addMouseListener(this);
//            this.add(help);

//            exit = new JLabel("EXIT");
//            exit.setBounds(350, 520, 150, 40);
//            exit.setEnabled(false);
//            exit.addMouseListener(this);
//            this.add(exit);


        /**1.实现背景图片及窗体属性*/
        MainPanel panel = new MainPanel();
        this.add(panel);

        //设置窗体基本属性大小 居中 边框隐藏 默认关闭按钮 logo图标
        this.setSize(1100,810);//大小
        this.setLocationRelativeTo(null);//居中
        //this.setUndecorated(true);//边框隐藏
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认关闭
        this.setIconImage(new ImageIcon("C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\forest.jpg").getImage());//logo????
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }

    //2、创建背景面板MainPanel，实现背景图片功能
    class MainPanel extends JPanel{//创建的MainPanel类，在MainFrame中调用
        Image background;
        public MainPanel() {
            try {
                background = ImageIO.read(new File("C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\forest.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(background, 0, 0,1100,810, null);
        }
    }



    //以下五个方法均为添加 implements MouseListener 后，快捷出来的
    @Override
    public void mouseClicked(MouseEvent e) {
        //鼠标点击
        if(e.getSource().equals(start)){
            //跳转到单人模式界面
            mousePressed(e);
            Chessboard chessboard = new Chessboard();
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810, chessboard);
            GameController gameController = new GameController(mainFrame,mainFrame.getChessboardComponent(), chessboard);
            mainFrame.setVisible(true);
            BGsound audioPlayWave = new BGsound("BGM.wav");// 开音乐(冒号里的内容与音乐文件名一致)
            audioPlayWave.start();
            @SuppressWarnings("unused")
            int musicOpenLab = 1;
            //如果启用了ai
            gameController.AIexists();
            //关闭当前界面
            dispose();
        } else if(e.getSource().equals(start1)) {
            //跳转到双人对战界面//这里跳转还得改
            mousePressed(e);
            Chessboard chessboard = new Chessboard();
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810, chessboard);
            GameController gameController = new GameController(mainFrame,mainFrame.getChessboardComponent(), chessboard);
            mainFrame.setVisible(true);
            BGsound audioPlayWave = new BGsound("BGM.wav");// 开音乐(冒号里的内容与音乐文件名一致)
            audioPlayWave.start();
            //关闭当前界面
            dispose();
        }else if(e.getSource().equals(start2)) {
            //跳转到双人对战界面//这里跳转还得改
            mousePressed(e);
            Chessboard chessboard = new Chessboard();
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810, chessboard);
            GameController gameController = new GameController(mainFrame,mainFrame.getChessboardComponent(), chessboard);
            mainFrame.setVisible(true);
            BGsound audioPlayWave = new BGsound("BGM.wav");// 开音乐(冒号里的内容与音乐文件名一致)
            audioPlayWave.start();
            //关闭当前界面
            dispose();
            gameController.AIexists();gameController.AIexist1();
        }
    }




    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }



    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }




    @Override
    public void mouseEntered(MouseEvent e) {
        // 鼠标移入
        if(e.getSource().equals(start)){//e指一个事件。e.getSource()获取事件
            //如果鼠标移入到（start）组件（图片按钮）
            start.setEnabled(true);
        }if(e.getSource().equals(start1)){//e指一个事件。e.getSource()获取事件
            //如果鼠标移入到（start）组件（图片按钮）
            start1.setEnabled(true);
        }
//            else if(e.getSource().equals(help)){
//                help.setEnabled(true);
//            }
//            else if(e.getSource().equals(exit)){
//                exit.setEnabled(true);
//            }
    }




    @Override
    public void mouseExited(MouseEvent e) {
        //鼠标移出
        if(e.getSource().equals(start)){
            start.setEnabled(false);
        }
        if(e.getSource().equals(start1)){
            start1.setEnabled(false);
        }
//            else if(e.getSource().equals(help)){
//                help.setEnabled(false);
//            }
//            else if(e.getSource().equals(exit)){
//                exit.setEnabled(false);
//            }
    }
}

