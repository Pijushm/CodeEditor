/**
 * Created by Pijush on 12/6/2015.
 */
/**
 * Created by Pijush on 12/6/2015.
 */
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Pijush on 10/21/2015.
 */
class UndoRedo
{

}
class UndoAction extends AbstractAction
{
    public UndoAction()
    {
        super("Undo");
        setEnabled(false);

    }
    public void actionPerformed(ActionEvent e)
    {
        CodeEditor.undoManager.undo();
        CodeEditor.undoAction.update();
        CodeEditor.redoaction.update();

    }
    public void update()
    {
        setEnabled(CodeEditor.undoManager.canUndo());
    }
}
class Redoaction extends AbstractAction
{
    public Redoaction()
    {
        super("Redo");
        setEnabled(false);

    }
    public void actionPerformed(ActionEvent e)
    {
        CodeEditor.undoManager.redo();
        CodeEditor.redoaction.update();
        CodeEditor.undoAction.update();

    }
    public void update()
    {
        setEnabled(CodeEditor.undoManager.canRedo());
    }
}




