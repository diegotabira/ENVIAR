package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import exceptions.SintaxException;

public class PathEditorWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField pathNameTextField;
	private JEditorPane pathEditorPane;
	private MainWindow mainWindow;//Instância da janela principal
	private String pathName;
	private String path = "";
	private final String DIRECTORY_PATH = "paths/";

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PathEditorWindow frame = new PathEditorWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public PathEditorWindow() {
		setResizable(false);
		setUndecorated(true);
//		getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		setTitle("Path Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPathPoints = new JLabel("Path Points");
		lblPathPoints.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPathPoints.setBounds(10, 74, 133, 22);
		contentPane.add(lblPathPoints);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 106, 296, 153);
		contentPane.add(scrollPane);
		
		pathEditorPane = new JEditorPane();
		scrollPane.setViewportView(pathEditorPane);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(pathNameTextField.getText().equals("")) {
						JOptionPane.showMessageDialog(PathEditorWindow.this, "The path name cannot be empty.", "Attention", JOptionPane.WARNING_MESSAGE);
					}else {
						pathName = pathNameTextField.getText();
						savePath();
						if(mainWindow != null) {
							mainWindow.loadPathList();
						}
						PathEditorWindow.this.dispose();						
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(PathEditorWindow.this, "Failed to save", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSave.setBounds(10, 269, 85, 21);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathEditorWindow.this.dispose();
			}
		});
		btnCancel.setBounds(105, 269, 85, 21);
		contentPane.add(btnCancel);
		
		JLabel lblPathName = new JLabel("Path Name");
		lblPathName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPathName.setBounds(10, 10, 133, 22);
		contentPane.add(lblPathName);
		
		pathNameTextField = new JTextField();
		pathNameTextField.setBounds(10, 42, 133, 22);
		contentPane.add(pathNameTextField);
		pathNameTextField.setColumns(10);
	}

	public void setPath(String pathName) throws SintaxException, IOException {
		this.pathName = pathName;
		pathNameTextField.setText(pathName);
		loadPath();
	}
	
	@SuppressWarnings("resource")
	private void loadPath() throws SintaxException, IOException {
		FileReader arq = new FileReader(DIRECTORY_PATH + pathName + ".txt");
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		while(linha != null) {
			path += linha + "\n";
			linha = lerArq.readLine();
		}
		pathEditorPane.setText(path);
	}
	
	private void savePath() throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(DIRECTORY_PATH + pathName + ".txt"));
		buffWrite.append(pathEditorPane.getText());
		buffWrite.close();
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
	}
	
}
