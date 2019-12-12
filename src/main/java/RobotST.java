import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import static org.sikuli.api.API.browse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


public class RobotST {
    public static void main(String[] argv) throws Exception {
        int counterTests = 1;
        int numberTests = 50;
        LocalDateTime finishTestsDateTime = LocalDateTime.now().plusHours(1);
        int minimumSecondsForTest = 20;
        int maximumSecondsForTest = 30;
        int timeWaitForElement = 60 + maximumSecondsForTest;
        boolean makeScreenshot = true;
        int pauseBeforeNextTest;
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
                finishTestsDateTime = finishTestsDateTime.minusHours(1).plusMinutes(Long.parseLong(argv[1]));
                minimumSecondsForTest = Math.abs(Integer.parseInt(argv[2]));
                maximumSecondsForTest = Math.abs(Integer.parseInt(argv[3]));
                makeScreenshot = Boolean.parseBoolean(argv[4]);
                if (minimumSecondsForTest > maximumSecondsForTest) {
                    minimumSecondsForTest = minimumSecondsForTest - maximumSecondsForTest;
                    maximumSecondsForTest = maximumSecondsForTest + minimumSecondsForTest;
                    minimumSecondsForTest = maximumSecondsForTest - minimumSecondsForTest;
                }
                timeWaitForElement = 60 + maximumSecondsForTest;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer!");
                e.printStackTrace();
                System.exit(1);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Input all arguments or none!");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Default configuration");
        }
        System.out.println("Max waiting for elements on page [s]: " + timeWaitForElement);
        showSettings(numberTests, minimumSecondsForTest, maximumSecondsForTest, makeScreenshot, finishTestsDateTime);
        while (counterTests <= numberTests || LocalDateTime.now().isBefore(finishTestsDateTime)) {
            System.out.println("Test numer " + counterTests + " executing...");
            try {
                confirmButtonStart(imageStartButton, timeWaitForElement);
                confirmButtonTestAgain(imageTestAgainButton, robot, dimension, timeWaitForElement, makeScreenshot);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Probably problem with find element on page, opening new page and trying again");
                browse(new URL("http://www.speedtest.pl/"));
            }
            System.out.println("Test number " + counterTests + " finished.");
            if (counterTests <= numberTests || LocalDateTime.now().isBefore(finishTestsDateTime)) {
                pauseBeforeNextTest = (int) (Math.random() * ((maximumSecondsForTest - minimumSecondsForTest) + 1))
                        + minimumSecondsForTest;
                System.out.println("\nWaiting for next test by: " + pauseBeforeNextTest + " seconds.");
                Thread.sleep(pauseBeforeNextTest * 1_000);
            } else {
                System.out.println("Last test was executed. End.");
            }
            ++counterTests;
        }
    }

    private static void showSettings(int numberTests, int minimumSecondsForTest,
                                     int maximumSecondsForTest, boolean makeScreenshot,
                                     LocalDateTime finishTestsDateTime) {
        System.out.println("Settings params:" +
                "\n - Tests will be completed after "
                + numberTests +
                " tests and when the date and time didn't exceed: "
                + finishTestsDateTime +
                "\n - minimum seconds to start test: " +
                minimumSecondsForTest +
                "\n - maximum seconds to start test: " +
                maximumSecondsForTest +
                "\n - make screenshot: " +
                makeScreenshot);
    }

    private static void confirmButtonStart(BufferedImage imageStartButton, int time_wait_for_element) {
        ScreenRegion r = findButtonOnPage(imageStartButton, time_wait_for_element);
        clickOnButton(r);
    }

    private static void confirmButtonTestAgain(BufferedImage imageTestAgainButton, Robot robot,
                                               Dimension dimension, int time_wait_for_element, boolean makeScreenshot) {
        ScreenRegion r = findButtonOnPage(imageTestAgainButton, time_wait_for_element);
        makeScreenshot(makeScreenshot, robot, dimension);
        clickOnButton(r);
    }

    private static void makeScreenshot(boolean makeScreenshot, Robot robot, Dimension dimension) {
        if (makeScreenshot) {
            screenCapture(robot, dimension);
        }
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-YYYY--hh-mm-ss-aaa__");
            ImageIO.write(screen, "jpg", new File(simpleDateFormat
                    .format(new Date()) + "speedtest.jpg"));
            System.out.println("Screenshot was made.");
        } catch (IOException e) {
            System.err.println("Can't make screenshot.");
            e.printStackTrace();
        }
    }
}