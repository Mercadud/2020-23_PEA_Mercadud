/**
 * 
 */
package DataSomething;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/**
 * @author merc This pretty much times the sorting algorithms if you want to
 * 
 *         don't ask why the sorting algorithms are literally taking 1
 *         millisecond
 */
public class Stopwatch {

	Timer timer;
	TimerTask task;

	boolean on = false;

	int milli = 0;

	public Stopwatch() {
		timer = new Timer("thread");
		task = new TimerTask() {

			public void run() {
				if (on) {
					milli++;
				}
			}
		};
		timer.scheduleAtFixedRate(task, 1, 1);
	}

	public void start() {
		on = true;
	}

	public void stop() {
		on = false;
	}

	public void reset() {
		milli = 0;
	}

	public String getTime() {
		return String.format("%03d", milli);
	}

	/*
	 * this was to test to see if the stopwatch class was accurate and I compared it
	 * to an external stopwatch and it is accurate:)
	 */
	private void timerTest() {
		start();
		for (int i = 0; i < 10000000; i++) {
			System.out.println(i);
		}
		stop();
		JOptionPane.showMessageDialog(null, getTime());
		reset();
	}
}
