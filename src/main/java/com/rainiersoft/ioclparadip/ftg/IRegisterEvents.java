package com.rainiersoft.ioclparadip.ftg;

import com.rainiersoft.core.*;

public interface IRegisterEvents 
{ 
  public void registerEvent (Tag tag, int secs, IEventStatusChangedListener listner);
  
}
