package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}