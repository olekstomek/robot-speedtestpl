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
        int minimumSecondsForTest = 5;
        int maximumSecondsForTest = 60;
        Robot robot = new Robot();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage imageStartButton = ImageIO.read(new File("../resources/button_start.PNG"));
        BufferedImage imageTestAgainButton = ImageIO.read(new File("../resources/button_testuj_ponownie.PNG"));

        browse(new URL("http://www.speedtest.pl/"));
        System.out.println("Opening URL...");
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
        System.out.println("Pause time/s for " + numberTests + "test/s in order [seconds]: " + Arrays.toString(times));

        while (counterTests < numberTests) {
            System.out.println("Test numer " + counterTests + " executing...");
            confirmButtonStart(imageStartButton);
            confirmButtonTestAgain(imageTestAgainButton, robot, dimension);
            Thread.sleep(times[counterTests] * 1_000);
            ++counterTests;
        }
        System.out.println("End.");
    }

    private static void confirmButtonStart(BufferedImage imageStartButton) {
        Target imageStartButtonTarget = new ImageTarget(imageStartButton);
        ScreenRegion s = new DesktopScreenRegion();
        ScreenRegion r = s.wait(imageStartButtonTarget, 3_600_000);
        Mouse mouse = new DesktopMouse();
        mouse.click(r.getCenter());
    }

    private static void confirmButtonTestAgain(BufferedImage imageTestAgainButton, Robot robot, Dimension dimension) {
        Target imageTestAgainButtonTarget = new ImageTarget(imageTestAgainButton);
        ScreenRegion s = new DesktopScreenRegion();
        ScreenRegion r = s.wait(imageTestAgainButtonTarget, 3_600_000);
        screenCapture(robot, dimension);
        Mouse mouse = new DesktopMouse();
        mouse.click(r.getCenter());
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