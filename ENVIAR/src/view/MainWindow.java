package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import exceptions.SintaxException;

public class MainWindow {

	private JFrame frmEnviar;
	private JList<String> pathList;
	private DefaultListModel<String> listModel;
	private JTable speedTable;
	private DefaultTableModel dtm;
	private JPanel pathPanel;
	
	private JCheckBox gpsSignalCheckBox;
	private JCheckBox gpsCalibrationCheckBox;
	private JCheckBox takePictureCheckBox;
	private JCheckBox orientationCheckBox;
	private JCheckBox callCheckBox;
	private JCheckBox longBackgroundCheckBox;
	private JCheckBox internetConnectionCheckBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmEnviar.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEnviar = new JFrame();
		frmEnviar.setTitle("ENVIAR");
		frmEnviar.setResizable(false);
		frmEnviar.setBounds(100, 100, 565, 573);
		frmEnviar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEnviar.getContentPane().setLayout(null);		
		pathPanel = new JPanel();
		pathPanel.setBounds(10, 38, 100, 139);
		frmEnviar.getContentPane().add(pathPanel);
		pathPanel.setLayout(null);
		
		JLabel lblPath = new JLabel("Path");
		lblPath.setBounds(0, 0, 37, 22);
		pathPanel.add(lblPath);
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		listModel = new DefaultListModel<>();
		pathList = new JList<String>(listModel);
		pathList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				removerTodasLinhasTabela();
				if(!pathList.isSelectionEmpty()) {
					ArrayList<String> selectedPaths = (ArrayList<String>) pathList.getSelectedValuesList();
					for (String path : selectedPaths) {
						String[] newRow = {path, ""};
						dtm.addRow(newRow);
					}					
				}
			}
		});
		pathList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() > 1) {
		        	PathEditorWindow pew = new PathEditorWindow();
		        	try {
						pew.setPath((String)list.getSelectedValue());
						pew.setVisible(true);
					} catch (SintaxException | IOException e) {
						JOptionPane.showMessageDialog(pathPanel, "The path could not be loaded", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
		        } 
		    }
		});
		pathList.setBounds(10, 43, 100, 80);
		pathList.setBorder(new LineBorder(new Color(0, 0, 0)));
		loadPathList();
		
		JScrollPane pathScrollPane = new JScrollPane();
		pathScrollPane.setBounds(0, 24, 100, 81);
		pathPanel.add(pathScrollPane);
		pathScrollPane.setViewportView(pathList);
		
		JButton addPathBtn = new JButton("+");
		addPathBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathEditorWindow pew = new PathEditorWindow();
				pew.setMainWindow(MainWindow.this);
				pew.setVisible(true);
			}
		});
		addPathBtn.setBounds(0, 115, 50, 21);
		pathPanel.add(addPathBtn);
		
		JButton removePathBtn = new JButton("-");
		removePathBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<String> selectedPaths = (ArrayList<String>) pathList.getSelectedValuesList();

				if(selectedPaths.size() == 0) {
					JOptionPane.showMessageDialog(pathPanel, "No selected item", "Attention", JOptionPane.WARNING_MESSAGE);
				} else {
					int option = JOptionPane.showConfirmDialog(pathPanel, "Do you really want to remove the path(s)?", "Attention", JOptionPane.WARNING_MESSAGE);
					if(option == 0) {
						for (String path : selectedPaths) {
							File pathFile = new File("paths/" + path + ".txt");
							pathFile.delete();
						}
						removerTodasLinhasTabela();
						loadPathList();							
					}
				}
			}
		});
		removePathBtn.setBounds(50, 115, 50, 21);
		pathPanel.add(removePathBtn);
		
		JPanel eventsPanel = new JPanel();
		eventsPanel.setBounds(120, 38, 172, 184);
		frmEnviar.getContentPane().add(eventsPanel);
		eventsPanel.setLayout(null);
		
		JLabel lblEvents = new JLabel("Events");
		lblEvents.setBounds(0, 0, 77, 22);
		lblEvents.setFont(new Font("Tahoma", Font.PLAIN, 18));
		eventsPanel.add(lblEvents);
		
		gpsSignalCheckBox = new JCheckBox("GPS Signal");
		gpsSignalCheckBox.setSelected(true);
		gpsSignalCheckBox.setBounds(0, 22, 166, 21);
		eventsPanel.add(gpsSignalCheckBox);
		
		longBackgroundCheckBox = new JCheckBox("Long Background");
		longBackgroundCheckBox.setSelected(true);
		longBackgroundCheckBox.setBounds(0, 137, 166, 21);
		eventsPanel.add(longBackgroundCheckBox);
		
		takePictureCheckBox = new JCheckBox("Take a Picture");
		takePictureCheckBox.setSelected(true);
		takePictureCheckBox.setBounds(0, 68, 166, 21);
		eventsPanel.add(takePictureCheckBox);
		
		orientationCheckBox = new JCheckBox("Orientation");
		orientationCheckBox.setSelected(true);
		orientationCheckBox.setBounds(0, 91, 166, 21);
		eventsPanel.add(orientationCheckBox);
		
		callCheckBox = new JCheckBox("Call");
		callCheckBox.setSelected(true);
		callCheckBox.setBounds(0, 114, 166, 21);
		eventsPanel.add(callCheckBox);
		
		gpsCalibrationCheckBox = new JCheckBox("GPS Calibration");
		gpsCalibrationCheckBox.setSelected(true);
		gpsCalibrationCheckBox.setBounds(0, 45, 166, 21);
		eventsPanel.add(gpsCalibrationCheckBox);
		
		internetConnectionCheckBox = new JCheckBox("Internet Connection");
		internetConnectionCheckBox.setSelected(true);
		internetConnectionCheckBox.setBounds(0, 160, 166, 21);
		eventsPanel.add(internetConnectionCheckBox);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 277, 541, 92);
		frmEnviar.getContentPane().add(separator);
		
		JLabel lblTestSuitCreation = new JLabel("Test Suite Creation");
		lblTestSuitCreation.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestSuitCreation.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuitCreation.setBounds(10, 10, 541, 22);
		frmEnviar.getContentPane().add(lblTestSuitCreation);
		
		JPanel panel = new JPanel();
		panel.setBounds(302, 42, 225, 180);
		frmEnviar.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 31, 205, 139);
		panel.add(scrollPane);
		
		String[] columnNames = {"Selected Paths", "Max Speed"};
		dtm = new DefaultTableModel();
		dtm.setColumnIdentifiers(columnNames);
		
		speedTable = new JTable(dtm);
		scrollPane.setViewportView(speedTable);
		
		
		
		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSpeed.setBounds(0, 0, 77, 22);
		panel.add(lblSpeed);
		
		JButton btnCreateTestSuite = new JButton("Create Test Suite");
		btnCreateTestSuite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTestSuite();
			}
		});
		btnCreateTestSuite.setBounds(210, 232, 144, 35);
		frmEnviar.getContentPane().add(btnCreateTestSuite);
	}
	
	private void createTestSuite() {
		if(pathList.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(pathPanel, "Select at least one path", "Attention", JOptionPane.WARNING_MESSAGE);
		}else if(eventsSelected() == 0){
			JOptionPane.showMessageDialog(pathPanel, "Select at least one event", "Attention", JOptionPane.WARNING_MESSAGE);
		}else if(!allSpeedsInformed()) {
			JOptionPane.showMessageDialog(pathPanel, "Please, inform all path speeds", "Attention", JOptionPane.WARNING_MESSAGE);
		}else {
			
		}
		
	}

	private boolean allSpeedsInformed() {
		int qteRows = dtm.getRowCount();
		for (int i = 0; i < qteRows; i++) {
			String speedStr = (String) dtm.getValueAt(i, 1);
			try {
				int speed = Integer.valueOf(speedStr);				
			}catch(Exception e) {
				return false;
			}
		}
		return true;
	}

	private int eventsSelected() {
		int selected = 0;
		if (gpsSignalCheckBox.isSelected()) {
			selected++;
		}
		if (gpsCalibrationCheckBox.isSelected()) {
			selected++;
		}
		if (takePictureCheckBox.isSelected()) {
			selected++;
		}
		if (orientationCheckBox.isSelected()) {
			selected++;
		}
		if (callCheckBox.isSelected()) {
			selected++;
		}
		if (longBackgroundCheckBox.isSelected()) {
			selected++;
		}
		if (internetConnectionCheckBox.isSelected()) {
			selected++;
		}
		return selected;
	}

	public void loadPathList() {
		if(listModel != null) {
			listModel.removeAllElements();
		}
		File pathsDirectory = new File("paths/");
		File[] paths = pathsDirectory.listFiles();
		for (File file : paths) {
			String fileName = file.getName().substring(0, file.getName().length() - 4);
			listModel.addElement(fileName);
		}
		
	}

	private void removerTodasLinhasTabela() {
		while(dtm.getRowCount() > 0) {
			dtm.removeRow(0);
		}
	}
}
