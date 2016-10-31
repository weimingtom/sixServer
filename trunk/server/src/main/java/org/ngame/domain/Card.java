package org.ngame.domain;

import lombok.Data;
import org.rojo.annotations.Entity;
import org.rojo.annotations.Id;
import org.rojo.annotations.Index;
import org.rojo.annotations.Value;

@Data
@Entity(cache = true)
public class Card
{

  @Id
  private String id;
  @Index
  @Value
  private String pid;
  @Value
  private String initId;
  @Value
  private int qulity;//品质

}
