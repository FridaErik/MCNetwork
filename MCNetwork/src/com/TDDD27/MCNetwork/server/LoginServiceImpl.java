package com.TDDD27.MCNetwork.server;

import com.TDDD27.MCNetwork.client.LoginService;
import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * Serverklass f�r all inloggningsfunktionalitet
 * H�mtar LoginInfo n�r anv�ndare loggar in
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
			//System.out.println("Login p� serversidan har registrerat att en anv�ndare �r inloggad");
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			loginInfo.setUserID(user.getUserId());
		} else {
			//System.out.println("Login p� serversidan har registrerat att ingen anv�ndare �r inloggad");
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
		//KOM IH�G TILL N�STA G�NG
		//OM loginInfo.loggedIn==true och loginInfo.UserID==UserID f�r en anv�ndare i databasen s� �r
		//en av v�ra anv�ndare inloggad annars beh�ver personen registrera sig. (P� klienten)
	}

}