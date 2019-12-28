package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import exceptions.SintaxException;
import testCase.TestSuiteGenerator;

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

	private JRadioButton order1RadioButton;
	private JRadioButton order2RadioButton;
	private JRadioButton order3RadioButton;
	private JRadioButton order4RadioButton;
	private JRadioButton order5RadioButton;
	
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
		frmEnviar.setBounds(100, 100, 642, 573);
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
		gpsSignalCheckBox.setEnabled(false);
		gpsSignalCheckBox.setSelected(true);
		gpsSignalCheckBox.setBounds(0, 22, 166, 21);
		eventsPanel.add(gpsSignalCheckBox);
		
		longBackgroundCheckBox = new JCheckBox("Long Background");
		longBackgroundCheckBox.setEnabled(false);
		longBackgroundCheckBox.setSelected(true);
		longBackgroundCheckBox.setBounds(0, 137, 166, 21);
		eventsPanel.add(longBackgroundCheckBox);
		
		takePictureCheckBox = new JCheckBox("Take a Picture");
		takePictureCheckBox.setEnabled(false);
		takePictureCheckBox.setSelected(true);
		takePictureCheckBox.setBounds(0, 68, 166, 21);
		eventsPanel.add(takePictureCheckBox);
		
		orientationCheckBox = new JCheckBox("Orientation");
		orientationCheckBox.setEnabled(false);
		orientationCheckBox.setSelected(true);
		orientationCheckBox.setBounds(0, 91, 166, 21);
		eventsPanel.add(orientationCheckBox);
		
		callCheckBox = new JCheckBox("Call");
		callCheckBox.setEnabled(false);
		callCheckBox.setSelected(true);
		callCheckBox.setBounds(0, 114, 166, 21);
		eventsPanel.add(callCheckBox);
		
		gpsCalibrationCheckBox = new JCheckBox("GPS Calibration");
		gpsCalibrationCheckBox.setEnabled(false);
		gpsCalibrationCheckBox.setSelected(true);
		gpsCalibrationCheckBox.setBounds(0, 45, 166, 21);
		eventsPanel.add(gpsCalibrationCheckBox);
		
		internetConnectionCheckBox = new JCheckBox("Internet Connection");
		internetConnectionCheckBox.setEnabled(false);
		internetConnectionCheckBox.setSelected(true);
		internetConnectionCheckBox.setBounds(0, 160, 166, 21);
		eventsPanel.add(internetConnectionCheckBox);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 277, 618, 22);
		frmEnviar.getContentPane().add(separator);
		
		JLabel lblTestSuitCreation = new JLabel("Test Suite Creation");
		lblTestSuitCreation.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestSuitCreation.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuitCreation.setBounds(10, 10, 541, 22);
		frmEnviar.getContentPane().add(lblTestSuitCreation);
		
		JPanel panel = new JPanel();
		panel.setBounds(302, 38, 225, 180);
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
				if(pathList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(pathPanel, "Select at least one path", "Attention", JOptionPane.WARNING_MESSAGE);
				}else if(eventsSelected() == 0){
					JOptionPane.showMessageDialog(pathPanel, "Select at least one event", "Attention", JOptionPane.WARNING_MESSAGE);
				}else if(!allSpeedsInformed()) {
					JOptionPane.showMessageDialog(pathPanel, "Please, inform all path speeds", "Attention", JOptionPane.WARNING_MESSAGE);
				}else if(!orderSelected()){
					JOptionPane.showMessageDialog(pathPanel, "Choose one of the five orders", "Attention", JOptionPane.WARNING_MESSAGE);
				}else {
					int order = orderChoosen();
					ArrayList<String[]> pathsAndSpeeds = new ArrayList<String[]>();
					int rowCount = dtm.getRowCount();
					for (int i = 0; i < rowCount; i++) {
						String[] aux = {(String) dtm.getValueAt(i, 0), (String) dtm.getValueAt(i, 1)};
						pathsAndSpeeds.add(aux);
					}
					TestSuiteGenerator tsg = new TestSuiteGenerator(order, pathsAndSpeeds);
					try {
						tsg.createTestSuite();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(pathPanel, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnCreateTestSuite.setBounds(224, 232, 144, 35);
		frmEnviar.getContentPane().add(btnCreateTestSuite);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(537, 38, 91, 180);
		frmEnviar.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblOrder = new JLabel("Order");
		lblOrder.setBounds(0, 0, 50, 22);
		lblOrder.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_1.add(lblOrder);
		
		ButtonGroup buttonGroup = new ButtonGroup(); 
		
		order1RadioButton = new JRadioButton("1 - Order");
		order1RadioButton.setBounds(0, 28, 105, 21);
		panel_1.add(order1RadioButton);
		buttonGroup.add(order1RadioButton);
		
		order2RadioButton = new JRadioButton("2 - Order");
		order2RadioButton.setBounds(0, 51, 105, 21);
		panel_1.add(order2RadioButton);
		buttonGroup.add(order2RadioButton);
		
		order3RadioButton = new JRadioButton("3 - Order");
		order3RadioButton.setBounds(0, 74, 105, 21);
		panel_1.add(order3RadioButton);
		buttonGroup.add(order3RadioButton);
		
		order4RadioButton = new JRadioButton("4 - Order");
		order4RadioButton.setBounds(0, 97, 105, 21);
		panel_1.add(order4RadioButton);
		buttonGroup.add(order4RadioButton);
		
		order5RadioButton = new JRadioButton("5 - Order");
		order5RadioButton.setBounds(0, 120, 105, 21);
		panel_1.add(order5RadioButton);
		buttonGroup.add(order5RadioButton);
	}
	
	private int orderChoosen() {
		int order = 0;
		
		if(order1RadioButton.isSelected()) {
			order = 1;
		}else if(order2RadioButton.isSelected()) {
			order = 2;
		}else if(order3RadioButton.isSelected()) {
			order = 3;
		}else if(order4RadioButton.isSelected()) {
			order = 4;
		}else if(order5RadioButton.isSelected()) {
			order = 5;
		}
		return order;
	}
	
	private boolean orderSelected() {
		if(order1RadioButton.isSelected()) {
			return true;
		}
		if(order2RadioButton.isSelected()) {
			return true;
		}
		if(order3RadioButton.isSelected()) {
			return true;
		}
		if(order4RadioButton.isSelected()) {
			return true;
		}
		if(order5RadioButton.isSelected()) {
			return true;
		}
		return false;
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
