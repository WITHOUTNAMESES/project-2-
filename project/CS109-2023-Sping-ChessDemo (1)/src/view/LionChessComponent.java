package view;


import model.PlayerColor;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class LionChessComponent extends JComponent {
    private PlayerColor owner;

    private boolean selected;
    private Image image1;
    private Image image2;
    private String filename;
    public LionChessComponent(PlayerColor owner, int size) {
        this.filename = "lion.wav";
        this.owner = owner;
        this.selected = false;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
        image1 = (new ImageIcon("C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\玩家1\\lion.png").getImage());
        image2 = (new ImageIcon("C:\\Users\\名称黑洞\\Desktop\\project（2）\\image源\\玩家2\\lion2.png").getImage());

    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        File soundFile = new File(filename);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        int nBytesRead = 0;
//这是缓冲
        byte[] abData = new byte[512];
        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.selected = selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("楷体", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        g2.setColor(owner.getColor());
        if(owner.getColor().equals(Color.RED)){
            g2.drawImage(image1,0,0,getWidth(),getHeight(),null);
        }
        if(owner.getColor().equals(Color.BLUE)){
            g2.drawImage(image2,0,0,getWidth(),getHeight(),null);
        }
        //g2.drawString("狮", getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
