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

Result in the console:
```
Opening URL...
Dimension: java.awt.Dimension[width=1366,height=768]
Pause time/s for 3test/s in order [seconds]: [17, 16, 17]
Test numer 0 executing...
Test numer 1 executing...
Test numer 2 executing...
End.
```

Remember that:
- you can not specify a value other than a number (eg String) as an argument,
- the second parameter must be smaller than the third (bound must be greater than origin)
- the program waits by default for graphic elements 1 hour (3 600 000 milliseconds), if the parameters of *the minimumSecondsForTest* or *maximumSecondsForTest* exceed this value an error may appear. Of course, you can always change this time before compiling.
