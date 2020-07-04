package com.rainiersoft.ioclparadip.igs;

import java.util.ArrayList;
import java.util.List;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;

public class RegisterEvents implements IRegisterEvents 
{
	
	private List<PollEvent> eventPollThreads = new ArrayList<PollEvent> ();
	IEventStatusChangedListener eventListner;
	
	  public void registerEvent (Tag tag, int secs, IEventStatusChangedListener listner)
	  {	
		PollEvent pollEvent = new PollEvent(tag, secs,  listner);
		eventPollThreads.add(pollEvent);
	  }

	  
	 	public RegisterEvents () 
	 	{
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay01_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay01_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay02_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay02_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay03_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay03_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay04_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay04_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay05_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay05_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay06_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay06_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay07_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay07_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay08_PinCodeValidation.Req", "0", new TagType("1bit boolean"), "Bay08_PinCodeValidation.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay01_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay01_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay02_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay02_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay03_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay03_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay04_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay04_TruckWeight.Req"), 10, eventListner));
			eventPollThreads.add(new PollEvent(new Tag ("Bay05_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay05_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay06_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay06_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay07_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay07_TruckWeight.Req"), 10, eventListner));
	 		eventPollThreads.add(new PollEvent(new Tag ("Bay08_TruckWeight.Req", "0", new TagType("1bit boolean"), "Bay08_TruckWeight.Req"), 10, eventListner));
	 		
	 		
	   	}

	  public void StartPolling ()
	  {
			if (eventPollThreads.size() > 0)
			{
 				for (PollEvent pe : eventPollThreads)
					 pe.start();
			}
		  
	  }
	  
	  public void StopPolling ()
	  {
		  
	  }
  
}
