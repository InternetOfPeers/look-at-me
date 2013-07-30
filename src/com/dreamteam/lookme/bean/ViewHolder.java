package com.dreamteam.lookme.bean;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder {
    TextView myNodeName;

    LinearLayout chatLayout;

    TextView chatMessage;

    LinearLayout fileLayout;

    LinearLayout progressLayout;

    ProgressBar progressBar;

    Button fileCancelBtn;

    TextView yourNodeName;

	public TextView getMyNodeName() {
		return myNodeName;
	}

	public void setMyNodeName(TextView myNodeName) {
		this.myNodeName = myNodeName;
	}

	public LinearLayout getChatLayout() {
		return chatLayout;
	}

	public void setChatLayout(LinearLayout chatLayout) {
		this.chatLayout = chatLayout;
	}

	public TextView getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(TextView chatMessage) {
		this.chatMessage = chatMessage;
	}

	public LinearLayout getFileLayout() {
		return fileLayout;
	}

	public void setFileLayout(LinearLayout fileLayout) {
		this.fileLayout = fileLayout;
	}

	public LinearLayout getProgressLayout() {
		return progressLayout;
	}

	public void setProgressLayout(LinearLayout progressLayout) {
		this.progressLayout = progressLayout;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public Button getFileCancelBtn() {
		return fileCancelBtn;
	}

	public void setFileCancelBtn(Button fileCancelBtn) {
		this.fileCancelBtn = fileCancelBtn;
	}

	public TextView getYourNodeName() {
		return yourNodeName;
	}

	public void setYourNodeName(TextView yourNodeName) {
		this.yourNodeName = yourNodeName;
	}
}
