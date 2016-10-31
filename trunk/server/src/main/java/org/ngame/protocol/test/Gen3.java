package org.ngame.protocol.test;

import java.io.File;
import java.util.List;

import org.ngame.Spring;
import org.ngame.data.DataBuilder;
import org.ngame.data.init.D_Base;
import org.ngame.data.init.D_Buff;
import org.ngame.data.init.D_Card;
import org.ngame.data.init.D_Skill;
import org.ngame.util.FileUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class Gen3 {
	public static void main(String[] args) {
		Spring.init();
		List<D_Card> card=DataBuilder.buildAll(D_Card.class);
		List<D_Buff> buff=DataBuilder.buildAll(D_Buff.class);
		List<D_Base> base=DataBuilder.buildAll(D_Base.class);
		List<D_Skill> skill=DataBuilder.buildAll(D_Skill.class);
		
		FileUtil.write(new File("./json/D_Card.json"), JSONArray.toJSONString(card));
		FileUtil.write(new File("./json/D_Buff.json"), JSONArray.toJSONString(buff));
		FileUtil.write(new File("./json/D_Base.json"), JSONArray.toJSONString(base));
		FileUtil.write(new File("./json/D_Skill.json"), JSONArray.toJSONString(skill));
		
	    System.out.println("succeed");
		
	}
}
