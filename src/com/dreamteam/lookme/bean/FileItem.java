package com.dreamteam.lookme.bean;


public class FileItem {
    String exchangeId = null;

	int progress = 0;

    public FileItem(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    
    public int getProgress() {
        return progress;
    }   
    

    public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}    
}