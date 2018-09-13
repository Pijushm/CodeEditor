/**
 * Created by Pijush on 12/10/2015.
 */
/**
 * Created by Pijush on 12/10/2015.
 */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class docColor extends DefaultStyledDocument {


    Color d = new Color(38, 140, 18);


    final StyleContext cont = StyleContext.getDefaultStyleContext();
    final AttributeSet attred = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
    final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    final AttributeSet attrgreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, d);
    final AttributeSet attrblue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
    int aps=0;int j = 0, k=0;

    public docColor() throws BadLocationException {
        insertString(0,"",attrBlack);
        listen(0);

    }

    public void listen(int p){

        if(p==0)
            return;
        else
            insert();
    }

    public void insert()
    {
        int p = super.getLength();
        try {


            String text = super.getText(0, super.getLength());
            for (int i = 0; i < super.getLength(); i++) {
                if (String.valueOf(text.charAt(i)).matches("(;|\\(|>|<|\\)|/|\\{|\\}|\\[|\\]|&|=|,|\")"))

                    super.setCharacterAttributes(i,1,attred,false);
                super.setCharacterAttributes(i+1,1,attrBlack,false);


                if (String.valueOf(text.charAt(i)).matches(" ")) {
                    int before = findLastNonWordChar(text,i);



                    if (before < 0) before = 0;
                    int after = findFirstNonWordChar(text, i-1);

                    int wordL = before;
                    int wordR = before;


                    while (wordR <= after) {


                        if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                            if (text.substring(wordL, wordR).matches("\\W|")) {
                                if (text.substring(wordL, wordR).matches("(\\s)*(\")|(\\w)*(\")|>"))
                                    return;
                                else

                                    super.setCharacterAttributes(wordL, wordR - wordL, attred, false);
                            } else if (text.substring(wordL, wordR).matches("(\\s)*(#include|std|cout|cin|)"))// #include| std| cout| cin|\ncin)"))
                            {
                                super.setCharacterAttributes(wordL, wordR - wordL, attrgreen, false);
                                ;
                            } else if (text.substring(wordL, wordR).matches("(\\s)*(int|double|if|else|while|switch|using|namespace|\")")) {
                                super.setCharacterAttributes(wordL, wordR - wordL, attrblue, false);
                                ;
                            }      //checking every word
                            else
                                super.setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                            wordL = wordR;
                        }
                        wordR++;
                    }
                }
            }

        } catch (BadLocationException e2) {
            e2.printStackTrace();
        }
    }




    public void insertString(int offset,String str,AttributeSet atr) throws BadLocationException {
        super.insertString(offset, str, atr);


        int p = super.getLength();

            if(str.matches("(\\W)"))
                setCharacterAttributes(offset, 2, attred, false);


        if(str.matches("\\w"))
            super.setCharacterAttributes(offset, 2, attrBlack, false);


            String text = super.getText(0, super.getLength());

           int before = findLastNonWordChar(text, offset);
            if (before < 0) before = 0;
            int after = findFirstNonWordChar(text, offset + str.length());

            int wordL = before;
            int wordR = before;


            while (wordR <= after) {


                if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

                    if (text.substring(wordL, wordR).matches("(\\s)*(#include|std|cout|cin|)"))// #include| std| cout| cin|\ncin)"))
                    {
                        super.setCharacterAttributes(wordL, wordR - wordL, attrgreen, false);
                        ;
                    } else if (text.substring(wordL, wordR).matches("(\\s)*(int|double|if|else|while|switch|using|namespace|\")")) {
                        super.setCharacterAttributes(wordL, wordR - wordL, attrblue, false);

                    }      //checking every word
                    //else
                        //super.setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                    wordL = wordR;
                }
                wordR++;
            }
        }

    public void remove(int offs,int len)throws BadLocationException {
        super.remove(offs, len);



        String text = super.getText(0, super.getLength());






        int before = findLastNonWordChar(text, offs);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offs);

        int wordL = before;
        int wordR = before;


        while (wordR <= after) {


            if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                if (text.substring(wordL, wordR).matches("\\W|")) {
                    if (text.substring(wordL, wordR-1).matches("(\\s)*(\")|(\\w)*(\")|>"))
                        return;
                    else

                        super.setCharacterAttributes(wordL, wordR - wordL, attred, false);
                } else if (text.substring(wordL, wordR).matches("(\\s)*(#include|std|cout|cin|)"))// #include| std| cout| cin|\ncin)"))
                {
                    super.setCharacterAttributes(wordL, wordR - wordL, attrgreen, false);
                    ;
                } else if (text.substring(wordL, wordR).matches("(\\s)*(int|double|if|else|while|switch|using|namespace|\")")) {
                    super.setCharacterAttributes(wordL, wordR - wordL, attrblue, false);

                }      //checking every word
                else
                    super.setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                wordL = wordR;
            }
            wordR++;
        }
    }








    public int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {

            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }

        return index;
    }

    public int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }



}





