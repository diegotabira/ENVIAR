package testCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import exceptions.SintaxException;

public class TestSuiteFiles {
	
	private String testSuiteName;
	private String testSuite;
	private String chosenApp;
	private ArrayList<String> testedApps;
	private HashMap<String, String> appPackages;
	private HashMap<String, String> mainResults;
	private HashMap<String, ArrayList<String>> testedTestCases;
	private HashMap<String, ArrayList<String>> sentCommands;
	private HashMap<String, ArrayList<String>> logcats;
	
	public TestSuiteFiles(String testSuiteName) throws SintaxException, IOException {
		this.testSuiteName = testSuiteName;
		this.chosenApp = "";
		loadData();
	}
	
	private void loadData() throws SintaxException, IOException {
		loadTestSuiteFile();
		loadTestedApps();
		loadAppPackages();
		loadMainResults();
		loadTestedTestCases();
		loadSentCommands();
		loadLogcats();
	}
	
	public void reloadData() throws SintaxException, IOException {
		loadData();
	}

	public String getChosenApp() {
		return chosenApp;
	}

	public void setChosenApp(String chosenApp) {
		this.chosenApp = chosenApp;
	}

	private void loadTestSuiteFile() throws SintaxException, IOException {
		FileReader arq = new FileReader("testSuites//" + testSuiteName + ".txt");
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		testSuite = "";
		while(linha != null) {
			testSuite += linha + "\n";
			linha = lerArq.readLine();
		}
		lerArq.close();
	}
	
	private void loadTestedApps() {
		testedApps = new ArrayList<String>();
		File appsDirectory = new File("testSuitesResults//" + testSuiteName);
		File[] apps = appsDirectory.listFiles();
		if(apps != null) {
			for (File file : apps) {			
				String[] aux = file.getName().split("//");
				testedApps.add(aux[0]);
			}			
		}
	}
	
	private void loadAppPackages() throws IOException {
		appPackages = new HashMap<String, String>();
		for (String app : testedApps) {
			String direcotoryPath = "testSuitesResults//" + testSuiteName + "//" + app;
			File appsDirectory = new File(direcotoryPath);
			File[] files = appsDirectory.listFiles();
			for (File file : files) {
				String fileName = file.getName();
				if(fileName.contains("pkg")) {				
					FileReader arq = new FileReader(direcotoryPath + "//" + fileName);
					BufferedReader lerArq = new BufferedReader(arq);
					String linha = lerArq.readLine();					
					lerArq.close();
					appPackages.put(app, linha);
					break;
				}
			}			
		}
	}
	
	private void loadMainResults() throws IOException {
		mainResults = new HashMap<>();
		for (String app : testedApps) {
			String direcotoryPath = "testSuitesResults//" + testSuiteName + "//" + app;
			File appsDirectory = new File(direcotoryPath);
			File[] files = appsDirectory.listFiles();
			for (File file : files) {
				String fileName = file.getName();
				if(fileName.contains("Test Suit")) {				
					FileReader arq = new FileReader(direcotoryPath + "//" + fileName);
					BufferedReader lerArq = new BufferedReader(arq);
					String linha = lerArq.readLine();
					String mainResult = "";
					while(linha != null) {
						mainResult += linha + "\n";
						linha = lerArq.readLine();
					}
					lerArq.close();
					mainResults.put(app, mainResult);
					break;
				}
			}			
		}		
	}
	
	private void loadTestedTestCases() {
		testedTestCases = new HashMap<String, ArrayList<String>>();
		for (String app : testedApps) {
			ArrayList<String> testCases = new ArrayList<String>();
			File appsDirectory = new File("testSuitesResults//" + testSuiteName + "//" + app);
			File[] apps = appsDirectory.listFiles();
			for (File file : apps) {
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				if(fileName.contains("Logcat")) {
					testCases.add(fileName.substring(0, fileName.indexOf("Logcat")));
				}
			}
			testedTestCases.put(app, testCases);
		}		
	}
	
	private void loadSentCommands() {
		sentCommands = new HashMap<String, ArrayList<String>>();
		for (String app : testedApps) {
			ArrayList<String> sentCommandsFiles = new ArrayList<String>();
			File appsDirectory = new File("testSuitesResults//" + testSuiteName + "//" + app);
			File[] apps = appsDirectory.listFiles();
			for (File file : apps) {
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				if(fileName.contains("SentCommands")) {
					sentCommandsFiles.add(fileName);
				}
			}
			sentCommands.put(app, sentCommandsFiles);
		}		
		
	}
	
	private void loadLogcats() {
		logcats = new HashMap<String, ArrayList<String>>();
		for (String app : testedApps) {
			ArrayList<String> testCases = new ArrayList<String>();
			File appsDirectory = new File("testSuitesResults//" + testSuiteName + "//" + app);
			File[] apps = appsDirectory.listFiles();
			for (File file : apps) {
				String fileName = file.getName().substring(0, file.getName().length() - 4);
				if(fileName.contains("Logcat")) {
					testCases.add(fileName);
				}
			}
			logcats.put(app, testCases);
		}		
		
	}
	
	public String getTestSuiteName() {
		return testSuiteName;
	}

	public String getTestSuite() {
		return testSuite;
	}

	public ArrayList<String> getTestedApps() {
		return testedApps;
	}
	
	public HashMap<String, String> getAppPackages() {
		return appPackages;
	}

	public HashMap<String, String> getMainResults() {
		return mainResults;
	}

	public HashMap<String, ArrayList<String>> getTestedTestCases() {
		return testedTestCases;
	}

	public HashMap<String, ArrayList<String>> getSentCommands() {
		return sentCommands;
	}

	public HashMap<String, ArrayList<String>> getLogcats() {
		return logcats;
	}

	public void addTestedApp(String appName) {
		this.testedApps.add(appName);
		this.testedTestCases.put(appName, new ArrayList<String>());
	}
	
	public void addPackageApp(String appName, String appPackage) {
		this.appPackages.put(appName, appPackage);
	}

	public void removeTestedApp(String selectedApp) {
		this.testedApps.remove(selectedApp);
		this.testedTestCases.remove(selectedApp);
	}

}
