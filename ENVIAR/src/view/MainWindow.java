package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import testCase.TestSuiteFiles;
import testCase.TestSuiteGenerator;

public class MainWindow {

	private JFrame frmEnviar;
	private JList<String> pathList;
	private DefaultListModel<String> pathListModel;
	private JList<String> testSuitesJList;
	private DefaultListModel<String> testSuitesListModel;
	private JTable speedTable;
	private DefaultTableModel speedTabelDtm;
	private JPanel pathPanel;
	private JButton createTestSuiteButton;
	
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
	private JLabel generatingJLabel;
	private JLabel lblTestSuiteExecution;
	private JPanel panel_2;
	private JLabel lblTestSuites;
	private JScrollPane scrollPane_1;
	private JTable testSuiteTable;
	private DefaultTableModel testSuitedtm;
	
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
		frmEnviar.setBounds(100, 100, 649, 676);
		frmEnviar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEnviar.getContentPane().setLayout(null);		
		pathPanel = new JPanel();
		pathPanel.setBounds(10, 38, 100, 184);
		frmEnviar.getContentPane().add(pathPanel);
		pathPanel.setLayout(null);
		
		JLabel lblPath = new JLabel("Path");
		lblPath.setBounds(0, 0, 37, 22);
		pathPanel.add(lblPath);
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		pathListModel = new DefaultListModel<>();
		pathList = new JList<String>(pathListModel);
		pathList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				removerTodasLinhasTabela();
				if(!pathList.isSelectionEmpty()) {
					ArrayList<String> selectedPaths = (ArrayList<String>) pathList.getSelectedValuesList();
					for (String path : selectedPaths) {
						String[] newRow = {path, ""};
						speedTabelDtm.addRow(newRow);
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
		lblTestSuitCreation.setBounds(10, 10, 618, 22);
		frmEnviar.getContentPane().add(lblTestSuitCreation);
		
		JPanel panel = new JPanel();
		panel.setBounds(302, 38, 225, 184);
		frmEnviar.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 31, 205, 139);
		panel.add(scrollPane);
		
		String[] columnNames = {"Selected Paths", "Max Speed"};
		speedTabelDtm = new DefaultTableModel();
		speedTabelDtm.setColumnIdentifiers(columnNames);
		
		speedTable = new JTable(speedTabelDtm);
		scrollPane.setViewportView(speedTable);
		
		
		
		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSpeed.setBounds(0, 0, 77, 22);
		panel.add(lblSpeed);
		
		createTestSuiteButton = new JButton("Create Test Suite");
		createTestSuiteButton.addActionListener(new ActionListener() {
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
					generateTestSuite();
				}
			}
		});
		createTestSuiteButton.setBounds(224, 232, 144, 35);
		frmEnviar.getContentPane().add(createTestSuiteButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(537, 38, 91, 184);
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
		
		generatingJLabel = new JLabel("Generating...");
		generatingJLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		generatingJLabel.setForeground(Color.RED);
		generatingJLabel.setBounds(378, 228, 106, 39);
		generatingJLabel.setVisible(false);
		frmEnviar.getContentPane().add(generatingJLabel);
		
		lblTestSuiteExecution = new JLabel("Test Suite Execution");
		lblTestSuiteExecution.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestSuiteExecution.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuiteExecution.setBounds(10, 291, 618, 22);
		frmEnviar.getContentPane().add(lblTestSuiteExecution);
		
		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(10, 323, 100, 259);
		frmEnviar.getContentPane().add(panel_2);
		
		lblTestSuites = new JLabel("Test Suites");
		lblTestSuites.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuites.setBounds(0, 0, 100, 22);
		panel_2.add(lblTestSuites);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 24, 100, 204);
		panel_2.add(scrollPane_1);
		
		testSuitesListModel = new DefaultListModel<>();
		testSuitesJList = new JList<String>(testSuitesListModel);
		testSuitesJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_1.setViewportView(testSuitesJList);
		testSuitesJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!testSuitesJList.isSelectionEmpty()) {
					try {
						loadTestSuite(testSuitesJList.getSelectedValue());
					} catch (SintaxException | IOException e1) {
						JOptionPane.showMessageDialog(pathPanel, "Failed to load test suite.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> selectedTestSuites = (ArrayList<String>) testSuitesJList.getSelectedValuesList();
				if(selectedTestSuites.size() == 0) {
					JOptionPane.showMessageDialog(pathPanel, "No selected item", "Attention", JOptionPane.WARNING_MESSAGE);
				}else {
					int option = JOptionPane.showConfirmDialog(pathPanel, "Do you really want to remove these teste suite(s)?", "Attention", JOptionPane.WARNING_MESSAGE);
					if(option == 0) {
						for (String testSuite : selectedTestSuites) {
							File testSuiteFile = new File("testSuites/" + testSuite + ".txt");
							testSuiteFile.delete();
						}
						loadTestSuiteList();
					}
				}
			}
		});
		
		deleteButton.setBounds(0, 238, 100, 21);
		panel_2.add(deleteButton);
		loadTestSuiteList();
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(120, 323, 508, 259);
		frmEnviar.getContentPane().add(panel_3);
		
		JLabel lblTestSuite = new JLabel("Test Suite");
		lblTestSuite.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuite.setBounds(0, 0, 100, 22);
		panel_3.add(lblTestSuite);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 24, 508, 235);
		panel_3.add(scrollPane_2);
		
		String[] testSuitecolumnNames = {"#", "Path", "Speed", "Setup", "First_Delay", "First_Event", "Second_Delay", "Second_Event", "Third_Delay", "Third_Event", "Fourth_Delay", "Fourth_Event", "Fifth_Delay", "Fifth_Event"};
		testSuitedtm = new DefaultTableModel();
		testSuitedtm.setColumnIdentifiers(testSuitecolumnNames);
		testSuiteTable = new JTable(testSuitedtm);
		testSuiteTable.setEnabled(false);
		scrollPane_2.setViewportView(testSuiteTable);
		setTableColumnSize();
		
		JButton executeTestSuiteButton = new JButton("Test Suite Execution");
		executeTestSuiteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int qteSelectedTestSuite = testSuitesJList.getSelectedIndices().length;
				if(qteSelectedTestSuite == 0) {
					JOptionPane.showMessageDialog(pathPanel, "Select one test suite", "Attention", JOptionPane.WARNING_MESSAGE);
				}else if(qteSelectedTestSuite > 1) {
					JOptionPane.showMessageDialog(pathPanel, "Select only one test suite", "Attention", JOptionPane.WARNING_MESSAGE);
				}else {
					try {
						executeTestSuite(testSuitesJList.getSelectedValue());
					} catch (SintaxException | IOException e1) {
						JOptionPane.showMessageDialog(pathPanel, "Failed to load test suite files", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		executeTestSuiteButton.setBounds(224, 592, 197, 35);
		frmEnviar.getContentPane().add(executeTestSuiteButton);
	}
	
	private void setTableColumnSize() {
		testSuiteTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		testSuiteTable.getColumnModel().getColumn(0).setPreferredWidth(25); 
		testSuiteTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		testSuiteTable.getColumnModel().getColumn(2).setPreferredWidth(40);
		testSuiteTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		int columnIndex = 4;
		for (int i = 0; i < 5; i++) {
			testSuiteTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(75);
			columnIndex++;
			testSuiteTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(200);
			columnIndex++;
		}
	       
	}

	private void executeTestSuite(String testSuiteName) throws SintaxException, IOException {
		TestSuiteFiles tsf = new TestSuiteFiles(testSuiteName);
		ExectionWindow ew = new ExectionWindow(tsf);
		ew.setVisible(true);
	}

	private void loadTestSuite(String testSuiteName) throws SintaxException, IOException {
		loadTestSuiteFile(testSuiteName);		
	}
	
	@SuppressWarnings("resource")
	private void loadTestSuiteFile(String testSuiteName) throws SintaxException, IOException {
		while(testSuitedtm.getRowCount() > 0) {
			testSuitedtm.removeRow(0);			
		}
		FileReader arq = new FileReader("testSuites//" + testSuiteName + ".txt");
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		int number = 0;
		while(linha != null) {
			number++;
			String[] newRow = new String[14];
			String[] lineElements = linha.split("\t");
			newRow[0] = "" + number;
			for (int i = 0; i < lineElements.length; i++) {
				newRow[i+1] = lineElements[i];
			}					
			testSuitedtm.addRow(newRow);		
			linha = lerArq.readLine();
		}
		lerArq.close();
	}

	private void generateTestSuite() {
		
		new Thread() {
		     
		    @Override
		    public void run() {
		    	int order = orderChoosen();
				if(order > 3) {
					int option = JOptionPane.showConfirmDialog(pathPanel, "Generating a test suite of order greater than 3 may take minutes or hours. Do you wish to continue?", "Attention", JOptionPane.WARNING_MESSAGE);
					if(option != 0) {
						return;
					}
				}
				String testSuiteName = JOptionPane.showInputDialog("Type the Test Suite Name");
				if(testSuiteName != null && !testSuiteName.equals("")) {
					generatingJLabel.setVisible(true);
					createTestSuiteButton.setEnabled(false);
					changeGeneratingLabel();
					ArrayList<String[]> pathsAndSpeeds = new ArrayList<String[]>();
					int rowCount = speedTabelDtm.getRowCount();
					for (int i = 0; i < rowCount; i++) {
						String[] aux = {(String) speedTabelDtm.getValueAt(i, 0), (String) speedTabelDtm.getValueAt(i, 1)};
						pathsAndSpeeds.add(aux);
					}
					try {
						TestSuiteGenerator tsg = new TestSuiteGenerator(testSuiteName, order, pathsAndSpeeds);
						tsg.createTestSuite();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(pathPanel, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					generatingJLabel.setVisible(false);
					createTestSuiteButton.setEnabled(true);
					loadTestSuiteList();
				}
		    }
		  }.start();	
		
		
	}

	protected void changeGeneratingLabel() {
		new Thread() {
		     
		    @Override
		    public void run() {
		    	while(generatingJLabel.isVisible()) {
		    		try {
		    			generatingJLabel.setText("Generating");
						Thread.sleep(1000);
		    			generatingJLabel.setText("Generating.");
						Thread.sleep(1000);
						generatingJLabel.setText("Generating..");
						Thread.sleep(1000);
						generatingJLabel.setText("Generating...");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		       
		    }
		  }.start();
		
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
		int qteRows = speedTabelDtm.getRowCount();
		for (int i = 0; i < qteRows; i++) {
			String speedStr = (String) speedTabelDtm.getValueAt(i, 1);
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
		if(pathListModel != null) {
			pathListModel.removeAllElements();
		}
		File pathsDirectory = new File("paths/");
		File[] paths = pathsDirectory.listFiles();
		for (File file : paths) {
			String fileName = file.getName().substring(0, file.getName().length() - 4);
			pathListModel.addElement(fileName);
		}
		
	}
	
	private void loadTestSuiteList() {
		if(testSuitesListModel != null) {
			testSuitesListModel.removeAllElements();
		}
		File testSuitesDirectory = new File("testSuites/");
		File[] testSuites = testSuitesDirectory.listFiles();
		for (File file : testSuites) {
			String fileName = file.getName().substring(0, file.getName().length() - 4);
			testSuitesListModel.addElement(fileName);
		}
	}

	private void removerTodasLinhasTabela() {
		while(speedTabelDtm.getRowCount() > 0) {
			speedTabelDtm.removeRow(0);
		}
	}
}
