package DataSomething;

public class Music {
	private int year;
	private int rank;
	private String artist;
	private String song;
	private int downloads;

	public Music(int y, int r, String a, String s, int d) {
		year = y;
		rank = r;
		artist = a;
		song = s;
		downloads = d;
	}

	public int getYear() {
		return year;
	}

	public int getRank() {
		return rank;
	}

	public String getArtist() {
		return artist;
	}

	public String getSong() {
		return song;
	}

	public int getDownloads() {
		return downloads;
	}
}
