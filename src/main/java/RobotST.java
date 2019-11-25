import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.sikuli.api.API.browse;

public class RobotST {
    public static void main(String[] argv) throws Exception {
        int counterTests = 0;
        int numberTests = 2;
        int minimumSecondsForTest = 20;
        int maximumSecondsForTest = 30;
        int timeWaitForElement = 60 + maximumSecondsForTest;
        Robot robot = new Robot();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage imageStartButton = ImageIO.read(new File("../resources/button_start.PNG"));
        BufferedImage imageTestAgainButton = ImageIO.read(new File("../resources/button_testuj_ponownie.PNG"));

        browse(new URL("http://www.speedtest.pl/"));
        System.out.println("Opening URL...");
        System.out.println("Dimension: " + dimension);

        if (argv.length > 0) {
            try {
                numberTests = Math.abs(Integer.parseInt(argv[0]));
                minimumSecondsForTest = Math.abs(Integer.parseInt(argv[1]));
                maximumSecondsForTest = Math.abs(Integer.parseInt(argv[2]));
                if (minimumSecondsForTest > maximumSecondsForTest) {
                    minimumSecondsForTest = minimumSecondsForTest - maximumSecondsForTest;
                    maximumSecondsForTest = maximumSecondsForTest + minimumSecondsForTest;
                    minimumSecondsForTest = maximumSecondsForTest - minimumSecondsForTest;
                }
                timeWaitForElement = 60 + maximumSecondsForTest;
                System.out.println("Max waiting for elements on page [s]: " + timeWaitForElement);
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
        System.out.println("Pause time/s for " + numberTests + " test/s in order [seconds]: " + Arrays.toString(times));

        while (counterTests < numberTests) {
            System.out.println("Test numer " + counterTests + " executing...");
            try {
                confirmButtonStart(imageStartButton, timeWaitForElement);
                confirmButtonTestAgain(imageTestAgainButton, robot, dimension, timeWaitForElement);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Probably problem with find element on page, opening new page and trying again");
                browse(new URL("http://www.speedtest.pl/"));
            }
            Thread.sleep(times[counterTests] * 1_000);
            ++counterTests;
        }
        System.out.println("Last test was executed. End.");
    }

    private static void confirmButtonStart(BufferedImage imageStartButton, int time_wait_for_element) {
        ScreenRegion r = findButtonOnPage(imageStartButton, time_wait_for_element);
        clickOnButton(r);
    }

    private static void confirmButtonTestAgain(BufferedImage imageTestAgainButton, Robot robot,
                                               Dimension dimension, int time_wait_for_element) {
        ScreenRegion r = findButtonOnPage(imageTestAgainButton, time_wait_for_element);
        screenCapture(robot, dimension);
        clickOnButton(r);
    }

    private static void clickOnButton(ScreenRegion r) {
        Mouse mouse = new DesktopMouse();
        mouse.click(r.getCenter());
    }

    private static ScreenRegion findButtonOnPage(BufferedImage imageButton, int timeWaitForElement) {
        Target imageTestAgainButtonTarget = new ImageTarget(imageButton);
        ScreenRegion s = new DesktopScreenRegion();
        return s.wait(imageTestAgainButtonTarget, timeWaitForElement * 1000);
    }

    private static void screenCapture(Robot robot, Dimension dimension) {
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