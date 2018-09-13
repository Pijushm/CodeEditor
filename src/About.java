import javax.swing.*;
import java.awt.*;

/**
 * Created by Pijush on 1/27/2016.
 */
public class About extends JDialog {
    public About(Frame owner)
    {
        super(owner, "About CodeEditor", true);

        JPanel panel=new JPanel();
        panel.setBackground(Color.darkGray);
        panel.setLayout(new GridLayout(2, 1));
        JLabel label=new JLabel("CodeEditor",SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman",Font.BOLD,16));

        panel.add(label,BorderLayout.NORTH);

        String message =
                "        Developer's name: Pijush Mohanto\n" +
                "        Email: pijush.m@hotmail.com\n";

        JTextArea area=new JTextArea(message);
        area.setBackground(Color.darkGray);
        area.setForeground(Color.white);
        area.setEditable(false);
        panel.add(area);

        getContentPane().add(panel);


        setBounds(400,250,280,150);
        setResizable(false);

        setVisible(true);




    }


}
