package view;

import model.Chessboard;
import model.Count;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.io.*;

import javax.swing.*;

public class SaveListener extends ChessGameFrame implements ActionListener{
    private javax.swing.JTextField text_name;
    private javax.swing.JFrame login;

    public SaveListener(javax.swing.JFrame login, javax.swing.JTextField text_name,int width, int height, Chessboard chessboard)
    {
        super(width,height,chessboard);//获取登录界面、账号密码输入框对象
        this.login=login;
        this.text_name=text_name;
    }

    public void actionPerformed(ActionEvent e)
    {
        Dimension dim3 = new Dimension(300,30);

        javax.swing.JFrame login2 = new javax.swing.JFrame();
        login2.setSize(400,200);
        login2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        login2.setLocationRelativeTo(null);
        login2.setFont(new Font("宋体",Font.PLAIN,14));

        javax.swing.JPanel jp1 = new JPanel();

            JLabel message = new JLabel("保存成功");
            message.setFont(new Font("宋体",Font.PLAIN,14));
            message.setPreferredSize(dim3);
            jp1.add(message);
            login2.add(jp1,BorderLayout.CENTER);

            login2.setResizable(false);
            login2.setVisible(true);

        // 新的文件或目录
        try {
            ObjectOutputStream newname = new ObjectOutputStream(new FileOutputStream(text_name.getText()));
            if(!text_name.getText().equals("gameStep.txt")){
                ObjectInputStream use = new ObjectInputStream(new FileInputStream("gameStep.txt"));
                for (int i = 0; i < Count.getCOUNT(); i++) {
                    String aim = (String) use.readObject();
                    newname.writeObject(aim);
                }
                String co = String.valueOf(Count.getCOUNT());
                newname.writeObject(co);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
