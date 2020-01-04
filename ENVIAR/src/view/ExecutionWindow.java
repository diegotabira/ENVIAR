package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import exceptions.SintaxException;
import main.TestSuiteExecutor;
import terminal.ADBComunicator;
import terminal.Commands;
import testCase.TestSuiteFiles;
import util.GuiUpdater;
import util.Monitor;
import util.Util;

public class ExecutionWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable mainResultTable;
	private JScrollPane mainResultScroolPane;
	private JScrollPane logcatScrollPane;
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
	private JScrollPane sentCommandsScrollPane;
	private JTextArea logcatTextArea;
	private JButton executeButton;
	private JLabel sentCommandsLabel;
	private JProgressBar progressBar;
	
	private JButton btnNewApp;
	private JButton btnClose;
	private JButton deleteButton;
	
	private JButton acceptButton;
	private JButton rejectButton;
	private JButton stopButton;
	
	private TestSuiteFiles testSuiteFiles;
	
	private boolean loading;
	private boolean executingTestSuite;
	
	private Monitor monitor;

	/**
	 * Create the frame.
	 * @param tsf 
	 */
	public ExecutionWindow(TestSuiteFiles tsf) {
		GuiUpdater.getInstance().setExecutionWindow(this);
		loading = false;
		executingTestSuite = false;
		this.testSuiteFiles = tsf;
		monitor = Monitor.getInstance();
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
		lblTestSuiteExecution.setBounds(10, 10, 993, 22);
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
							sentCommandsLabel.setText("Sent Commands");
							loadSentCommands(app, testCase);
							loadLogcat(app, testCase);
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ExecutionWindow.this, e1.getMessage(), "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(120, 311, 419, 259);
		contentPane.add(panel_1);
		
		sentCommandsLabel = new JLabel("Sent Commands");
		sentCommandsLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		sentCommandsLabel.setBounds(0, 0, 148, 22);
		panel_1.add(sentCommandsLabel);
		
		sentCommandsScrollPane = new JScrollPane();
		sentCommandsScrollPane.setBounds(0, 24, 419, 235);
		panel_1.add(sentCommandsScrollPane);
		
		sentCommandsTextArea = new JTextArea();
		sentCommandsTextArea.setEditable(false);
		sentCommandsScrollPane.setViewportView(sentCommandsTextArea);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(179, 42, 805, 259);
		contentPane.add(panel_2);
		
		mainResultScroolPane = new JScrollPane();
		mainResultScroolPane.setBounds(0, 32, 696, 227);
		panel_2.add(mainResultScroolPane);
		
		
		String[] testSuitecolumnNames = {"#", "Verdict", "Time", "Path", "Speed", "Setup", "First_Delay", "First_Event", "Second_Delay", "Second_Event", "Third_Delay", "Third_Event", "Fourth_Delay", "Fourth_Event", "Fifth_Delay", "Fifth_Event"};
		mainResultdtm = new DefaultTableModel();
		mainResultdtm.setColumnIdentifiers(testSuitecolumnNames);
		mainResultTable = new JTable(mainResultdtm);
		mainResultTable.setEnabled(false);
		mainResultScroolPane.setViewportView(mainResultTable);
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
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExecutionWindow.this.dispose();
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
					Util.APP_PKG = loadAppPackage(app);
					loadMainResult(app);
					loadTestCasesList(app);
				}
			}
		});
		loadAppList();
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(84, 238, 80, 21);
		panel_3.add(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<String> selectedApps = (ArrayList<String>) appPackageJList.getSelectedValuesList();

				if(selectedApps.size() == 0) {
					JOptionPane.showMessageDialog(ExecutionWindow.this, "No selected item", "Attention", JOptionPane.WARNING_MESSAGE);
				} else {
					int option = JOptionPane.showConfirmDialog(ExecutionWindow.this, "Do you really want to remove the app(s)?", "Attention", JOptionPane.WARNING_MESSAGE);
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
		
		btnNewApp = new JButton("New");
		btnNewApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String appName = JOptionPane.showInputDialog("Type the App Name");
				if(appName != null && !appName.equals("")) {
					String appPackage = JOptionPane.showInputDialog("Type the App Package");
					if(appPackage != null && !appPackage.equals("") && !appPackage.contains(" ")) {
						try {
							String folder = "testSuitesResults//" + testSuiteFiles.getTestSuiteName() + "//" + appName;
							new File(folder).mkdir();
							BufferedWriter buffWrite = new BufferedWriter(new FileWriter(folder + "//" + "pkg.txt"));
							buffWrite.append(appPackage);
							buffWrite.close();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(ExecutionWindow.this, e1.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
						testSuiteFiles.addTestedApp(appName);
						testSuiteFiles.addPackageApp(appName, appPackage);
						loadAppList();	
					}else {
						JOptionPane.showMessageDialog(ExecutionWindow.this, "Please, enter the package correctly", "Attention",
								JOptionPane.WARNING_MESSAGE);
					}					
				}
			}
		});
		btnNewApp.setBounds(0, 238, 80, 21);
		panel_3.add(btnNewApp);
		
		executeButton = new JButton("Execute");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(appPackageJList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(ExecutionWindow.this, "Please, select an application", "Attention",
							JOptionPane.WARNING_MESSAGE);
				}else if(!avdRunning()){
					try {
						String[] avds = getInstalledAVDs();
						int x = JOptionPane.showOptionDialog(null, "Choose one of the AVDs to run",
								"Click a button",
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, avds, avds[0]);
						runAVD(avds[x]);
						
					} catch (IOException | InterruptedException e1) {
						JOptionPane.showMessageDialog(ExecutionWindow.this, e1.getMessage(), "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
					sentCommandsTextArea.setText("");
					JOptionPane.showMessageDialog(ExecutionWindow.this, "Wait for the emulator to start.", "Attention",
							JOptionPane.INFORMATION_MESSAGE);
				}else {
					monitor.start();
					executeButton.setEnabled(false);
					sentCommandsLabel.setText("Terminal");
					executingTestSuite = true;
					String app = appPackageJList.getSelectedValue();
					testSuiteFiles.setChosenApp(app);
					enableDisableElements();
					executeTestSuite();					
				}
			}
		});
		executeButton.setBounds(10, 597, 85, 21);
		contentPane.add(executeButton);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(549, 311, 435, 259);
		contentPane.add(panel_4);
		
		JLabel lblLogcat = new JLabel("Logcat");
		lblLogcat.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogcat.setBounds(0, 0, 148, 22);
		panel_4.add(lblLogcat);
		
		logcatScrollPane = new JScrollPane();
		logcatScrollPane.setBounds(0, 24, 435, 235);
		panel_4.add(logcatScrollPane);
		
		logcatTextArea = new JTextArea();
		logcatTextArea.setEditable(false);
		logcatScrollPane.setViewportView(logcatTextArea);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(393, 581, 310, 62);
		contentPane.add(panel_5);
		panel_5.setLayout(null);
		
		acceptButton = new JButton("Accept");
		acceptButton.setBounds(10, 31, 85, 21);
		panel_5.add(acceptButton);
		acceptButton.setVisible(false);
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.accept();
			}
		});
		
		rejectButton = new JButton("Reject");
		rejectButton.setBounds(105, 31, 85, 21);
		panel_5.add(rejectButton);
		rejectButton.setVisible(false);
		rejectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.reject();
			}
		});
		
		stopButton = new JButton("Stop");
		stopButton.setBounds(200, 31, 85, 21);
		panel_5.add(stopButton);
		stopButton.setVisible(false);
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executingTestSuite = false;
				enableDisableElements();
				monitor.stop();
			}
		});
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 10, 275, 11);
		panel_5.add(progressBar);
		progressBar.setVisible(false);
		progressBar.setMaximum(100);
	}
	
	public void updateResults() throws SintaxException, IOException {
		testSuiteFiles.reloadData();
		loadMainResult(testSuiteFiles.getChosenApp());
		loadTestCasesList(testSuiteFiles.getChosenApp());
	}

	private void runAVD(String avd) throws IOException, InterruptedException {
		new Thread() {
			
			@Override
		    public void run() {
		    	try {
		    		ADBComunicator adb = ADBComunicator.getInstance();
		    		adb.runADBCommand(Commands.START_EMULATOR + avd);	
				} catch (IOException | InterruptedException e) {
					JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
		    }
		}.start();
	}
	
	private String[] getInstalledAVDs() throws IOException, InterruptedException {
		ADBComunicator adb = ADBComunicator.getInstance();
		String adbResponse = adb.runADBCommand(Commands.LIST_AVDS);
		String[] avds = adbResponse.split("\n");
		return avds;
	}
	
	private boolean avdRunning() {
		ADBComunicator adb = ADBComunicator.getInstance();
		try {
			String response = adb.runADBCommand(Commands.INTERNET_ON);
			sentCommandsTextArea.setText("");
			if(response.contains("no emulator detected") || response.equals("")) {
				return false;
			}
		} catch (IOException | InterruptedException e) {
			JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}

	private void executeTestSuite() {
		new Thread() {
		     
		    @Override
		    public void run() {
		    	TestSuiteExecutor tse = new TestSuiteExecutor(ExecutionWindow.this, testSuiteFiles);
		    	try {
					tse.execute();
				} catch (SintaxException | IOException | InterruptedException e) {
					JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
		    }
		  }.start();
		
	}

	public JTextArea getSentCommandsTextArea() {
		return this.sentCommandsTextArea;
	}
	
	public JScrollPane getSentCommandsScrollPane() {
		return this.sentCommandsScrollPane;
	}
	
	public JTextArea getLogcatTextArea() {
		return this.logcatTextArea;
	}
	
	public JScrollPane getLogcateScrollPane() {
		return this.logcatScrollPane;
	}
	
	public JProgressBar getProgressBar() {
		return this.progressBar;
	}

	private void enableDisableElements() {
		boolean enable = !executingTestSuite;
		appPackageJList.setEnabled(enable);
		testCasesList.setEnabled(enable);
		btnNewApp.setEnabled(enable);
		btnClose.setEnabled(enable);
		executeButton.setEnabled(enable);
		deleteButton.setVisible(executingTestSuite);
		acceptButton.setVisible(executingTestSuite);
		rejectButton.setVisible(executingTestSuite);
		stopButton.setVisible(executingTestSuite);
		progressBar.setVisible(executingTestSuite);
		if(executingTestSuite) {
			sentCommandsTextArea.setText("");
			logcatTextArea.setText("");
		}
		
	}
	
	private String loadAppPackage(String app) {
		String a = testSuiteFiles.getAppPackages().get(app);
		return testSuiteFiles.getAppPackages().get(app);
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
		JScrollBar vertical = mainResultScroolPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		testedLabel.setText("Tested: " + qteTestCases);
		acceptedLabel.setText("Accepted: " + qteAccepted);
		failedLabel.setText("Failed: " + qteFailed);
		notTestedLabel.setText("Not Tested: " + notTested);
	}
	
	public void addTestCase(String testCase) {
		String[] newRow = new String[16];
		String[] lineElements = testCase.split("\t\t");
		int qte = mainResultdtm.getRowCount() + 1;
		newRow[0] = qte + "";
		newRow[1] = "Testing";
		newRow[2] = "";
		for (int i = 0; i < lineElements.length; i++) {
			newRow[i + 3] = lineElements[i];
		}					
		mainResultdtm.addRow(newRow);
		JScrollBar vertical = mainResultScroolPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		vertical.setValue(vertical.getMaximum());
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
							JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
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
							JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(ExecutionWindow.this, e.getMessage(), "ERROR",
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
