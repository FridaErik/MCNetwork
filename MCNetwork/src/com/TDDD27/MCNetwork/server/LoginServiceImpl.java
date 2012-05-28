package com.TDDD27.MCNetwork.server;

import com.TDDD27.MCNetwork.client.LoginService;
import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * Serverklass för all inloggningsfunktionalitet
 * Hämtar LoginInfo när användare loggar in
 * via sitt google-konto
 * @author Frida&Erik
 *
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	@Override
	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			//System.out.println("Login på serversidan har registrerat att en användare är inloggad");
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			loginInfo.setUserID(user.getUserId());
		} else {
			//System.out.println("Login på serversidan har registrerat att ingen användare är inloggad");
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
		//KOM IHÅG TILL NÄSTA GÅNG
		//OM loginInfo.loggedIn==true och loginInfo.UserID==UserID för en användare i databasen så är
		//en av våra användare inloggad annars behöver personen registrera sig. (På klienten)
	}

}