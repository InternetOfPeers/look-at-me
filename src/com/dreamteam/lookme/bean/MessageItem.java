package com.dreamteam.lookme.bean;

import java.util.Date;

import com.dreamteam.lookme.communication.LookAtMeNode;

public class MessageItem {
	private boolean isMine;

	private LookAtMeNode node;

	private String message = "";

    private FileItem fileItem = null;
    
    private Date creationTime=new Date(System.currentTimeMillis());

	public MessageItem(LookAtMeNode node, String message,boolean isMine) {
        this.node = node;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LookAtMeNode getLookAtMeNode() {
    	if(isMine)
    		return null;        		
        return node;
    }
           

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem item) {
        fileItem = item;
    }
    	
    public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}    
	
    public LookAtMeNode getNode() {
		return node;
	}

	public void setNode(LookAtMeNode node) {
		this.node = node;
	}	
	

    public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}	

}