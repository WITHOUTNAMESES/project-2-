package view;

import controller.GameController;
import model.Chessboard;
import model.Count;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame implements Serializable {
    //    public final Dimension FRAME_SIZE ;
    protected final int WIDTH;
    protected final int HEIGTH;
    protected Chessboard chessboard;
    public Chessboard getChessboard(){
        return chessboard;
    }
    private int scoreBlue=0; private int scoreRed=0;

    private final int ONE_CHESS_SIZE;
    protected ChessboardComponent chessboardComponent;
    public ChessGameFrame(int width, int height, Chessboard chessboard) {
        setTitle("2023 CS109 Project"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;
        this.chessboard = chessboard;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addChessboard();
        addLabel();
        addRestartButton();
        addtButton();
        addt1Button();
        addtsaveButton();
        changeButton();
        addchangeAIButton();
        setBackground();

    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private String score = String.format("Blue vs Red\n%s\t :  %s",scoreBlue,scoreRed);
    private JLabel statusLabel = new JLabel(score);
    private void addLabel() {
        statusLabel.setLocation(HEIGTH, HEIGTH / 10-40);
        statusLabel.setSize(200, 120);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }
    public void afterWin(PlayerColor color){
        if(color.equals(PlayerColor.BLUE)){
            scoreBlue++;remove(statusLabel);
            score = String.format("Blue vs Red\n%s\t :  %s",scoreBlue,scoreRed);
            statusLabel = new JLabel(score);
            statusLabel.setLocation(HEIGTH, HEIGTH / 10-40);
            statusLabel.setSize(200, 120);
            statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
            add(statusLabel);
            resetBG();
            repaint();
        }else{
            scoreRed++;remove(statusLabel);
            score = String.format("Blue vs Red\n%s\t :  %s",scoreBlue,scoreRed);
            statusLabel = new JLabel(score);
            statusLabel.setLocation(HEIGTH, HEIGTH / 10-40);
            statusLabel.setSize(200, 120);
            statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
            add(statusLabel);
            resetBG();
            repaint();
        }
    }

    private void addRestartButton() {//TODO:实现初始化游戏
        JButton button = new JButton("Click to restart");
        button.addActionListener((e) -> chessboardComponent.resetAllChessComponent(chessboard));
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    public static ArrayList<String> steps = new ArrayList<>();
    private void withdraw(){
        if( Count.getCOUNT()==0){
            System.out.println("no more withdraw");return;
        }
        String aim = steps.get(Count.getCOUNT()-1);
        String[] aims = aim.split(",");
        if(aims[1].equals("noChess")){
            chessboardComponent.withdrawMove(aims,chessboard);
        }else{
            chessboardComponent.withdrawCapture(aims,chessboard);
        }
        steps.remove(Count.getCOUNT()-1);
        GameController.deleteLastStep();
        Count.minCOUNT();
    }

    private void load(){
        Load lo = new Load();
        lo.initUI();
    }
    protected void replaymovemethod(ArrayList<String> gameSteps){
        chessboardComponent.replayAllChessComponent(chessboard,gameSteps);
        if(GameController.getCurrentPlayer().equals(PlayerColor.BLUE)){
            JOptionPane.showMessageDialog(chessboardComponent,"BLUE team go");
        }else {
            JOptionPane.showMessageDialog(chessboardComponent,"RED team go");
        }
    }


    private void Save(){
        Save save = new Save(this.WIDTH,this.HEIGTH,this.chessboard);
        save.initUI();
    }

    private void addchangeAIButton() {//TODO:实现复现操作
        JButton button = new JButton("AIChange");
        button.addActionListener((e) -> GameController.changeAI());
        button.setLocation(HEIGTH, HEIGTH / 10 + 400);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addtButton() {//TODO:实现复现操作
        JButton button = new JButton("Click to Load");
        button.addActionListener((e) -> load());
        button.setLocation(HEIGTH, HEIGTH / 10 + 50);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addt1Button() {//TODO:实现撤回操作
        JButton button = new JButton("Withdraw");
        button.addActionListener((e) -> withdraw());//TODO
        button.setLocation(HEIGTH, HEIGTH / 10 +200);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addtsaveButton() {//TODO:实现保存操作
        JButton button = new JButton("Click to Save");
        button.addActionListener((e) -> Save());
        button.setLocation(HEIGTH, HEIGTH / 10+260);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void changeButton() {//TODO:实现更换背景
        JButton button = new JButton("Click to change");
        button.addActionListener((e) -> change());//
        button.setLocation(HEIGTH, HEIGTH / 10+320);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void resetBG(){
        String f = null;
        i--;if(i<0){i=3;}
        switch (i){
            case 0:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\animal.jpg";break;
            case 1:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\cloud.jpg";break;
            case 2:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\sunset.jpg";break;
            case 3:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\forest.jpg";break;
        }
        i++;
        if(i>3){i=0;}
        background.setIcon(null);
        background = new JLabel(new ImageIcon(f));
        background.setBounds(0,0,getWidth(),getHeight());
        getContentPane().add(background);
    }

    private void change(){
        String f = null;
        switch (i){
            case 0:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\animal.jpg";break;
            case 1:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\cloud.jpg";break;
            case 2:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\sunset.jpg";break;
            case 3:
                f = "C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\forest.jpg";break;
        }
        i++;
        if(i>3){i=0;}
        background.setIcon(null);
        background = new JLabel(new ImageIcon(f));
        background.setBounds(0,0,getWidth(),getHeight());
        getContentPane().add(background);
    }

    private int i = 0;

    private JLabel background;
    public void setBackground(){
        background = new JLabel(new ImageIcon("C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\BG\\forest.jpg"));
        background.setBounds(0,0,getWidth(),getHeight());
        getContentPane().add(background);
    }

//        private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }


    public class Load {
        static JTextField text_name = new JTextField();

        //在类中定义初始化界面的方法
        public void initUI() {
            //在initUI中实例化JFrame类的对象
            JFrame frame = new JFrame();
            //设置窗体对象的属性值
            frame.setTitle("输入对局名");//设置窗体标题
            frame.setSize(400, 250);//设置窗体大小，只对顶层容器生效
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);//设置窗体相对于另一组间的居中位置，参数null表示窗体相对于屏幕的中央位置
            frame.setResizable(false);//禁止调整窗体大小
            frame.setFont(new Font("宋体",Font.PLAIN,14));//设置字体，显示格式正常，大小

            //实例化FlowLayout流式布局类的对象，指定对齐方式为居中对齐组件之间的间隔为10个像素
            FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
            //实例化流式布局类的对象
            frame.setLayout(fl);

            //实例化JLabel标签对象
            JLabel labname = new JLabel("文件名：");
            labname.setFont(new Font("宋体",Font.PLAIN,14));
            //将labname标签添加到窗体上
            frame.add(labname);

            //实例化JTextField标签对象化
            Dimension dim1 = new Dimension(300,30);
            text_name.setPreferredSize(dim1);//设置除顶级容器组件以外其他组件的大小
            //将textName标签添加到窗体上
            frame.add(text_name);
            addbutton();
            frame.add(button1);

            frame.setVisible(true);

        }
        JButton button1 = new JButton();
        //设置按键的显示内容
       private void addbutton(){
            Dimension dim2 = new Dimension(100,30);
            button1.setText("确定");
            button1.setFont(new Font("宋体",Font.PLAIN,14));
            button1.addActionListener(e -> replaymovemethod(method()));
            button1.setSize(dim2);
            button1.setVisible(true);
       }

       public static ArrayList<String> method(){
           try {
               ObjectInputStream ois = new ObjectInputStream(new FileInputStream(text_name.getText()));
               ArrayList<String> gameSteps = new ArrayList<>();
               for (int i = 0; i < 10000; i++) {
                   String gameStep = (String) ois.readObject();
                   if(Character.isDigit(gameStep.charAt(0))){
                       int co = Integer.parseInt(gameStep);
                       Count.setCOUNT(co);break;
                   }
                   System.out.println(gameStep);
                   gameSteps.add(gameStep);
               }
               steps = gameSteps;
               ois.close();
               return gameSteps;
           } catch (FileNotFoundException ex) {
               ex.printStackTrace();
           } catch (IOException ex) {
               ex.printStackTrace();
           } catch (ClassNotFoundException ex) {
               ex.printStackTrace();
           }
           return null;
       }
    }
}
