package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import testCase.TestSuiteFiles;

public class ExectionWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable mainResultTable;
	private DefaultTableModel mainResultdtm;
	private JList<String> appPackageJList;
	private DefaultListModel<String> appPackageListModel;
	private JList<String> testCasesList;
	private DefaultListModel<String> testCasesListModel;
	private JLabel testedLabel;
	private JLabel acceptedLabel;
	private JLabel failedLabel;
	private JLabel notTestedLabel;
	private JTextArea sentCommandsTextArea;
	private JTextArea logcatTextArea;
	
	private TestSuiteFiles testSuiteFiles;
	
	private boolean loading;

	/**
	 * Create the frame.
	 * @param tsf 
	 */
	public ExectionWindow(TestSuiteFiles tsf) {
		loading = false;
		this.testSuiteFiles = tsf;
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1003, 653);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestSuiteExecution = new JLabel("Test Suite Execution");
		lblTestSuiteExecution.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestSuiteExecution.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestSuiteExecution.setBounds(10, 10, 1143, 22);
		contentPane.add(lblTestSuiteExecution);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 311, 100, 259);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblTestCases = new JLabel("Test Cases");
		lblTestCases.setBounds(0, 0, 100, 22);
		lblTestCases.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(lblTestCases);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 32, 100, 227);
		panel.add(scrollPane);
		
		testCasesListModel = new DefaultListModel<String>();
		testCasesList = new JList<String>(testCasesListModel);
		testCasesList.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setViewportView(testCasesList);
		
		
		
		testCasesList.addMouseListener(new java.awt.event.MouseAdapter() {  

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!testCasesList.isSelectionEmpty()) {
					String testCase = testCasesList.getSelectedValue();
					String app = appPackageJList.getSelectedValue();
					try {
						if(!loading) {
							loadSentCommands(app, testCase);
							loadLogcat(app, testCase);
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ExectionWindow.this, e1.getMessage(), "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(120, 311, 419, 259);
		contentPane.add(panel_1);
		
		JLabel lblSentCommands = new JLabel("Sent Commands");
		lblSentCommands.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSentCommands.setBounds(0, 0, 148, 22);
		panel_1.add(lblSentCommands);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 24, 419, 235);
		panel_1.add(scrollPane_1);
		
		sentCommandsTextArea = new JTextArea();
		scrollPane_1.setViewportView(sentCommandsTextArea);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(179, 42, 805, 259);
		contentPane.add(panel_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 32, 696, 227);
		panel_2.add(scrollPane_2);
		
		
		String[] testSuitecolumnNames = {"#", "Verdict", "Time", "Path", "Speed", "Setup", "First_Delay", "First_Event", "Second_Delay", "Second_Event", "Third_Delay", "Third_Event", "Fourth_Delay", "Fourth_Event", "Fifth_Delay", "Fifth_Event"};
		mainResultdtm = new DefaultTableModel();
		mainResultdtm.setColumnIdentifiers(testSuitecolumnNames);
		mainResultTable = new JTable(mainResultdtm);
		mainResultTable.setEnabled(false);
		scrollPane_2.setViewportView(mainResultTable);
		setTableColumnSize();
		
		JLabel lblMainResult = new JLabel("Main Result");
		lblMainResult.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMainResult.setBounds(0, 0, 225, 22);
		panel_2.add(lblMainResult);
		
		testedLabel = new JLabel("Tested: ");
		testedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		testedLabel.setBounds(706, 32, 89, 22);
		panel_2.add(testedLabel);
		
		acceptedLabel = new JLabel("Accepted: ");
		acceptedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		acceptedLabel.setBounds(706, 64, 89, 22);
		panel_2.add(acceptedLabel);
		
		failedLabel = new JLabel("Failed: ");
		failedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		failedLabel.setBounds(706, 96, 89, 22);
		panel_2.add(failedLabel);
		
		notTestedLabel = new JLabel("Not Tested: ");
		notTestedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		notTestedLabel.setBounds(706, 128, 89, 22);
		panel_2.add(notTestedLabel);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExectionWindow.this.dispose();
			}
		});
		btnClose.setBounds(105, 597, 85, 21);
		contentPane.add(btnClose);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(10, 42, 168, 259);
		contentPane.add(panel_3);
		
		JLabel lblAppPackage = new JLabel("Application Name");
		lblAppPackage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAppPackage.setBounds(0, 0, 164, 22);
		panel_3.add(lblAppPackage);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(0, 32, 164, 196);
		panel_3.add(scrollPane_3);
		
		appPackageListModel = new DefaultListModel<>();
		appPackageJList = new JList(appPackageListModel);
		appPackageJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_3.setViewportView(appPackageJList);
		appPackageJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!appPackageJList.isSelectionEmpty()) {
					sentCommandsTextArea.setText("");
					logcatTextArea.setText("");
					String app = appPackageJList.getSelectedValue();
					loadMainResult(app);
					loadTestCasesList(app);
				}
			}
		});
		loadAppList();
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBounds(84, 238, 80, 21);
		panel_3.add(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<String> selectedApps = (ArrayList<String>) appPackageJList.getSelectedValuesList();

				if(selectedApps.size() == 0) {
					JOptionPane.showMessageDialog(ExectionWindow.this, "No selected item", "Attention", JOptionPane.WARNING_MESSAGE);
				} else {
					int option = JOptionPane.showConfirmDialog(ExectionWindow.this, "Do you really want to remove the app(s)?", "Attention", JOptionPane.WARNING_MESSAGE);
					if(option == 0) {
						for (String selectedApp : selectedApps) {
							File appDirectory = new File("testSuitesResults//" + testSuiteFiles.getTestSuiteName() + "//" + selectedApp);
							
							File[] files = appDirectory.listFiles();  
							for (File file : files) {
								file.delete();
							}
							
				            appDirectory.delete();
							testSuiteFiles.removeTestedApp(selectedApp);
							testCasesListModel.removeAllElements();
						}			
						loadAppList();
					}
				}
			}
		});
		
		JButton btnNewApp = new JButton("New");
		btnNewApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String appName = JOptionPane.showInputDialog("Type the App Name");
				if(appName != null && !appName.equals("")) {
					String folder = "testSuitesResults//" + testSuiteFiles.getTestSuiteName() + "//" + appName;
					new File(folder).mkdir();
					testSuiteFiles.addTestedApp(appName);
					loadAppList();
				}
			}
		});
		btnNewApp.setBounds(0, 238, 80, 21);
		panel_3.add(btnNewApp);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(10, 597, 85, 21);
		contentPane.add(btnExecute);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(549, 311, 435, 259);
		contentPane.add(panel_4);
		
		JLabel lblLogcat = new JLabel("Logcat");
		lblLogcat.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogcat.setBounds(0, 0, 148, 22);
		panel_4.add(lblLogcat);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(0, 24, 435, 235);
		panel_4.add(scrollPane_4);
		
		logcatTextArea = new JTextArea();
		scrollPane_4.setViewportView(logcatTextArea);
	}

	private void loadMainResult(String app) {
		while(mainResultdtm.getRowCount() > 0) {
			mainResultdtm.removeRow(0);			
		}
		int qteTestCases = 0;
		int qteAccepted = 0;
		int qteFailed = 0;
		int notTested = calculateQteTestCases();
		String mainResult = testSuiteFiles.getMainResults().get(app);
		if(mainResult != null) {
			Scanner scanner = new Scanner(mainResult);
			String linha = "";
			while(scanner.hasNextLine()) {
				linha = scanner.nextLine();
				if(linha.contains("#")) {
					continue;
				}
				qteTestCases++;
				notTested--;
				if(linha.contains("Accept")) {
					qteAccepted++;
				}
				if(linha.contains("Fail")) {
					qteFailed++;
				}
				String[] newRow = new String[16];
				String[] lineElements = linha.split("\t\t");
				for (int i = 0; i < lineElements.length; i++) {
					newRow[i] = lineElements[i];
				}					
				mainResultdtm.addRow(newRow);	
			}
			scanner.close();
		}
		testedLabel.setText("Tested: " + qteTestCases);
		acceptedLabel.setText("Accepted: " + qteAccepted);
		failedLabel.setText("Failed: " + qteFailed);
		notTestedLabel.setText("Not Tested: " + notTested);
	}

	private int calculateQteTestCases() {
		Scanner scanner = new Scanner(testSuiteFiles.getTestSuite());
		int qte = 0;
		while(scanner.hasNextLine()) {
			qte++;
			scanner.nextLine();
		}
		scanner.close();
		return qte;
	}

	private void loadAppList() {
		if(appPackageListModel != null) {
			appPackageListModel.removeAllElements();
		}
		ArrayList<String> apps = testSuiteFiles.getTestedApps();
		for (String app : apps) {
			appPackageListModel.addElement(app);
		}		
	}
	
	private void loadSentCommands(String app, String testCase) throws IOException {
		
		sentCommandsTextArea.setText("Loading...");
		loading = true;
		
		new Thread() {
		     
		    @Override
		    public void run() {
		    	ArrayList<String> sentCommandsList = testSuiteFiles.getSentCommands().get(app);
				for (String sentCommands : sentCommandsList) {
					if(sentCommands.contains(testCase)) {
						String filePath = "testSuitesResults//" + testSuiteFiles.getTestSuiteName() + "//" + app + "//" + sentCommands + ".txt";
						FileReader arq;
						try {
							arq = new FileReader(filePath);
							BufferedReader lerArq = new BufferedReader(arq);
							String linha = lerArq.readLine();
							String result = "";
							while(linha != null) {
								result += linha + "\n";
								linha = lerArq.readLine();
							}
							lerArq.close();
							sentCommandsTextArea.setText(result);
						} catch (FileNotFoundException e) {
							JOptionPane.showMessageDialog(ExectionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(ExectionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
				}
				loading = false;
		    }
		  }.start();
		
		
	}
	
	private void loadLogcat(String app, String testCase) throws IOException {
		
		loading = true;
		logcatTextArea.setText("Loading...");
		
		new Thread() {
		     
		    @Override
		    public void run() {
		    	
		    	ArrayList<String> logcatList = testSuiteFiles.getLogcats().get(app);
				for (String logcat : logcatList) {
					if(logcat.contains(testCase)) {
						String filePath = "testSuitesResults//" + testSuiteFiles.getTestSuiteName() + "//" + app + "//" + logcat + ".txt";
						FileReader arq;
						try {
							arq = new FileReader(filePath);
							BufferedReader lerArq = new BufferedReader(arq);
							String linha = lerArq.readLine();
							String result = "";
							int aux = 0;
							while(linha != null) {
								result += linha + "\n";
								aux++;
								System.out.println(aux);
								linha = lerArq.readLine();
							}
							lerArq.close();
							logcatTextArea.setText(result);
							break;
						} catch (FileNotFoundException e) {
							JOptionPane.showMessageDialog(ExectionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(ExectionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
		    	
				loading = false;
		    }
		  }.start();
		
		
	}
	
	private void loadTestCasesList(String app) {
		if(testCasesListModel != null) {
			testCasesListModel.removeAllElements();
		}
		ArrayList<String> testCases = testSuiteFiles.getTestedTestCases().get(app);
		for (String testCase : testCases) {
			testCasesListModel.addElement(testCase);
		}
	}

	private void setTableColumnSize() {
		mainResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		mainResultTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		mainResultTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		mainResultTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		mainResultTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		mainResultTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		mainResultTable.getColumnModel().getColumn(5).setPreferredWidth(50);
		int columnIndex = 6;
		for (int i = 0; i < 5; i++) {
			mainResultTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(75);
			columnIndex++;
			mainResultTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(200);
			columnIndex++;
		}
	       
	}
}
