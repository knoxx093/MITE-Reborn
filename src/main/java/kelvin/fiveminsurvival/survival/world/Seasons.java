package kelvin.fiveminsurvival.survival.world;

public class Seasons {
	public static final int SPRING = 0, SUMMER = 1, WINTER = 2, FALL = 3;
	
	public static int getSeason(double day) {
		double year = 365.25;
		double eighth = year / 8.0;
		double quarter = year / 4.0;
		if (day < eighth || day > year - eighth) {
			return Seasons.WINTER;
		}
		if (day > eighth && day < eighth + quarter) {
			return Seasons.SPRING;
		}
		if (day > eighth + quarter && day < eighth + quarter * 2) {
			return Seasons.SUMMER;
		}
		return Seasons.FALL;
	}
}
