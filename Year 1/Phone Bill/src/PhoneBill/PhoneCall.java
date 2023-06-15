package PhoneBill;

public class PhoneCall {

	private String number;
	private String dayOfWeek;
	private String fullTime;
	private int duration, time;

	public PhoneCall() {
		dayOfWeek = "Unknown";
		fullTime = "Unknown";
		duration = 0;
	} //PhoneCall()

	public PhoneCall(String day, String clock, int callDur) {
		if (day.equals("Mo"))
			dayOfWeek = "Monday";
		else if (day.equals("Tu"))
			dayOfWeek = "Tuesday";
		else if (day.equals("We"))
			dayOfWeek = "Wednesday";
		else if (day.equals("Th"))
			dayOfWeek = "Thursday";
		else if (day.equals("Fr"))
			dayOfWeek = "Friday";
		else if (day.equals("Sa"))
			dayOfWeek = "Saturday";
		else if (day.equals("Su"))
			dayOfWeek = "Sunday";
		else
			dayOfWeek = "DNV"; /* Date not Valid */

		fullTime = clock;
		time = Integer.parseInt(fullTime.substring(0, fullTime.indexOf(":")));
		duration = callDur;
	} //PhoneCall(x, x, x)

	public void setNumber(String a) {
		number = a;
	} //setNumber()

	public String getNumber() {
		return number;
	} //getNumber()

	public String getDay() {
		return dayOfWeek;
	} //getDay()

	public String getFullTime() {
		return fullTime;
	} //getFullTime()

	public int getDuration() {
		return duration;
	} //getDuration()


	public double calculateBill() {
		if (dayOfWeek == "Monday" || dayOfWeek == "Tuesday" || dayOfWeek == "Wednesday" || dayOfWeek == "Thursday"
				|| dayOfWeek == "Friday") {
			if (time >= 8 && time < 18) {
				return duration * 0.40;
			} //if
			else if ((time >= 0 && time < 8) || (time >= 18 && time < 24)) {
				return duration * 0.25;
			} //else if
			else {
				System.err.print("Valid date, but not valid time");
				return -1;
			} //else
		} //if
		else if (dayOfWeek == "Saturday" || dayOfWeek == "Sunday") {
			return duration * 0.15;
		} //else if 
		else {
			System.err.println("Invalid day of the week, unknown cost");
			return -1;
		} //else
	} //calculateBill()
} //class
