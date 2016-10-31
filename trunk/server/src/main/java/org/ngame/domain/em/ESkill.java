package org.ngame.domain.em; 
/**
 * 技能类型
 * @author sf
 *
 */
public enum ESkill {
	/**
	 *召唤
	 */
	SUMMON(1),
	/**
	 *治疗
	 */
	CURE(2),
	/**
	 *多重
	 */
	MULTIPLE(4),
	/**
	 *伤害
	 */
	HURT(6),
	/**
	 *转换(分解)
	 */
	CHANGE(7),
	/**
	 * 自杀
	 */
	SUICIDE(8),
	/**
	 * 冰冻
	 */
	FROZEN(14),
	/**
	 * 禁锢
	 */
	 IMPRISONMENT(15),
	 /**
	  * 隐身
	  */
	 STEALTH(16),
	 /**
	  * 护盾
	  */
	 PROTECT(17),
	 /**
	  * 移除隐身
	  */
	 UNSTEALTH(18),
	 /**
	  * 移除护盾
	  */
	 UNPROTECT(19);
	
	private int value;

	ESkill(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ESkill parse(int value) {
		for (ESkill type : ESkill.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}

}
 