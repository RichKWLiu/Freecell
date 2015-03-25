package freecell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UIFreeSell extends JFrame
{
    // Variables
    private GameModel _model = new GameModel();
    private UICardPanel _boardDisplay;
    private JCheckBox _autoCompleteCB = new JCheckBox("Auto Complete");
    
    //Initialise the application and graphical user interface
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new UIFreeSell();
            }
        }
        );
    }
    
    // Define and initialise layout and appearance of application
    public UIFreeSell()
    {
        _boardDisplay = new UICardPanel(_model);
        
        // Create New Game button
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(new ActionNewGame());
        
        //Create Check Box for Auto-Complete
        _autoCompleteCB.setSelected(true);
        _autoCompleteCB.addActionListener(new ActionAutoComplete());
        _boardDisplay.setAutoCompletion(_autoCompleteCB.isSelected());
        
        // Set the JPanel layout to be a flow layout
        // Add Button and Check Box
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(newGameBtn);
        controlPanel.add(_autoCompleteCB);
        
        // Create content pane
        // Set JPanel layout to be Border layout
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(_boardDisplay, BorderLayout.CENTER);

        // Set contents for the window - title, visibility, resizability, closing of application
        setContentPane(content);
        setTitle("Free Cell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    // Action Listener when New Game is pressed
    class ActionNewGame implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            _model.reset();
        }
    }
    
    // Action Listener when Auto-Complete is pressed
    class ActionAutoComplete implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            _boardDisplay.setAutoCompletion(_autoCompleteCB.isSelected());
        }
    }
}
