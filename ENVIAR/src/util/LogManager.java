package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.CrashException;
import terminal.ADBComunicator;
import testCase.TestCase;

public class LogManager {
	
	private String testSuitResults;
	private String pathFile;
	private ArrayList<String[]> testSuite;
	private String text;
	private Logcather logcather;
	
	public LogManager(String appPackage) {
		logcather = new Logcather();
		pathFile = "results/" + appPackage + "/";
		testSuitResults = pathFile + "0 - Test Suite Results.txt";		
		try {
			loadTestCases();
		} catch (FileNotFoundException e) {
			try {
				File path = new File(pathFile);
				if(!path.exists()) {
					path.mkdirs();
				}
				BufferedWriter buffWrite = new BufferedWriter(new FileWriter(testSuitResults));
		        buffWrite.append("#\t\tVerdict\t\tTime Slept\t\tPath\t\tSpeed\t\tSetup\t\tDelays and Events");
		        buffWrite.close();
		        loadTestCases();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTestCases() throws IOException {
		FileReader arq = new FileReader(testSuitResults);
		testSuite = new ArrayList<>();
		text = "";
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		if(linha != null) {
			text += linha;
			linha = lerArq.readLine();
		}
		while(linha != null) {
			text += "\n" + linha;
			linha = Util.retirarExcessoEspacos(linha);
			String[] colunas = linha.split(" ");
			testSuite.add(colunas);
			linha = lerArq.readLine();
		}	
		
		lerArq.close();
	}
	
	private boolean equals(String[] testCaseStr, TestCase testCase) {
		if(!testCaseStr[3].equals(testCase.getPathName())) {
			return false;
		}
		if(Double.valueOf(testCaseStr[4]) != testCase.getMaxSpeed()) {
			return false;
		}
		if(!testCaseStr[5].equals(testCase.getSetupName())) {
			return false;
		}
		
		ArrayList<String> delayEvents = testCase.getDelayEvents();
		int j = 0;
		for(int i = 6; i < testCaseStr.length; i++) {
			if(!testCaseStr[i].equals(delayEvents.get(j))) {
				return false;
			}
			j++;
		}
		return true;
	}
	
	public boolean alreadyTested(TestCase testCase) {
		for (String[] testCaseStr : testSuite) {
			if(equals(testCaseStr, testCase)) {
				return true;
			}
		}
		return false;
	}
	
	public void clearLogcat() throws IOException {
		logcather.clear();
	}
	
	public String getLogcat() throws IOException {
		return logcather.getLogcat();
	}
	
	public void monitorLogcat() throws IOException, CrashException {
		logcather.monitorLogcat();
	}
	
	public void saveTestCase(String verdict, long sleptTime, TestCase testCase, int index) {
		try {
			String separator = "\t\t";
			String line = "\n" + index + separator + verdict + separator + sleptTime + separator + testCase.toString();
			text += line;
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(testSuitResults));
			buffWrite.append(text);
			buffWrite.close();
			
			
//			String logcat = logcather.getLogcat('v');
			String indexStr = Integer.toString(index);
			if(index < 10) {
				indexStr = "00" + indexStr;
			}else if(index < 100) {
				indexStr = "0" + indexStr;
			}
			String logcat = logcather.getLogcat();
			String logcatName = "Test Case " + indexStr + " Logcat.txt";
//			logcatName = logcatName.replace(separator, " ");
			buffWrite = new BufferedWriter(new FileWriter(pathFile + "/" + logcatName));
			buffWrite.append(logcat);
			buffWrite.close();
			
			String sentCommandsName = "Test Case " + indexStr + " SentCommands.txt";
			buffWrite = new BufferedWriter(new FileWriter(pathFile + "/" + sentCommandsName));
			buffWrite.append(ADBComunicator.getInstance().getSentCommands());
			buffWrite.close();
			ADBComunicator.getInstance().clearSendCommands();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
