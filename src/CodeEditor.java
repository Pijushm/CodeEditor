/**
 * Created by Pijush on 12/10/2015.
 */
/**
 * Created by Pijush on 11/27/2015.
 */
/**
 * Created by Pijush on 10/18/2015.
 */
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.*;

import static java.awt.Color.*;
import static java.awt.datatransfer.DataFlavor.stringFlavor;

public class CodeEditor extends JFrame {

    protected JEditorPane m_monitor;

    protected RTFEditorKit kit;
    protected StyleContext context=new StyleContext();


    protected JFileChooser m_chooser;
    protected SimpleFilter m_cFilter;
    protected SimpleFilter m_cppFilter;


    int fontsize;





    protected  JPanel panel;
    protected  JInternalFrame iframe;
    protected  JTextArea area;

    protected  ProcessBuilder pb;
    JPanel panel2;
    JLabel filename=new JLabel("                   Untitled");
    JTextArea lines=new JTextArea(" 1");

    String line=System.getProperty("line.separator");
    protected  JToolBar toolbar2;
   // Color f=new Color(47, 47,51);
    Color d=new Color(38, 140, 18);


    docColor doc=new docColor();



    protected static UndoManager undoManager = new UndoManager();
    final AttributeSet attred = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.RED);
    final AttributeSet attrBlack = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    final AttributeSet attrgreen=context.addAttribute(context.getEmptySet(),StyleConstants.Foreground,d);
    final AttributeSet attrblue=context.addAttribute(context.getEmptySet(),StyleConstants.Foreground,Color.blue);


    protected static UndoAction undoAction = new UndoAction();
    protected static Redoaction redoaction = new Redoaction();


    public CodeEditor() throws BadLocationException {
        super("C/C++ CodeEditor");
        Dimension d=getToolkit().getScreenSize();
        setSize(d);
        m_monitor = new JTextPane();
        m_monitor.grabFocus();




        m_monitor.setDocument(doc);
        m_monitor.setFont(new Font("Courier New", Font.PLAIN, 16));
        doc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                undoAction.update();
                redoaction.update();

            }
        });





        JScrollPane ps = new JScrollPane(m_monitor);




        m_chooser = new JFileChooser();
        m_chooser.setCurrentDirectory(new File("."));

        WindowListener wndcloser = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        WindowListener wndopener = new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                m_monitor.grabFocus();

            }
        };


        ;
        panel=new JPanel(new BorderLayout());
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));


        getContentPane().add(BorderLayout.CENTER, panel);

        GridBagConstraints c=new GridBagConstraints();



        c.insets = new Insets( 2, 2, 2, 2 );
        c.anchor = GridBagConstraints.WEST;



        toolbar2=new JToolBar();
        toolbar2.setPreferredSize(new Dimension(172, 32));

        panel.add(toolbar2,c);
        c.gridy=1;
        panel.add(filename, c);

        JScrollPane pane=new JScrollPane(m_monitor);
        pane.setPreferredSize(new Dimension(500, 400));
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridx=0;
        c.weightx=1;
        c.weighty=1;
        c.gridy=2;



        c.fill=GridBagConstraints.BOTH;
        panel.add(pane, c);
        iframe=new JInternalFrame("Logs");
        iframe.setClosable(false);
        iframe.setMaximizable(false);
        iframe.setResizable(true);
        iframe.setVisible(true);
        c.gridy=3;
        area=new JTextArea();
        JScrollPane sarea=new JScrollPane(area);
        sarea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        iframe.getContentPane().add(sarea);
        area.setBackground(WHITE);
        area.setForeground(BLUE);
        panel.add(iframe, c);


        lines.setEditable(false);
        lines.setPreferredSize(new Dimension(50, d.height));
        lines.setBackground(Color.lightGray);
        lines.setBorder(new EmptyBorder(2, 0, 0, 0));
        lines.setFont(new Font("Courier New",Font.PLAIN,16));


        m_monitor.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretposition = m_monitor.getDocument().getLength();
                Element root = m_monitor.getDocument().getDefaultRootElement();
                String text = " 1" + System.getProperty("line.separator");
                for (int i = 2; i < root.getElementIndex(caretposition) + 2; i++) {
                    text += " "+i + System.getProperty("line.separator");
                }
                return text;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lines.setText(getText());
            }
        });


        pane.setRowHeaderView(lines);


        JMenuBar menubar = createMenubar();
        setJMenuBar(menubar);




        m_chooser.setCurrentDirectory(new File("."));
        m_cFilter = new SimpleFilter("c","C files");
        m_cppFilter=new SimpleFilter("cpp","C++ files");

        m_chooser.setFileFilter(m_cppFilter);
        m_chooser.setFileFilter(m_cFilter);


        addWindowListener(wndcloser);
        addWindowListener(wndopener);


        //Keymap keymap = m_monitor.getKeymap();
        //KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false);
        //keymap.removeKeyStrokeBinding(keystroke);

        KeyStroke remove = KeyStroke.getKeyStroke("control V");

        InputMap im = m_monitor.getInputMap();
        im.put(remove, "none");




        setVisible(true);
    }

    public JMenuBar createMenubar() {
        JMenuBar menubar = new JMenuBar();
        menubar.setBorder(new BevelBorder(BevelBorder.RAISED));
        menubar.setBackground(Color.GRAY);


        JMenu file = new JMenu("File");
        file.setMnemonic('f');








        //ImageIcon new2icon = new ImageIcon("new.png");
        ImageIcon newicon=new ImageIcon(getClass().getResource("/images/new.png"));
        ImageIcon openicon=new ImageIcon(getClass().getResource("/images/open.png"));
        ImageIcon saveicon=new ImageIcon(getClass().getResource("/images/save.png"));
        ImageIcon copyicon=new ImageIcon(getClass().getResource("/images/copy.png"));
        ImageIcon cuticon=new ImageIcon(getClass().getResource("/images/cut.png"));
        ImageIcon pasteicon=new ImageIcon(getClass().getResource("/images/paste.png"));
        ImageIcon exiticon=new ImageIcon(getClass().getResource("/images/exit.png"));
        ImageIcon buildicon=new ImageIcon(getClass().getResource("/images/build.png"));
        ImageIcon buildrunicon=new ImageIcon(getClass().getResource("/images/buildrun.png"));
        ImageIcon runicon=new ImageIcon(getClass().getResource("/images/run.png"));
        ImageIcon undoicon=new ImageIcon(getClass().getResource("/images/undo.png"));
        ImageIcon redoicon=new ImageIcon(getClass().getResource("/images/redo.png"));
        ImageIcon fonticon=new ImageIcon(getClass().getResource("/images/font.png"));





        Action newaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    doc = new docColor();
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                m_monitor.setDocument(doc);
                doc.addUndoableEditListener(new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        undoManager.addEdit(e.getEdit());
                        undoAction.update();
                        redoaction.update();

                    }
                });


                filename.setText("                   Untitled");
                lines.setText(" 1");
                m_monitor.getDocument().addDocumentListener(new DocumentListener() {
                    public String getText() {
                        int caretposition = m_monitor.getDocument().getLength();
                        Element root = m_monitor.getDocument().getDefaultRootElement();
                        String text = " 1" + System.getProperty("line.separator");
                        for (int i = 2; i < root.getElementIndex(caretposition) + 2; i++) {
                            text +=" "+ i + System.getProperty("line.separator");
                        }
                        return text;
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        lines.setText(getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        lines.setText(getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        lines.setText(getText());
                    }
                });


            }
        };
        JMenuItem item = file.add(new JMenuItem("New",newicon));
        item.addActionListener(newaction);

        item.setMnemonic('n');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK, false));



        item = file.add(new JMenuItem("Open",openicon)) ;
        Action openaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_chooser.showOpenDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                    return;
                File file = m_chooser.getSelectedFile();
                if(file.getName().endsWith(".c")||file.getName().endsWith(".cpp"))
                {
                    try {
                        FileReader in = new FileReader(file);
                        //InputStream in = new FileInputStream(file);
                        JTextPane a=new JTextPane();
                        a.read(in, file);

                        docColor p=new docColor();
                        p.insertString(0,a.getText(),null);
                        p.listen(1);

                        m_monitor.setDocument(p);
                        p.addUndoableEditListener(new UndoableEditListener() {
                            @Override
                            public void undoableEditHappened(UndoableEditEvent e) {
                                undoManager.addEdit(e.getEdit());
                                undoAction.update();
                                redoaction.update();

                            }
                        });

                        in.close();



                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    filename.setText("                "+file.getName());
                    area.setText("");

                    m_monitor.getDocument().addDocumentListener(new DocumentListener() {
                        public String getText() {
                            int caretposition = m_monitor.getDocument().getLength();
                            Element root = m_monitor.getDocument().getDefaultRootElement();
                            String text = " 1" + System.getProperty("line.separator");
                            for (int i = 2; i < root.getElementIndex(caretposition) + 2; i++) {
                                text += " "+i + System.getProperty("line.separator");
                            }
                            return text;
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            lines.setText(getText());
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            lines.setText(getText());
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            lines.setText(getText());
                        }
                    });

                }
                else
                {
                    JOptionPane.showMessageDialog(CodeEditor.this,"Not a C or C++ file","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }

        };
        item.addActionListener(openaction);

        item.setMnemonic('o');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK, false));



        item=file.add(new JMenuItem("Save",saveicon));
        Action saveaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_chooser.showSaveDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                    return;


                try {
                    FileWriter out=new FileWriter(m_chooser.getSelectedFile());
                    m_monitor.write(out);
                    out.close();
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                filename.setText("                "+m_chooser.getSelectedFile().getName());


            }
        };
        item.addActionListener(saveaction);
        item.setMnemonic('s');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK, false));


        Action exitaction = new AbstractAction("Exit",exiticon) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        file.addSeparator();
        item = file.add(exitaction);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK, false));
        item.setMnemonic('x');


        JButton newt = new JButton(newicon);


        newt.addActionListener(newaction);

        newt.setFocusPainted(false);
        newt.setRequestFocusEnabled(false);
        newt.setToolTipText("New Document");

        JButton opent = new JButton(openicon);

        opent.addActionListener(openaction);
        opent.setToolTipText("Open Document");
        opent.setFocusPainted(false);



        JButton savet = new JButton(saveicon);
        savet.addActionListener(saveaction);
        savet.setToolTipText("Save Document");


        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.gray);


        toolBar.add(newt);


        toolBar.add(opent);
        toolBar.add(savet);







        JMenu Edit = new JMenu("Edit");
        JMenuItem copy = new JMenuItem("Copy",copyicon);
        Action copyaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = m_monitor.getSelectedText();
                StringSelection ss = new StringSelection(s);
                getToolkit().getSystemClipboard().setContents(ss, null);
            }
        };
        copy.addActionListener(copyaction);


        copy.setMnemonic('C');

        Edit.add(copy);
        JButton copyt=new JButton(copyicon);
        copyt.addActionListener(copyaction);
        copyt.setToolTipText("Copy");
        toolBar.add(copyt);

        JMenuItem cut = new JMenuItem("Cut",cuticon);
        Action cutaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = m_monitor.getSelectedText();

                StringSelection ss = new StringSelection(s);
                getToolkit().getSystemClipboard().setContents(ss, null);
                m_monitor.replaceSelection("");
            }
        };
        cut.addActionListener(cutaction);
        cut.setMnemonic('x');
        Edit.add(cut);

        JButton cutt = new JButton(cuticon);
        cutt.addActionListener(cutaction);
        cutt.setToolTipText("Cut");
        toolBar.add(cutt);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK, false));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK, false));
        copy.setEnabled(false);
        copyt.setEnabled(false);
        cut.setEnabled(false);
        cutt.setEnabled(false);


        CaretListener cst = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                int start = m_monitor.getSelectionStart();
                int endx = m_monitor.getSelectionEnd();
                if (start == endx) {
                    copy.setEnabled(false);
                    copyt.setEnabled(false);
                    cut.setEnabled(false);
                    cutt.setEnabled(false);
                } else {
                    copy.setEnabled(true);
                    copyt.setEnabled(true);
                    cut.setEnabled(true);
                    cutt.setEnabled(true);
                }
            }
        };

        m_monitor.addCaretListener(cst);

        JMenuItem paste=new JMenuItem("Paste",pasteicon);

        paste.setMnemonic('v');



        Action pasteaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Transferable t = getToolkit().getSystemClipboard().getContents(null);
                try {


                    if (t != null && t.isDataFlavorSupported(stringFlavor)) {
                        String copytext = (String) t.getTransferData(stringFlavor);

                        //m_monitor.paste();
                        docColor d=new docColor();
                        m_monitor.getDocument().insertString(m_monitor.getSelectionStart(),copytext,null);
                        d= (docColor) m_monitor.getDocument();
                        d.insert();



                        //m_monitor.setDocument(doc);
                        //doc.insert();
                        m_monitor.getDocument().addDocumentListener(new DocumentListener() {
                            public String getText() {
                                int caretposition = m_monitor.getDocument().getLength();
                                Element root = m_monitor.getDocument().getDefaultRootElement();
                                String text = " 1" + System.getProperty("line.separator");
                                for (int i = 2; i < root.getElementIndex(caretposition) + 2; i++) {
                                    text += " " + i + System.getProperty("line.separator");
                                }
                                return text;
                            }

                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                lines.setText(getText());
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                lines.setText(getText());
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                lines.setText(getText());
                            }
                        });







                    }

                } catch (UnsupportedFlavorException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }

            }
        };


        paste.addActionListener(pasteaction);
        Edit.add(paste);

        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK, false));
        JButton pastet=new JButton(pasteicon);
        pastet.addActionListener(pasteaction);
        pastet.setToolTipText("Paste");

        toolBar.add(pastet);

        item = Edit.add(undoAction);
        item.setIcon(undoicon);
        item.setMnemonic('z');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK, false));
        item = Edit.add(redoaction);
        item.setIcon(redoicon);
        item.setMnemonic('z');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK, false));





        JMenu Build=  new JMenu("Build");
        JMenuItem build = new JMenuItem("Build",buildicon);
        Action buildaction =new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.setText("");
                //File file = m_chooser.getSelectedFile();


                if (!(filename.getText().endsWith(".c") || filename.getText().endsWith(".cpp")))

                {
                    if (m_chooser.showSaveDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                        return;
                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    filename.setText("                " + m_chooser.getSelectedFile().getName());
                } else {


                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                }

                String filepath = m_chooser.getSelectedFile().getPath();
                String filepath2 = filepath.substring(0, filepath.lastIndexOf(File.separator));

                String name = m_chooser.getSelectedFile().getName();
                String name2 = name.substring(0, name.lastIndexOf("."));

                try {
                    if (filename.getText().endsWith(".c"))
                        pb = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");
                    else if (filename.getText().endsWith(".cpp"))
                        pb = new ProcessBuilder("cmd", "/C", "g++ " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");


                    pb.directory(new File(filepath2));
                    Process p = pb.start();
                    p.waitFor();
                    int x = p.exitValue();

                    if (x == 0) {
                        area.setForeground(BLUE);
                        area.setText("            == Build Finished");
                    } else {

                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        //BufferedWriter rm=new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));


                        String out;
                        area.setText("");


                        while ((out = r.readLine()) != null)


                        {


                            area.setForeground(RED);
                            area.append(out + System.getProperty("line.separator"));
                        }


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        build.addActionListener(buildaction);





        Build.add(build);

        JMenuItem compile=new JMenuItem("Compile");

        Build.add(compile);
        Action compileaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.setText("");
                //File file = m_chooser.getSelectedFile();


                if (!(filename.getText().endsWith(".c") || filename.getText().endsWith(".cpp")))

                {
                    if (m_chooser.showSaveDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                        return;
                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    filename.setText("                " + m_chooser.getSelectedFile().getName());
                } else {


                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                }

                String filepath = m_chooser.getSelectedFile().getPath();
                String filepath2 = filepath.substring(0, filepath.lastIndexOf(File.separator));

                String name = m_chooser.getSelectedFile().getName();
                String name2 = name.substring(0, name.lastIndexOf("."));

                try {
                    if (filename.getText().endsWith(".c"))
                        pb = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");
                    else if (filename.getText().endsWith(".cpp"))
                        pb = new ProcessBuilder("cmd", "/C", "g++ " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");


                    pb.directory(new File(filepath2));
                    Process p = pb.start();
                    p.waitFor();
                    int x = p.exitValue();

                    if (x == 0) {
                        area.setForeground(d);
                        area.setText("            == 0 error.. Compilation Finished");
                    } else {

                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        //BufferedWriter rm=new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));


                        String out;
                        area.setText("");


                        while ((out = r.readLine()) != null)


                        {


                            area.setForeground(RED);
                            area.append(out + System.getProperty("line.separator"));
                        }


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        compile.addActionListener(compileaction);
        JMenuItem run=new JMenuItem("Run");
        run.setIcon(runicon);
        Action runaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                area.setText("          ");
                if (!(filename.getText().endsWith(".c") || filename.getText().endsWith(".cpp"))) {


                    if (!(JOptionPane.showConfirmDialog(CodeEditor.this, "File has not build..build now?", "Information", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                        return;
                    else {
                        if (m_chooser.showSaveDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                            return;
                        try {
                            FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                            m_monitor.write(out);
                            out.close();


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        filename.setText("                " + m_chooser.getSelectedFile().getName());
                    }
                } else {


                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                }


                String filepath = m_chooser.getSelectedFile().getPath();
                String filepath2 = filepath.substring(0, filepath.lastIndexOf(File.separator));
                String name = m_chooser.getSelectedFile().getName();
                String name2 = name.substring(0, name.lastIndexOf("."));

                try {
                    if (filename.getText().endsWith(".c"))
                        pb = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");
                    else if (filename.getText().endsWith(".cpp"))
                        pb = new ProcessBuilder("cmd", "/C", "g++ " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");


                    pb.directory(new File(filepath2));
                    Process p = pb.start();
                    p.waitFor();
                    int x = p.exitValue();

                    if (x == 0) {
                        Runtime rt = Runtime.getRuntime();
                        try {
                            String username = System.getProperty("user.name");
                            String c = "@echo off\n" + "\"" +
                                    filepath2 + "\\" + name2 + ".exe\"\n" + "echo.\n" + "echo.\n" + "echo Process finished\n" +
                                    "pause\n" +
                                    "exit";


                            File dir = new File("C:\\Users\\" + username + "\\CodeEditor");
                            dir.mkdir();

                            try {
                                File file2 = new File("C:\\Users\\" + username + "\\CodeEditor" + "\\run.bat");
                                file2.createNewFile();
                                PrintWriter writer = new PrintWriter(file2);
                                writer.println(c);
                                writer.close();


                                Process p2 = Runtime.getRuntime().exec("cmd /c start run.bat", null, new File("C:\\Users\\" + username + "\\CodeEditor"));
                            } catch (Exception ex) {

                            }


                        } catch (Exception ex) {

                        }

                    } else {

                        JOptionPane.showMessageDialog(CodeEditor.this, "Compilation Error", "Error", JOptionPane.ERROR_MESSAGE);

                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        };
        run.addActionListener(runaction);
        Build.add(run);
        JMenuItem comprun=new JMenuItem("Build and Run");
        comprun.setIcon(buildrunicon);
        Action buildrunaction=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = m_chooser.getSelectedFile();

                if (!(filename.getText().endsWith(".c") || filename.getText().endsWith(".cpp")))


                {
                    if (m_chooser.showSaveDialog(CodeEditor.this) != JFileChooser.APPROVE_OPTION)
                        return;
                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    filename.setText("                " + m_chooser.getSelectedFile().getName());
                } else {


                    try {
                        FileWriter out = new FileWriter(m_chooser.getSelectedFile());
                        m_monitor.write(out);
                        out.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                }

                String filepath = m_chooser.getSelectedFile().getPath();

                String filepath2 = filepath.substring(0, filepath.lastIndexOf(File.separator));

                String name = m_chooser.getSelectedFile().getName();
                String name2 = name.substring(0, name.lastIndexOf("."));

                try {
                    if (filename.getText().endsWith(".c"))
                        pb = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");
                    else if (filename.getText().endsWith(".cpp"))
                        pb = new ProcessBuilder("cmd", "/C", "g++ " + "\"" + filepath2 + "\\" + name + "\"" + " -o \"" + name2+"\"");


                    pb.directory(new File(filepath2));
                    Process p = pb.start();
                    p.waitFor();
                    int x = p.exitValue();

                    if (x == 0) {
                        area.setForeground(BLUE);
                        area.setText("          == Build Finished");


                        Runtime rt = Runtime.getRuntime();
                        try {
                            String username = System.getProperty("user.name");
                            String c = "@echo off\n" + "\"" +
                                    filepath2 + "\\" + name2 + ".exe\"\n" + "echo.\n" + "echo.\n" + "echo Process finished\n" +
                                    "pause\n" +
                                    "exit";


                            File dir = new File("C:\\Users\\" + username + "\\CodeEditor");

                            dir.mkdir();

                            try {
                                File file2 = new File("C:\\Users\\" + username + "\\CodeEditor" + "\\run.bat");
                                file2.createNewFile();
                                PrintWriter writer = new PrintWriter(file2);
                                writer.println(c);
                                writer.close();


                                Process p2 = Runtime.getRuntime().exec("cmd /c start run.bat", null, new File("C:\\Users\\" + username + "\\CodeEditor"));
                            } catch (Exception ex) {

                            }


                        } catch (Exception ex) {

                        }
                    } else {

                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        BufferedReader rm=new BufferedReader(new InputStreamReader(p.getInputStream()));


                        String out;
                        area.setText("");

                        while ((out = r.readLine()) != null)

                        {
                            area.setForeground(red);


                            area.append(out + System.getProperty("line.separator"));


                            //area.append(out);
                        }


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            }
        };
        comprun.addActionListener(buildrunaction);
        Build.add(comprun);


        JMenu Format=new JMenu("Format");
        JMenuItem FontSize=new JMenuItem("Font");
        FontSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          FontDialogue f=new FontDialogue(CodeEditor.this,m_monitor,lines);
            }
        });
        FontSize.setIcon(fonticon);

        Format.add(FontSize);


        JMenu Help=new JMenu("Help");

        JMenuItem about=new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new About(CodeEditor.this);
            }
        });
        Help.add(about);







        menubar.add(file);
        menubar.add(Edit);
        menubar.add(Build);
        menubar.add(Format);
        menubar.add(Help);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        JButton build2=new JButton(buildicon);
        build2.addActionListener(buildaction);
        build2.setToolTipText("Build");
        toolbar2.add(build2);
        JButton compile2=new JButton(runicon);
        compile2.addActionListener(runaction);
        compile2.setToolTipText("Run");
        toolbar2.add(compile2);
        JButton buildrun=new JButton(buildrunicon);
        buildrun.addActionListener(buildrunaction);
        buildrun.setToolTipText("Build and Run");
        toolbar2.add(buildrun);
        toolbar2.setOpaque(false);






        return menubar;
    }






    public static void main(String[] args) throws BadLocationException {
        new CodeEditor ();

    }
}

