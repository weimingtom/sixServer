package org.ngame.domain;

import lombok.Data;

import java.util.List;

import org.ngame.protocol.domain.CardGroupInfo;
import org.rojo.annotations.Entity;
import org.rojo.annotations.Id;
import org.rojo.annotations.Index;
import org.rojo.annotations.Value;

@Data
@Entity(cache = true)
public class CardGroup
{

  @Id
  private String id;
  @Index
  @Value
  private String pid;
  @Value
  private String name;
  @Value
  private String baseInitId;
  @Value
  private List<String> cardIds;

  
  public CardGroupInfo change(){
	  CardGroupInfo cgi=new CardGroupInfo();
	  cgi.setId(this.id);
	  cgi.setPid(this.pid);
	  cgi.setName(this.name);
	  cgi.setBaseInitId(this.baseInitId);
	  cgi.setCardIds(cardIds);
	  return cgi;
  }
}
