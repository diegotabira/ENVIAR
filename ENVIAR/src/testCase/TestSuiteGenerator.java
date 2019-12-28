package testCase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestSuiteGenerator {
	
	private String testSuiteName;
	private int order;
	private ArrayList<String[]> pathsAndSpeeds;
	private BufferedReader lerArq;
	private String pictInputStr;
	
	public TestSuiteGenerator(String testSuiteName, int order, ArrayList<String[]> pathsAndSpeeds) {
		this.testSuiteName = testSuiteName;
		this.order = order;
		this.pathsAndSpeeds = pathsAndSpeeds;
		this.pictInputStr = "";
	}

	public void createTestSuite() throws Exception {
		createPictInput();
		executePict();	
	}
	
	private void createPictInput() throws Exception {
		String newLine = "Path: ";
		for (String[] path : pathsAndSpeeds) {
			newLine += path[0] + ", ";
		}
		newLine = newLine.substring(0, newLine.length() - 2) + "\n";
		pictInputStr += newLine;
		
		newLine = "Speed: ";
		for (String[] speed : pathsAndSpeeds) {
			newLine += speed[1] + ", ";
		}
		newLine = newLine.substring(0, newLine.length() - 2) + "\n";
		pictInputStr += newLine;
		
		newLine = "Setup: S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12" + "\n";
		pictInputStr += newLine;
		
		pictInputStr += getEventLines(order);
		
		//RULES
		
		newLine = "";
		for (String[] pathAndSpeed : pathsAndSpeeds) {
			newLine += "IF [Path] = " + "\"" + pathAndSpeed[0] + "\" " + "THEN [Speed] = " + pathAndSpeed[1] + ";" +"\n";
		}
		pictInputStr += newLine;
		
		try {
			pictInputStr += getEventRules(order);
			savePictInput(pictInputStr);
		} catch (IOException e) {
			throw new Exception("Failed to generate test suite");
		}
	}

	private String getEventLines(int order) {
		String waits = "WAIT, NOT_WAIT";		
		String events = "GPS_ON, GPS_OFF, SIMULATE_LONG_BACKGROUND, TAKE_A_PICTURE, ORIENTATION_PORTRAIT, ORIENTATION_LANDSCAPE, RECEIVE_CALL, ACCEPT_CALL, CANCEL_CALL, GPS_CALIBRATED, GPS_NOT_CALIBRATED, INTERNET_ON, INTERNET_OFF";
		
		String[] eventsWaits = new String[5];
		eventsWaits[0] = "First_Delay:" +" " + waits + "\n";
		eventsWaits[0] += "First_Event:" + " " + "GPS_ON, GPS_OFF, SIMULATE_LONG_BACKGROUND, TAKE_A_PICTURE, ORIENTATION_PORTRAIT, ORIENTATION_LANDSCAPE, RECEIVE_CALL, GPS_CALIBRATED, GPS_NOT_CALIBRATED, INTERNET_ON, INTERNET_OFF" + "\n";
		eventsWaits[1] = "Second_Delay:" +" " + waits + "\n";
		eventsWaits[1] += "Second_Event:" + " " + events + "\n";
		eventsWaits[2] = "Third_Delay:" +" " + waits + "\n";
		eventsWaits[2] += "Third_Event:" + " " + events + "\n";
		eventsWaits[3] = "Fourth_Delay:" +" " + waits + "\n";
		eventsWaits[3] += "Fourth_Event:" + " " + events + "\n";
		eventsWaits[4] = "Fifth_Delay:" +" " + waits + "\n";
		eventsWaits[4] += "Fifth_Event:" + " " + events + "\n";
		
		String result = "";
		for (int i = 0; i < order; i++) {
			result += eventsWaits[i];
		}
		return result;
	}
	
	private String getEventRules(int order) throws IOException {
		String rulesFile = "pict/" + "order" + order + "Rules.txt";
		FileReader arq = new FileReader(rulesFile);
		lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		String result = "";
		while(linha != null) {
			result += linha + "\n";
			linha = lerArq.readLine();
		}
		return result;
	}
	
	private void savePictInput(String pictInputStr) throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter("pict/pictInput.txt"));
		buffWrite.append(pictInputStr);
		buffWrite.close();
	}
	
	private String executePict() throws Exception {
		StringBuffer returnValue = new StringBuffer();
        String line;
        InputStream inStream = null;
        try {
        	String execPath = "pict\\pict.exe";
        	String inPath = "pict\\pictInput.txt";
        	String outPath = "testSuites\\" + testSuiteName + ".txt";
        	ProcessBuilder builder = new ProcessBuilder(execPath, inPath);
        	builder.redirectOutput(new File(outPath));
        	Process process = builder.start();
            inStream = process.getInputStream();
            BufferedReader brCleanUp = new BufferedReader(
                    new InputStreamReader(inStream));
            while ((line = brCleanUp.readLine()) != null) {
                returnValue.append(line).append("\n");
            }

            brCleanUp.close();
            try {


                process.waitFor();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        	throw new Exception("Failed to generate test suite"); 
        }
        return returnValue.toString();
	}

}
