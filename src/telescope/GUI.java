package telescope;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import telescope.connection.SerialUSB;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class GUI implements ItemListener {

	private JLabel currentXLabel;

	private JLabel currentYLabel;

	private JTextField gotoXField;

	private JTextField gotoYField;

	private Data data;

	private JComboBox<Object> telescopeCombobox;

	private Button gotoButton;

	private Button refreshButton;

	private SerialUSB usbTelescope;
	
	public GUI(final Data data, SerialUSB usbTelescope) {
		this.data = data;
		this.usbTelescope = usbTelescope;

		telescopeCombobox = new JComboBox<>();
		telescopeCombobox.addItemListener(this);
		
		refreshButton = new Button("Refresh ports");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCom();
			}
		});
		
		currentXLabel = new JLabel();
		currentYLabel = new JLabel();
		gotoXField = new JTextField();
		gotoYField = new JTextField();
		gotoButton = new Button("GOTO");
		
		gotoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoClicked(e);
			}
		});
		
		JFrame frame = new JFrame("Telescope controller");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				data.getTelescope().close();
			};
		});
		
		JPanel panel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		
		panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        panel.add(refreshButton, gbc);
        panel.add(telescopeCombobox, gbc);
        
		panel.add(currentXLabel, gbc);
		panel.add(currentYLabel, gbc);
		panel.add(gotoXField, gbc);
		panel.add(gotoYField, gbc);
		panel.add(gotoButton, gbc);
		
		gbc.weighty = 1;
		panel.add(Box.createVerticalGlue(), gbc);
		panel.setMinimumSize(new Dimension(500, 0));
		
		gotoXField.setAlignmentX(Component.CENTER_ALIGNMENT);
		gotoYField.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		frame.add(panel, BorderLayout.WEST);
		
		frame.setSize(600, 400);
		
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );        
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );
        frame.add(glcanvas, BorderLayout.CENTER);
        
        glcanvas.addMouseMotionListener(new MouseAdapter() {
        	@Override
        	public void mouseDragged(MouseEvent e) {
        		//TODO stuff...
        		System.out.println(e);
        	}
        });
        
        
        FPSAnimator animator = new FPSAnimator(glcanvas, 60);
        
        glcanvas.addGLEventListener( new GLEventListener() {
			@Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
				GL2 gl2 = glautodrawable.getGL().getGL2();
				
				gl2.glViewport(0, 0, width, height);
				
				gl2.glMatrixMode(GL2.GL_PROJECTION);
				gl2.glLoadIdentity();
				new GLU().gluPerspective(90, (double) width / height, 0.1, 100);
				/*
				gl2.glRotatef(rotationX, 1, 0, 0);
				gl2.glRotatef(rotationY, 0, 1, 0);
				gl2.glRotatef(rotationZ, 0, 0, 1);
				gl2.glTranslatef(positionX, positionY, positionZ);
				*/
				
				gl2.glMatrixMode(GL2.GL_MODELVIEW);
				gl2.glLoadIdentity();
				
				gl2.glPointSize(2);
				gl2.glEnable(GL.GL_BLEND);
				gl2.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            }
            
            @Override
            public void init( GLAutoDrawable glautodrawable ) {
            	//texture1 = TextureIO.newTexture(new File("mustang_shelby-wallpaper-1440x900.jpg"), false);
            	
            }
            
            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            }
            
            @Override
            public void display( GLAutoDrawable gl) {
            	GL2 gl2 = gl.getGL().getGL2();
            	
            	//clear window
            	gl2.glClearColor(0.2f, 0.3f, 0.4f, 1);
            	gl2.glClear( GL.GL_COLOR_BUFFER_BIT  | GL.GL_DEPTH_BUFFER_BIT);
                
            	gl2.glBegin(GL2.GL_POINTS);
            	gl2.glColor4f(0, 1, 0, 0.9f);
            	gl2.glVertex3f(0, 0, -1);
            	gl2.glEnd();
                
            }
        });
		
		setLabels();
		

		frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		refreshCom();
	}

	private void refreshCom() {
		telescopeCombobox.removeAllItems();
		telescopeCombobox.addItem(null);
		
		Enumeration<CommPortIdentifier> comPorts = usbTelescope.getComPorts();
		while (comPorts.hasMoreElements()) {
			CommPortIdentifier comPort = comPorts.nextElement();
			if (comPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				telescopeCombobox.addItem(comPort.getName());						
			}
		}
	}
	
	public void gotoClicked(ActionEvent event) {
		try {
			data.setGotoX(Integer.parseInt(gotoXField.getText()));
			data.setGotoY(Integer.parseInt(gotoYField.getText()));
			
			data.sendToTelescope();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void setLabels() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				currentXLabel.setText("Current X: " + data.getTelescope().getCurrentX());
				currentYLabel.setText("Current Y: " + data.getTelescope().getCurrentY());					
			}
		});
	}
	
	public void setGotoLabels() {
		if (!(""+data.getGotoX()).equals(gotoXField.getText())) {
			gotoXField.setText(""+data.getGotoX());
		}
		if (!(""+data.getGotoY()).equals(gotoYField.getText())) {
			gotoYField.setText(""+data.getGotoY());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		usbTelescope.disconnect();
		
		String selectedComPort = (String) e.getItem();
		if (selectedComPort!=null) {
			try {
				usbTelescope.connect(selectedComPort);
			} catch (Exception e1) {
				throw new Error(e1);
			}
		}
	}
	
}
