package org.ngame.domain.em; 
//buff type
public enum EBuff {
	/**
	 * 加攻:
	 */
	ADDATT(1),
	/**
	 * 加血
	 */
	ADDBLOOD(2),
	/**
	 * 指定血量
	 */
	ASSIGNBOOLD(3),
	/**
	 * 指定攻击
	 */
	ASSIGNATT(4);
	
	private int value;

	EBuff(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static EBuff parse(int value) {
		for (EBuff type : EBuff.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

}
 