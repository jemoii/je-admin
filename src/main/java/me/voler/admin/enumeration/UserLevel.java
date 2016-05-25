package me.voler.admin.enumeration;

public enum UserLevel {

	STUDENT(1), TEACHER(2), ADMINISTRATOR(3);

	private int level;

	public final int getLevel() {
		return level;
	}

	UserLevel(int level) {
		this.level = level;
	}

}
