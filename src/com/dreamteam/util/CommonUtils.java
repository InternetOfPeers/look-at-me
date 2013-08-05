package com.dreamteam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Services;

public class CommonUtils {

	public static byte[] getBytes(Object obj) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectOutputStream os;

		try {
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toByteArray();
	}

	public static String generateChannelName(String myId, String profileId) {
		if (myId.compareTo(profileId) < 0)
			return myId + "_" + profileId;
		else
			return profileId + "_" + myId;

	}
	
	
	public static Node getNodeFromChannelName(String channelName) {
		String [] nodes = channelName.split("_");
		if(!Services.currentState.getMyBasicProfile().getId().equals(nodes[0]))
			return getNodeFromProfileId(nodes[0]);
		else return getNodeFromProfileId(nodes[1]);
	}
	
	public static Node getNodeFromProfileId(String profileId) {
		Collection<Node> nodeJoined =Services.currentState.getSocialNodeMap().values();
		Iterator<Node> iter = nodeJoined.iterator();
		while(iter.hasNext())
		{
			Node tempNode = iter.next();
			if(tempNode.getProfile().getId().equalsIgnoreCase(profileId))
				return tempNode;
		}
		Log.d("getNodeFromProfileId profile not found!profileId: "+profileId);
		return null;
	}
	
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	public static String timeElapsed(Date date1, Date date2)
	{
		Long diff=getDateDiff(date1, date2, TimeUnit.SECONDS);
		if(diff<=60)
			return "now";
		else if(getDateDiff(date1, date2, TimeUnit.MINUTES)<=60)
			return getDateDiff(date1, date2, TimeUnit.MINUTES) +" minutes ago";
		else if(getDateDiff(date1, date2, TimeUnit.DAYS)<=7)
			return getDateDiff(date1, date2, TimeUnit.MINUTES) +" days ago";
		else
		{	
		    SimpleDateFormat dfDate_day= new SimpleDateFormat("dd/MM/yyyy");
		    String dt="";
		    Calendar c = Calendar.getInstance(); 
		    return dfDate_day.format(c.getTime());			
		}
	}
	

}
