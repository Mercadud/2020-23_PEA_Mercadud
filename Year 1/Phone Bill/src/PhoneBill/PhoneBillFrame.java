package PhoneBill;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*The reason why I didnt make any test cases is because I am lazy and don't have enough time*/

public class PhoneBillFrame extends JFrame implements ActionListener {

	/**
	 * I added the following line because they asked me, not because I knew what I was doing
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel user;
	private JTextField userInput;
	private JTextArea resultDisplay;
	private JLabel question;
	private JButton button;

	private double totalCharge;
	private boolean success = false;

	public PhoneBillFrame() {
		setTitle("Monthly Phone Bill");

		user = new JPanel();
		getContentPane().add(user);

		question = new JLabel("Enter Phone number:");
		user.add(question);

		userInput = new JTextField(10);
		user.add(userInput);

		button = new JButton("Display Phone Bill");
		user.add(button);
		button.addActionListener(this);

		resultDisplay = new JTextArea(15, 20);
		getContentPane().add(resultDisplay, BorderLayout.SOUTH);
		resultDisplay.setEditable(false);
		resultDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
	} //PhoneBillFrame()

	public void actionPerformed(ActionEvent e) {

		if (userInput.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "You must enter a positive number", "Missing Number",
					JOptionPane.ERROR_MESSAGE);
			userInput.setText("ERROR");
		} //if
		else {
			File person = new File(userInput.getText() + ".txt");
			Scanner phoneUser = null;
			try {
				phoneUser = new Scanner(person);
				success = true;
			} //try
			catch (FileNotFoundException el) {
				JOptionPane.showMessageDialog(this, "File non existant", "Missing Number", JOptionPane.ERROR_MESSAGE);
				success = false;
			} //catch
			catch (IOException el) {
				JOptionPane.showMessageDialog(this, el.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
				success = false;
			} // catch (IOException)

			if (success) {
				PhoneCall phone = new PhoneCall();
				NumberFormat formatter = NumberFormat.getCurrencyInstance();

				resultDisplay.setText("Monthly Phone Bill\n");
				resultDisplay.append("Phone Number: " + userInput.getText() + "\n\n");
				resultDisplay.append("        Long Distance Charges\n");
				resultDisplay.append("Day        Start   Duration    Charge\n");
				resultDisplay.append("-------------------------------------\n");
				while (phoneUser.hasNext() && success) {
					phone = new PhoneCall(phoneUser.next(), phoneUser.next(), phoneUser.nextInt());
					resultDisplay.append(String.format("%-11s", phone.getDay()));
					resultDisplay.append(String.format("%5s", phone.getFullTime()));
					resultDisplay.append(String.format("%11d", phone.getDuration()));
					if (phone.calculateBill() != -1.00) {
						resultDisplay.append(String.format("%10s", formatter.format(phone.calculateBill())));
						totalCharge += phone.calculateBill();
					} //if() 
					else {
						JOptionPane.showMessageDialog(this, "Make sure you have the right format\n ex. Tu 15:32 32",
								"Error within file", JOptionPane.ERROR_MESSAGE);
						success = false;
					} //else
					resultDisplay.append("\n");
				} //while
				if (success) {

					resultDisplay.append("                            ---------\n");
					resultDisplay.append(String.format("%37s", formatter.format(totalCharge)));
					resultDisplay.append("\n\n");
					resultDisplay.append("               Basic Charge:   $17.65\n");
					resultDisplay.append("      Long Distance Charges:");
					resultDisplay.append(String.format("%9s%n", formatter.format(totalCharge)));
					resultDisplay.append("                 Amount Due:");
					resultDisplay.append(String.format("%9s", formatter.format(totalCharge + 17.65)));
				} //if(success)
				else {
					resultDisplay.setText("ERROR!");
				} //else
			} //if
			else {
				resultDisplay.setText("ERROR!");
			} //else
		} //else
	} //actionPerformed()

	public static void main(String[] args) {
		PhoneBillFrame frame = new PhoneBillFrame();

		frame.setSize(300, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} //main

} //class
