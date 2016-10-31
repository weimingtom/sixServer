package org.ngame.domain.em; 

public enum ETarget {
	/**
	 * 没有
	 */
	NONE(1),
	/**
	 * 敌方指定
	 */
	ENEMYASSIGN(2),
	/**
	 * 我方指定
	 */
	OURASSIGN(3),
	/**
	 * 敌方全体
	 */
	ENEMYALL(4),
	/**
	 * 我方全体
	 */
	OURALL(5),
	/**
	 * 全体
	 */
	ALL(6),
	/**
	 * 我方目标和相邻
	 */
	OURSIDE(7),
	/**
	 * 敌方目标和相邻
	 */
	ENEMYSIDE(8),
	/**
	 * 我方目标和随机相邻
	 */
	OURRANDOMSIDE(9),
	/**
	 * 敌方目标和随机相邻
	 */
	ENEMYRANDOMSIDE(10),
	/**
	 * 自己
	 */
	OUR(11),
	/**
	 * 随机敌方俩个目标
	 */
	RANDOMTWO(12),
	/**
	 * 指定一个目标
	 */
	ASSIGN(13);
	
	private int value;

	ETarget(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ETarget parse(int value) {
		for (ETarget type : ETarget.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

}
 