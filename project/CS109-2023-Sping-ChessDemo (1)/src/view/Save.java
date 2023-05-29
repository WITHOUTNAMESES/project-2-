package view;

import model.Chessboard;

import java.awt.Dimension;  //封装了一个构件的高度和宽度
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

public class Save extends ChessGameFrame{
public Save(int width, int height, Chessboard chessboard) {
        super(width, height, chessboard);
        }
    //在类中定义初始化界面的方法
    public void initUI() {
        //在initUI中实例化JFrame类的对象
        JFrame frame = new JFrame();
        //设置窗体对象的属性值
        frame.setTitle("保存对局");//设置窗体标题
        frame.setSize(400, 250);//设置窗体大小，只对顶层容器生效
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//设置窗体关闭操作，3表示关闭窗体退出程序
        frame.setLocationRelativeTo(null);//设置窗体相对于另一组间的居中位置，参数null表示窗体相对于屏幕的中央位置
        frame.setResizable(false);//禁止调整窗体大小
        frame.setFont(new Font("宋体",Font.PLAIN,14));//设置字体，显示格式正常，大小

        //实例化FlowLayout流式布局类的对象，指定对齐方式为居中对齐组件之间的间隔为10个像素
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        //实例化流式布局类的对象
        frame.setLayout(fl);

        //实例化JLabel标签对象，该对象显示“账号”
        JLabel labname = new JLabel("文件名：");
        labname.setFont(new Font("宋体",Font.PLAIN,14));
        //将labname标签添加到窗体上
        frame.add(labname);

        //实例化JTextField标签对象化
        JTextField text_name = new JTextField();
        Dimension dim1 = new Dimension(300,30);
        text_name.setPreferredSize(dim1);//设置除顶级容器组件以外其他组件的大小
        //将textName标签添加到窗体上
        frame.add(text_name);

        //实例化JButton组件
        JButton button1 = new JButton();
        //设置按键的显示内容
        Dimension dim2 = new Dimension(100,30);
        button1.setText("确定");
        button1.setFont(new Font("宋体",Font.PLAIN,14));
        //设置按键大小
        button1.setSize(dim2);
        frame.add(button1);

        frame.setVisible(true);//窗体可见，一定要放在所有组件加入窗体后

        SaveListener ll = new SaveListener(frame,text_name,WIDTH,HEIGTH,chessboard);
        button1.addActionListener(ll);
    }

}
