/**
 * Created by Pijush on 1/23/2016.
 */
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FontDialogue extends JDialog {
        protected  String[] m_fontsize=new String[] {"8", "9", "10", "11", "12", "14",
                "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
       protected  JComboBox size;
      protected  JCheckBox chk;
      protected  JLabel label;
      int fontsize=0,fontstyle;

    public FontDialogue(Frame owner,JEditorPane pane,JTextArea area)
    {
        super(owner,"Font",true);


        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel panel=new JPanel(new GridLayout(1,2));
        JPanel panel1=new JPanel();
        panel1.setBorder(new TitledBorder(new EtchedBorder(),"FontSize"));
        size=new JComboBox(m_fontsize);
        size.setSelectedItem(Integer.toString(pane.getFont().getSize()));

         fontsize = Integer.parseInt(size.getSelectedItem().toString());
        System.out.println(fontsize);
        size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontsize = Integer.parseInt(size.getSelectedItem().toString());
                //pane.setFont(new Font("Courier New", Font.PLAIN, fontsize));
                label.setFont(new Font("Courier New", Font.PLAIN, fontsize));
               // area.setFont(new Font("Courier New", Font.PLAIN, fontsize));
                System.out.println(fontsize);


            }
        });


        panel1.add(size);
        panel.add(panel1);
        JPanel panel2=new JPanel(new GridLayout(4,1,0,1));
        panel2.setBorder(new TitledBorder(new EtchedBorder(), "Font Style"));


        ButtonGroup buttonGroup=new ButtonGroup();

        fontstyle=pane.getFont().getStyle();
        if(fontstyle==Font.PLAIN)
        {

        }
        chk=new JCheckBox("Regular");
        if(fontstyle==Font.PLAIN)
        {
            chk.setSelected(true);
        }
        chk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pane.setFont(new Font("Courier New", Font.PLAIN, fontsize));
                label.setFont(new Font("Courier New", Font.PLAIN, fontsize));
               // area.setFont(new Font("Courier New", Font.PLAIN, fontsize));
            }
        });
        chk.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        buttonGroup.add(chk);
        panel2.add(chk);
        chk=new JCheckBox("Bold");
        if(fontstyle==Font.BOLD)
            chk.setSelected(true);
        chk.setFont(new Font("Times New Roman", Font.BOLD, 12));
        chk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pane.setFont(new Font("Courier New", Font.BOLD, fontsize));
                label.setFont(new Font("Courier New", Font.BOLD, fontsize));
                //area.setFont(new Font("Courier New", Font.BOLD, fontsize));
            }
        });
        buttonGroup.add(chk);
       panel2.add(chk);
        chk=new JCheckBox("Italic");
        if(fontstyle==Font.ITALIC)
            chk.setSelected(true);
        chk.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        chk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // pane.setFont(new Font("Courier New", Font.ITALIC, fontsize));
                label.setFont(new Font("Courier New", Font.ITALIC, fontsize));
                //area.setFont(new Font("Courier New", Font.ITALIC, fontsize));
            }
        });
        buttonGroup.add(chk);
        panel2.add(chk);
        chk=new JCheckBox("Bold Italic");
        if(fontstyle==(Font.BOLD|Font.ITALIC))
            chk.setSelected(true);
        chk.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 12));
        chk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pane.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, fontsize));
                label.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, fontsize));
               // area.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, fontsize));
            }
        });
        buttonGroup.add(chk);

        panel2.add(chk);




        panel.add(panel2);
        getContentPane().add(panel);
        JPanel panel3=new JPanel();
        panel3.setBorder(new TitledBorder(new EtchedBorder(),"Preview"));
         label=new JLabel("Text");
        label.setFont(new Font("Courier New",fontstyle,fontsize));
        panel3.add(label);
        getContentPane().add(panel3);

        JPanel panel4=new JPanel(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();

        JButton button=new JButton("OK");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pane.setFont(new Font("Courier New", label.getFont().getStyle(),label.getFont().getSize()));
                area.setFont(new Font("Courier New", label.getFont().getStyle(),label.getFont().getSize()));

                setVisible(false);
            }
        });

        panel4.add(button, c);
        button=new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        panel4.add(button,c);
        getContentPane().add(panel4);

        //JLabel label=new JLabel("This is text");
        //getContentPane().add(label);

        setResizable(false);

        setBounds(400, 250, 350, 300);

        setVisible(true);



    }
}
