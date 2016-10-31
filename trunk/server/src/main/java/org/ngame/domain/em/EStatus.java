package org.ngame.domain.em; 
//buff type
public enum EStatus {
	/**
	 * 冰冻
	 */
	FROZEN(1),
	/**
	 * 禁锢
	 */
	IMPRISONMENT(2),
	/**
	 * 潜伏
	 */
	STEALTH(3),
	/**
	 * 护盾
	 */
	PROTECT(4);
	
	private int value;

	EStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static EStatus parse(int value) {
		for (EStatus type : EStatus.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

}
 