package DataSomething;

import java.util.StringTokenizer;

public class Header {

	private String title;
	private int lineSize;

	public Header(String t, int w) {
		title = t;
		lineSize = w;
	}

	public String getCentredTitle() {
		StringBuffer head = new StringBuffer();
		StringTokenizer headingLine = new StringTokenizer(title, "~");
		while (headingLine.hasMoreTokens()) {
			String titleToken = headingLine.nextToken();
			int endOfTitle = (lineSize / 2) + (titleToken.length() / 2);
			head.append(String.format("%" + endOfTitle + "s\n", titleToken));
		}
		head.append("\n");

		return head.toString();
	}
	
	public String getCentredTitleUnderlined()
	{
		StringBuffer head = new StringBuffer();
		StringBuffer underline;

		StringTokenizer headingLine = new StringTokenizer(title, "~");

		while (headingLine.hasMoreTokens())
		{
			String titleToken = headingLine.nextToken();
			int endOfTitle = (lineSize / 2) + (titleToken.length() / 2);
			head.append(String.format("%" + endOfTitle + "s\n", titleToken));
			underline = new StringBuffer();
			for (int i = 0; i < titleToken.length(); ++i)
				underline.append("-");
			head.append(String.format("%" + endOfTitle + "s\n", underline));
		}
		head.append("\n");

		return head.toString();
	}
}
