package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.Picture;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlobServiceAsync {

	void getBlobStoreUploadUrl(AsyncCallback<String> asyncCallback);

	void getPicture(Long id, AsyncCallback<com.TDDD27.MCNetwork.shared.Picture> asyncCallback);

}
