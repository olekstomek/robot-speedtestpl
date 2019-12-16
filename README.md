# robot-speedtestpl
Program using the robot class in Java and the sikuli-api library for taking measurements of internet speed in an external application

It is a simple program that uses the [Sikuli Java API library](https://code.google.com/archive/p/sikuli-api/). The program opens an external page for testing internet speed in the default browser on the default screen (if you use more than one monitor), recognizes graphic elements to perform the process and clicks on them. For each result, a screenshot with the result is automatically created and saved in the program launch location. The program has default parameters set in the source code which of course you can change when compiling.

You can create an account and log in to speedtest.pl. Thanks to this, the results are saved on the server and you can download them as a CSV file. You can also see the speed history in the graph (this can be useful especially for mobile connections).
![image](https://user-images.githubusercontent.com/26818304/70943850-1631f000-2052-11ea-825a-d04863160749.png)

You can run the program from the command line and provide arguments. To do this, you must first compile the code (example for the terminal in the path with the code file):

`javac -cp ../resources/sikuli-slides-1.2.0.jar RobotST.java`

and then run the java class with parameters, for example:

`java -cp .;../resources/sikuli-slides-1.2.0.jar RobotST 3 50 10 20 true`

**`;` on Windows**

**`:` on Linux or Mac**

It means that:
- three tests will be performed -> **the first parameter**
- fifty minutes will be add to current time and by this time tests will be executed -> **second parameter**
- the interval between tests will be random from 10 seconds to 19 *(10 <= x < 20)* -> **third and fourth parameter**
- screenshots with results will be created (true/false) - > **fifth parameter**

Attention. The first and second parameters are the AND logical condition (you can change it to OR). AND means that the test will end if the number of tests given in the first parameter is reached AND the time for testing has ended.

Test example in the console (simulation):
```
java -cp .;../resources/sikuli-slides-1.2.0.jar RobotST 2 2 7 13 true
Opening URL...
Dimension: java.awt.Dimension[width=1366,height=768]
Max waiting for elements on page [s]: 73
Settings params:
 - Tests will be completed after 2 tests and when the date and time didn't exceed: 2019-12-16T22:08:57.512506600
 - minimum seconds to start test: 7
 - maximum seconds to start test: 13
 - make screenshot: true
Test numer 1 executing...
Screenshot was made.
Test number 1 finished.

Waiting for next test by: 10 seconds.
Test numer 2 executing...
Screenshot was made.
Test number 2 finished.

Waiting for next test by: 13 seconds.
Test numer 3 executing...
Screenshot was made.
Test number 3 finished.
Last test was executed. End.
```
As I wrote above: I gave the number of tests 2 in the parameter and 3 were completed, because the date and time had not been reached yet. If time were reached and the number of tests had not yet been completed - the tests will continue to be carried out until two conditions are met.

You can run without arguments:
```
java -cp .;../resources/sikuli-slides-1.2.0.jar RobotST
Opening URL...
Dimension: java.awt.Dimension[width=1366,height=768]
Default configuration
Max waiting for elements on page [s]: 90
Settings params:
 - Tests will be completed after 50 tests and when the date and time didn't exceed: 2019-12-16T23:11:40.700531700
 - minimum seconds to start test: 20
 - maximum seconds to start test: 30
 - make screenshot: true
Test numer 1 executing...
```

Remember that:
- you can not specify a value other than a number (eg String) as an argument,
- the third parameter can be smaller than the fourth (program change automatically values),
- the program waits by default for graphic elements 60 seconds + maximum seconds for test (second parameter),
- values can be negative (program change automatically values to positive),
- program create screenshot with every result,
- if the page does not display a button or the button is not recognized, a new page tab will be opened in the browswer and the test will continue,
- if the page changes the layout (e.g. start button), all you have to do is take a screenshot of this button and overwrite it (__button_testuj_ponowanie.PNG__),
- if the test run time expires during the test countdown, the test will not run
