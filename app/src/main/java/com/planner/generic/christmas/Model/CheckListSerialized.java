package com.planner.generic.christmas.Model;

import java.io.Serializable;
import java.util.List;

public class CheckListSerialized  implements Serializable {
  public List<Task> tasks;
  public Location location;
  public Long targetTimeStamp;
}
