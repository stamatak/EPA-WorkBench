package raxml_gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;


public class TabButton extends JButton implements ActionListener {
	private JTabbedPane _pane;
	private Job _tab;
	public TabButton(JTabbedPane parent, Job tab) {
		_pane = parent;
		_tab = tab;
		
		
		
		
		MouseListener buttonMouseListener = new MouseAdapter() {
	        public void mouseEntered(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(true);
	            }
	        }

	        public void mouseExited(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(false);
	            }
	        }
	    };
		
		
		int size = 10;
		setPreferredSize(new Dimension(size, size));
		setToolTipText("close this tab");
		//Make the button looks the same for all Laf's
		setUI(new BasicButtonUI());
		//Make it transparent
		setContentAreaFilled(false);
		//No need to be focusable
		setFocusable(false);
		setBorder(BorderFactory.createEtchedBorder());
		setBorderPainted(false);
		//Making nice rollover effect
		//we use the same listener for all buttons
		addMouseListener(buttonMouseListener);
		setRolloverEnabled(true);
		//Close the proper tab by clicking the button
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		int i = _pane.indexOfComponent(_tab);

		if (i != -1) {
			if (_tab.equals(_tab.getMainFrame().getWorkflowPanel().getUsableSubmissionPanel())){
				_tab.getMainFrame().getWorkflowPanel().setUsableSubmissionPanel(null);
				
			}
			boolean delete = _tab.abortSubmissionThread();
			if (delete){
				_pane.remove(i);
			}
		}
	}

	//we don't want to update UI for this button
	public void updateUI() {
	}

	//paint the cross
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		//shift the image for pressed buttons
		if (getModel().isPressed()) {
			g2.translate(1, 1);
		}
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.BLACK);
		if (getModel().isRollover()) {
			g2.setColor(Color.white);
		}
		int delta = 0;
		g2.drawRect(delta,delta, getWidth()-delta-1, getHeight()-delta-1);
		g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		g2.drawLine(getWidth() - delta, delta-1, delta-1, getHeight() - delta );
		g2.dispose();
	}
}


