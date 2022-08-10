package guidemo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A frame that displays a multiline text, possibly with a background image
 * and with added icon images, in a DrawPanel, along with a variety of controlls.
 */
public class GuiDemo extends JFrame{
	
	/**
	 * The main program just creates a GuiDemo frame and makes it visible.
	 */
	public static void main(String[] args){
		JFrame frame = new GuiDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private DrawPanel drawPanel;
	private SimpleFileChooser fileChooser;
	private TextMenu textMenu;
	private JCheckBoxMenuItem gradientOverlayCheckbox = new JCheckBoxMenuItem("Gradient Overlay", true);
	
	/**
	 * The constructor creates the frame, sizes it, and centers it horizontally on the screen.
	 */
	public GuiDemo() {
		
		super("Final Assignment");  
		// This is my window title.
		
		JPanel content = new JPanel();  
		// This will hold the content of the window.
		content.setBackground(Color.LIGHT_GRAY);
		content.setLayout(new BorderLayout());
		setContentPane(content);
		
		// Lets Create the DrawPanel 
		// which fills most of the window, and customize it.
		
		drawPanel = new DrawPanel();
		drawPanel.getTextItem().setText(
				"Big bugs have little bugs\n" +
						"      Upon their backs to bite 'em,\n" +
						"And little bugs have littler bugs,\n" +
						"      And so it goes, ad infinitum."
			);
		drawPanel.getTextItem().setLineHeightMultiplier(1.5);
		drawPanel.getTextItem().setFontSize(36);
		drawPanel.getTextItem().setColor(Color.BLUE);
		drawPanel.getTextItem().setJustify(TextItem.LEFT);
		drawPanel.setBackgroundImage(Util.getImageResource("resources/images/cloud.jpeg"));
		content.add(drawPanel, BorderLayout.CENTER);
		
		// Lets create an icon tool bar to the SOUTH position of the layout
		// This will help us to immediately change the background of canvas.
		
		IconSupport iconSupport = new IconSupport(drawPanel);
		content.add( iconSupport.createToolbar(true), BorderLayout.SOUTH );
		content.add(makeToolbar(), BorderLayout.NORTH);
		
		// Now we need to add a menu bar and plug it to the frame.  
		// we will define The TextMenu with a separate class.  
		// all other menu and their items are created in this class.
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(makeFileMenu());
		textMenu = new TextMenu(drawPanel);
		menuBar.add(textMenu );
		menuBar.add( makeBackgroundMenu() );
		menuBar.add(iconSupport.createMenu());
		setJMenuBar(menuBar);
		
		// Define the size of the window and its position.
		
		pack();  
		// we should Size the window to fit its content.
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth())/2, 50);
		
		// Let's create the file chooser and customise it
		
		fileChooser = new SimpleFileChooser();
		try { 
			// I want to use the Desktop folder as the root folder in the file chooser.
			
			String userDir = System.getProperty("user.home");
			if (userDir != null) {
				File desktop = new File(userDir,"Desktop");
				if (desktop.isDirectory())
					fileChooser.setDefaultDirectory(desktop);
			}
		}
		catch (Exception e) {
		}

	} // the menu constructor ends here. 
	
	
	/**
	 * We will create menu in this section.
	 * 
	 * Create the "File" menu from actions that are defined later in this class.
	 */
	private JMenu makeFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(newPictureAction);
		menu.add(saveImageAction);
		menu.addSeparator();
		menu.add(quitAction);
		return menu;
	}
	
	/**
	 * Create the "Background" menu, using objects of type ChooseBackgroundAction,
	 * We will add an individual class for this file.
	 */
	private JMenu makeBackgroundMenu() {
		JMenu menu = new JMenu("Background");
		menu.add(new ChooseBackgroundAction("Cats"));
		menu.add(new ChooseBackgroundAction("Mandelbrot"));
		menu.add(new ChooseBackgroundAction("Earthrise"));
		menu.add(new ChooseBackgroundAction("Sunset"));
		menu.add(new ChooseBackgroundAction("Cloud"));
		menu.add(new ChooseBackgroundAction("Eagle_nebula"));
		menu.addSeparator();
		menu.add(new ChooseBackgroundAction("Custom..."));
		menu.addSeparator();
		// This will allow users to set custom background 
		menu.add(new ChooseBackgroundAction("Color..."));
		menu.addSeparator();
		menu.add(gradientOverlayCheckbox);
		gradientOverlayCheckbox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (gradientOverlayCheckbox.isSelected())
					drawPanel.setGradientOverlayColor(Color.WHITE);
				else
					drawPanel.setGradientOverlayColor(null);
			}
		});
		return menu;
	}
	
	private JToolBar makeToolbar() {
		JToolBar toolBar = new JToolBar("Tool Bar");
		toolBar.add(newPictureAction);
		toolBar.add(saveImageAction);
		toolBar.addSeparator();
		toolBar.add(new ChooseBackgroundAction("Cats"));
		toolBar.add(new ChooseBackgroundAction("Mandelbrot"));
		toolBar.add(new ChooseBackgroundAction("Earthrise"));
		toolBar.add(new ChooseBackgroundAction("Sunset"));
		toolBar.add(new ChooseBackgroundAction("Cloud"));
		toolBar.add(new ChooseBackgroundAction("Eagle_nebula"));
		toolBar.addSeparator();
		toolBar.add(new ChooseBackgroundAction("Custom..."));
		toolBar.addSeparator();
		toolBar.add(new ChooseBackgroundAction("Color..."));
		return toolBar;
	}
	
	private AbstractAction newPictureAction = 
		new AbstractAction("New", Util.iconFromResource("resources/action_icons/fileopen.png")) {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
		    	drawPanel.clear();
		    	gradientOverlayCheckbox.setSelected(true);
		    	textMenu.setDefaults();
		    }
		};
	
	private AbstractAction quitAction = 
		new AbstractAction("Exit", Util.iconFromResource("resources/action_icons/exit.png")) {
		    public void actionPerformed(ActionEvent evt) {
		    	System.exit(0);
		    }
		};
		
	private AbstractAction saveImageAction = 
		new AbstractAction("Save Image...", Util.iconFromResource("resources/action_icons/filesave.png")) {
		    public void actionPerformed(ActionEvent evt) {
		    	File f = fileChooser.getOutputFile(drawPanel, "Select Ouput File", "Output.jpeg");
		    	if (f != null) {
		    		try {
		    			BufferedImage img = drawPanel.copyImage();
		    			String format;
		    			String fileName = f.getName().toLowerCase();
		    			if (fileName.endsWith(".png"))
		    				format = "PNG";
		    			else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg"))
		    				format = "JPEG";
		    			else {
		    				JOptionPane.showMessageDialog(drawPanel,
		    						"The output file extension must be wth\n.png or .jpeg.");
		    				return;
		    			}
		    			ImageIO.write(img,format,f);
		    		}
		    		catch (Exception e) {
		    			JOptionPane.showMessageDialog(drawPanel, "Sorry, problem occured while saving the image.");
		    		}
		    	}
		    }
    	};
    	
    	
    /**
     * An object of type ChooseBackgroudnAction represents an action through which the
     * user selects the background of the picture.  There are three types of background:
     * solid color background ("Color..." command), an image selected by the user from 
     * the file system ("Custom..." command), and four built-in image resources
     * (Mandelbrot, Earthrise, Sunset, and Eagle_nebula).
     */
    private class ChooseBackgroundAction extends AbstractAction {
    	String text;
    	ChooseBackgroundAction(String text) {
    		super(text);
    		this.text = text;
    		if (!text.equals("Custom...") && !text.equals("Color...")) {
    			putValue(Action.SMALL_ICON, 
    					Util.iconFromResource("resources/images/" + text.toLowerCase() + "_thumbnail.jpeg"));
    		}
    		if (text.equals("Color...")) {
    			BufferedImage colorIcon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
     			Graphics g = colorIcon.createGraphics();
     			g.setColor(Color.GRAY);
     			g.fillRect(0, 0, 32, 32);
    			g.setColor(Color.RED);
    			g.fillRect(2, 2, 9, 28);
    			g.setColor(Color.GREEN);
    			g.fillRect(11, 2, 9, 28);
    			g.setColor(Color.BLUE);
    			g.fillRect(20, 2, 9, 28);
    			g.dispose();
    			
    			putValue(Action.SMALL_ICON, new ImageIcon(colorIcon));
    			putValue(Action.SHORT_DESCRIPTION, "<html>You can use a solid color for background<br>instead of an image.</html>");
    		}
    		else if (text.equals("Custom...")) {
    			putValue(Action.SMALL_ICON,
    					Util.iconFromResource("resources/action_icons/fileopen.png"));
    			putValue(Action.SHORT_DESCRIPTION, "<html>Please select an image file<br>to use as the background.</html>");
    		}
    		else
    			putValue(Action.SHORT_DESCRIPTION, "Use this image as the background.");
    			
    	}
		public void actionPerformed(ActionEvent evt) {
			if (text.equals("Custom...")) {
				File inputFile = fileChooser.getInputFile(drawPanel, "Select custom Background Image");
				if (inputFile != null) {
					try {
						BufferedImage img = ImageIO.read(inputFile);
						if (img == null)
							throw new Exception();
						drawPanel.setBackgroundImage(img);
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(drawPanel, "Sorry, error reading the file.");
					}
				}
			}
			else if (text.equals("Color...")) {
				Color c = JColorChooser.showDialog(drawPanel, "Select Color for Background", drawPanel.getBackground());
				if (c != null) {
					drawPanel.setBackground(c);
					drawPanel.setBackgroundImage(null);
				}
			}
			else {
				Image bg = Util.getImageResource("resources/images/" + text.toLowerCase() + ".jpeg");
				drawPanel.setBackgroundImage(bg);
			}
		}
    }

}
