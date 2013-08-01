package coffee;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RobotTest {

	/**
	 * @param args
	 * @throws AWTException 
	 */
	public static void main(String[] args) throws AWTException {
		System.out.println("LEFT_CLICK = "+InputEvent.BUTTON1_DOWN_MASK);
		System.out.println("LEFT_CLICK = "+InputEvent.BUTTON1_MASK);
		System.out.println("MIDDLE_CLICK = "+InputEvent.BUTTON2_DOWN_MASK);
		System.out.println("MIDDLE_CLICK = "+InputEvent.BUTTON2_MASK);
		System.out.println("RIGHT_CLICK = "+InputEvent.BUTTON3_DOWN_MASK);
		System.out.println("RIGHT_CLICK = "+InputEvent.BUTTON3_MASK);
		Robot robot = new Robot();
//		System.out.println("LEFT = "+KeyEvent.VK_LEFT);
//		System.out.println("RIGHT = "+KeyEvent.VK_RIGHT);
//		System.out.println("A = "+KeyEvent.VK_A);
//		System.out.println("B = "+KeyEvent.VK_B);
//		System.out.println("C = "+KeyEvent.VK_C);
//		System.out.println("F1 = "+KeyEvent.VK_F1);
//		System.out.println("F2 = "+KeyEvent.VK_F2);
//		System.out.println("CTRL = "+KeyEvent.VK_CONTROL);
		robot.delay(2000);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(2000);
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		robot.delay(2000);
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
//		int i=0;
//		for(i=0;i<10;i++){
//			robot.keyPress(KeyEvent.VK_RIGHT);
//		}
	}

}
