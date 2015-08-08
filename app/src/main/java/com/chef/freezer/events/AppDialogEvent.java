package com.chef.freezer.events;
import com.chef.freezer.loader.AppCard;

public class AppDialogEvent
{
	
	public AppCard ac;
	
	public AppDialogEvent(AppCard a){
		this.ac = a;
	}
}
