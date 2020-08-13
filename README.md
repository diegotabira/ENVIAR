# ENVIAR

ENVIAR is a tool capable of simulating the sending of application environment data, system events, and simulating adverse situations (i.e., GPS not calibrated). Using the ENVIAR tool, it is possible to create test scenarios that would be difficult to perform manually. Besides, it is possible to create situations during a route such as an internet connection changing. For example, consider that a team is developing a GPS navigation application that uses data provided over the internet. It is possible to test the application manually by turning off the internet phone connection. However, if the route is from one city to another, this test may not be feasible. ENVIAR can simulate any route in the Android emulator and simulate events like the example above. Also, ENVIAR does not require any changes to the Android operating system, does not require code instrumentation, nor does the AUT source code. ENVIAR only needs the tester to create the test cases and opens the AUT in the functionality he wants to test. For example, if the tester wants to check an application's GPS navigator, he must open the application and make it ready to use the GPS navigation functionality.

Before you can run ENVIAR, it is necessary to prepare the environment for it to run.

Minimum Requirements for Execution:

  - Dual core 2 GHz processor;
  - 8 GB of RAM;
  - 20 GB free hard disk space;
  - Windows 7 Operating System;

Recommended Requirements for Execution:

  - Quad Core 2.4GHz Processor;
  - 16 GB of RAM;
  - 50 GB free hard disk space;
  - Windows 10 Operating System;

In addition to the requirements described, it is necessary:

	- Install Java 1.8+;
		- Set the JAVA_HOME environment variable.
	- Install Android SDK;
		- Create the emulator that will run the applications to be tested;
		- Set ANDROID_HOME variable pointing to SDK root folder;
		- Set up the following paths	
			- ANDROID_HOME
			- ANDROID_HOME\platform-tools
			- ANDROID_HOME\emulator
			- ANDROID_HOME\tools
			- ANDROID_HOME\tools\bin
			- ANDROID_HOME\build-tools

The AVD used to evaluate the ENVIAR tool can be found [HERE](https://1drv.ms/u/s!AsZf37wBzvQLj4gsOtVe1YDAkkfGpQ?e=j4aS14). Its evaluation files can be found [HERE](https://drive.google.com/drive/folders/1qIOzFmquatkcI-2ZJD0aXudY_kNMf9tB?usp=sharing).

ENVIAR is open source software and is distributed under the [GNU GPL license (version 3)](http://www.gnu.org/licenses/gpl-3.0.txt).
