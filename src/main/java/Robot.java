import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.sikuli.api.API.browse;

public class Robot {
    public static void main(String[] argv) throws Exception {
        int counterTests = 0;
        int numberTests = 3;
        int minimumSecondsForTest = 120;
        int maximumSecondsForTest = 150;
        java.awt.Robot robot = new java.awt.Robot();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        browse(new URL("http://www.speedtest.pl/"));
        System.out.println("Opening URL...");
        int x = (int) (0.53 * dimension.width);
        int y = (int) (0.61 * dimension.height);
        System.out.println("Dimension: " + dimension);

        if (argv.length > 0) {
            try {
                numberTests = Integer.parseInt(argv[0]);
                minimumSecondsForTest = Integer.parseInt(argv[1]);
                maximumSecondsForTest = Integer.parseInt(argv[2]);
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer!");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Settings default params:" +
                    "\n - number tests: " + numberTests +
                    "\n - minimum seconds to start: " + minimumSecondsForTest +
                    "\n - maximum seconds to start: " + maximumSecondsForTest);
        }

        int[] times = new SplittableRandom().ints(numberTests, minimumSecondsForTest, maximumSecondsForTest)
                .parallel()
                .toArray();
        System.out.println("Pause times for each test in order [seconds]: " + Arrays.toString(times));

        while (counterTests < numberTests) {
            System.out.println("Test numer " + counterTests + " executing...");
            Thread.sleep((long) (times[counterTests] * 300));
            confirmButtonStart(robot, x, y);
            Thread.sleep((long) (times[counterTests] * 700));
            screenCapture(robot, dimension);
            confirmButtonTestAgain(robot, x, y + 130);
            ++counterTests;
        }
        System.out.println("End.");
    }

    private static void confirmButtonStart(java.awt.Robot robot, int x, int y) {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private static void confirmButtonTestAgain(java.awt.Robot robot, int x, int y) {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private static void screenCapture(java.awt.Robot robot, Dimension dimension) {
        Rectangle rectangle = new Rectangle(dimension);
        BufferedImage screen = robot.createScreenCapture(rectangle);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-YYYY--hh-mm-ss_");
            ImageIO.write(screen, "jpg", new File(simpleDateFormat
                    .format(new Date()) + "speedtest.jpg"));
        } catch (IOException e) {
            System.err.println("Can't make screenshot.");
            e.printStackTrace();
        }
    }
}