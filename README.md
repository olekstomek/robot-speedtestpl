# robot-speedtestpl
Program using the robot class in Java and the sikuli-api library for taking measurements of internet speed in an external application

It is a simple program that uses the [Sikuli Java API library](https://code.google.com/archive/p/sikuli-api/). The program opens an external page for testing internet speed in the default browser on the default screen (if you use more than one monitor), recognizes graphic elements to perform the process and clicks on them. For each result, a screenshot with the result is automatically created and saved in the program launch location. The program has default parameters set in the source code which of course you can change when compiling.


You can run the program from the command line and provide arguments. To do this, you must first compile the code (example for the terminal in the path with the code file):

`javac -cp ../resources/sikuli-slides-1.2.0.jar RobotST.java`

and then run the java class with parameters, for example:

`java -cp .;../resources/sikuli-slides-1.2.0.jar RobotST 3 10 20`

**`;` on Windows**

**`:` on Linux or Mac**

It means that:
- three tests will be performed -> **the first parameter**
- the interval between tests will be random from 10 seconds to 19 *(10 <= x < 20)* -> **second and third parameter**

Test example in the console:
```
java -cp .;../resources/sikuli-slides-1.2.0.jar RobotST 5 2 -10
Opening URL...
Dimension: java.awt.Dimension[width=1366,height=768]
Max waiting for elements on page [s]: 70
Pause time/s for 5 test/s in order [seconds]: [8, 8, 9, 2, 9]
Test numer 0 executing...
Test numer 1 executing...
Test numer 2 executing...
Test numer 3 executing...
Test numer 4 executing...
Last test was executed. End.
```

Remember that:
- you can not specify a value other than a number (eg String) as an argument,
- the second parameter can be smaller than the third (program change automatically values),
- the program waits by default for graphic elements 60 seconds + maximum seconds for test (second parameter),
- values can be negative (program change automatically values to positive),
- program create screenshot with every result.