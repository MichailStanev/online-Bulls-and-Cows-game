import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NumPanelButton extends JButton implements ActionListener{
    public ImageIcon X;
    public ImageIcon O;


    byte value=0;
    int i = 0;
	/*
	0:num
	1:X
	2:O
	*/

    public NumPanelButton(){
        X=new ImageIcon(this.getClass().getResource("X.png"));
        O=new ImageIcon(this.getClass().getResource("O.png"));

        this.addActionListener(this);
        this.setContentAreaFilled(false);
        this.setOpaque(false);

    }

    public void actionPerformed(ActionEvent e){
        value++;
        value%=3;
        switch(value){
            case 0:
                setIcon(null);
                break;
            case 1:
                setIcon(X);
                break;
            case 2:
                setIcon(O);
                break;
        }
    }
}