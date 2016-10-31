package org.ngame.domain.em; 
/**
 * @author:jh
 * @Date:2016年7月20日上午11:00:07
 */
public enum ETrigger {
	/**
	 *进场时
	 */
	ENTER(1),
	/**
	 *死亡时
	 */
	DEATH(2),
	/**
	 *攻击时
	 */
	ATTACK(3),
	/**
	 *被攻击时
	 */
	ATTACKED(4),
	/**
	 * 你的回合开始时
	 */
	ROUNDBEGIN(5),
	/**
	 * 杀死地方时
	 */
	BEKILLED(6),
	/**
	 * 基地行动时
	 */
	RACEACTION(7);
	
	private int value;

	ETrigger(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ETrigger parse(int value) {
		for (ETrigger type : ETrigger.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

}
 