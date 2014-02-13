package com.brainmote.lookatme.service;

import android.content.Context;

import com.samsung.android.sdk.SsdkUnsupportedException;

public interface GroupPlayManager {

	void init(Context context) throws SsdkUnsupportedException;

	boolean isReady();

}
