package com.dreamteam.lookme.bean;

import java.util.Date;

public class MessageItem {
	
	private boolean isMine;

	private String nodeId;

	private String message = "";

	private FileItem fileItem = null;

	private Date creationTime = new Date(System.currentTimeMillis());

	public MessageItem(String nodeId, String message, boolean isMine) {
		this.nodeId = nodeId;
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getNodeId() {
		if (isMine)
			return null;
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}