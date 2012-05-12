/**
 * 
 */
package com.TDDD27.MCNetwork.client;





import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;

import java.util.ArrayList;
import java.util.List;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/** Formlu�r f�r att l�gga
 * till ny anv�ndare, beh�ver l�nkas med
 * Google-konto
 * @author Frida
 *
 */
public class Userform extends FormPanel implements ValueChangeHandler {
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private Grid grid = new Grid(11, 3);
	//private MultiUploader  upload = new MultiUploader ();

	private TextBox textBoxFnamn = new TextBox();
	private HTML textHTMLFnamn = new HTML("<p>F&ouml;rnamn</p>", true);
	private HTML errorFnamn = new HTML("", true);
	private TextBox textBoxLnamn = new TextBox();
	private HTML textHTMLLnamn = new HTML("<p>Efternamn</p>", true);
	private HTML errorLnamn = new HTML("", true);
	private TextBox textBoxEmail = new TextBox();
	private HTML textHTMLEmail = new HTML("Email", true);
	private HTML errorEmail = new HTML("", true);
	private TextBox textBoxCity = new TextBox();
	private HTML textHTMLCity = new HTML("Stad", true);
	private HTML errorCity = new HTML("", true);
	private ListBox regionList;
	private HTML textHTMLRegion = new HTML("L&auml;n", true);
	private HTML errorRegion = new HTML("", true);
	private HTML textHTMLGender = new HTML("K&ouml;n", true);
	private RadioButton btn1 = new RadioButton("group", "Man");
	private RadioButton btn2 = new RadioButton("group", "Kvinna");
	private RadioButton btn3 = new RadioButton("group", "Vill ej ange");
	private HorizontalPanel radioBtnPanel = new HorizontalPanel();
	private HTML errorGender = new HTML("", true);

	private ListBox yearList;
	private HTML textHTMLBYear = new HTML("F&ouml;delse&aring;r", true);
	private HTML errorBYear = new HTML("", true);
	private TextBox textBoxMiles = new TextBox();
	private HTML textHTMLMiles = new HTML("Antal k&ouml;rda mil (ca)", true);
	private HTML errorMiles = new HTML("", true);


	DisclosurePanel MCDiscPanel = new DisclosurePanel();
	private Boolean submitOK = true;

	private HTML fileHTML = new HTML("Upload Something", true);
	private String bildPath ="";
	private Button submit = new Button("Submit");
	private LoginInfo loginInfo = null;


	@SuppressWarnings("unchecked")
	public Userform(MCNetwork theparent) {
		super();
		System.out.println("Skapar ny UserForm");
		parent=theparent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				setWidget(new HTML("<H1>Det verkar inte finnas en inloggad anv�ndare men d� ska inte fliken uppdatera" +
						" din uppgifter synas i menyn, n�t blir fel</H1>", true));
			}
			public void onSuccess(LoginInfo result) {
				System.out.println("Userform har registrerat att en anv�ndare �r inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID());
			}
		});


	}

	private MCUser getDBUser(String userID) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<MCUser>> callback = new AsyncCallback<ArrayList<MCUser>>() {
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(ArrayList<MCUser> result) {
				System.out.println("H�mtat DBUSer med ID fr�n Google");
				if(result==null){
					System.out.println("Anv�nderan finns inte i databasen");
					setEmptyForm();
				}
				else if(result.size()>1){
					System.out.println("Dubble ID i Databasen!!!!!!");
				}
				else{
					loggedInUser=result.get(0);
					setEmptyForm();
					System.out.println("Hittade en!!!!");
					fillForm(loggedInUser);
					//setfilledForm();
				}
			}
		};


		testService.getUserByID(userID, callback);
		return null;
	}



	@SuppressWarnings("deprecation")
	protected void fillForm(MCUser currentUser) {
		textBoxFnamn.setText(currentUser.getFirstName());
		textBoxLnamn.setText(currentUser.getLastName());
		textBoxEmail.setText(currentUser.geteMail());
		textBoxCity.setText(currentUser.getCity());
		int index1 = 0;
		for(int i=0; i<=regionList.getItemCount()-1; i++){
			if(regionList.getItemText(i).equals(currentUser.getRegion())){
				index1=i;
			}
		}
		regionList.setSelectedIndex(index1);
		if(currentUser.getGender().equals("Man")){
			btn1.setValue(true);
		}
		else if(currentUser.getGender().equals("Kvinna")) {
			btn2.setValue(true);
		}
		else{
			btn3.setValue(true);
		}
		int index2 = 0;
		for(int i=0; i<=yearList.getItemCount()-1; i++){
			if(Integer.parseInt(yearList.getItemText(i))==(currentUser.getBirthYear())){
				index2=i;
			}
		}
		yearList.setSelectedIndex(index2);
		textBoxMiles.setText(Integer.toString(currentUser.getMilesDriven()));
	}

	protected void clearUserForm() {
		textBoxFnamn.setText("");
		textBoxLnamn.setText("");
		textBoxEmail.setText("");
		textBoxCity.setText("");
		textBoxFnamn.setText("");
		textBoxMiles.setText("");
		this.clear();
		
		//this.removeFromParent();
	}
	protected void setSuccessText(String text){
		HTML SuccesLabel = new HTML("<H1>"+text+"</H1>", true);
		this.add(SuccesLabel);
	}
	private void addUser(final MCUser mcuser) {
		MCUser returnUser = null;
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<Long> callback = new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Long result) {
				clearUserForm();
				setSuccessText("Ny anv&auml;ndare tillagd, v�lkommen "+ mcuser.getFirstName());
			}
		};

		System.out.println("Region: "+ mcuser.getRegion());
		testService.storeUser(mcuser, callback);

	}


	private void setEmptyForm(){
		System.out.println("SetEmptyForm");
		// Add a finish handler which will load the image once the upload finishes
		//MultiUploader  upload = new MultiUploader ();
		//upload.addOnFinishUploadHandler(onFinishUploaderHandler);
		grid.addStyleName("MainUserForm");
		textBoxFnamn.setName("textBoxFnamn");
		textBoxLnamn.setName("textBoxLnamn");
		textBoxEmail.setName("textBoxEmail");
		textBoxCity.setName("textBoxCity");
		grid.setWidget(0, 0, textHTMLFnamn);
		grid.setWidget(0, 1, textBoxFnamn);
		grid.setWidget(0, 2, errorFnamn);
		grid.setWidget(1, 0, textHTMLLnamn);
		grid.setWidget(1, 1, textBoxLnamn);
		grid.setWidget(1, 2, errorLnamn);
		grid.setWidget(2, 0, textHTMLEmail);
		grid.setWidget(2, 1, textBoxEmail);
		grid.setWidget(2, 2, errorEmail);
		grid.setWidget(3, 0, textHTMLCity);
		grid.setWidget(3, 1, textBoxCity);
		grid.setWidget(3, 2, errorCity);
		regionList = getListBoxLan();
		grid.setWidget(4, 0, textHTMLRegion);
		grid.setWidget(4, 1, regionList);
		grid.setWidget(4, 2, errorRegion);
		grid.setWidget(5, 0, textHTMLGender);
		radioBtnPanel.add(btn1);
		radioBtnPanel.add(btn2);
		radioBtnPanel.add(btn3);
		grid.setWidget(5, 1, radioBtnPanel);
		grid.setWidget(5, 2, errorGender);
		grid.setWidget(6, 0, textHTMLBYear);
		yearList=getListBoxYears();
		grid.setWidget(6, 1, yearList);
		grid.setWidget(6, 2, errorBYear);
		grid.setWidget(7, 0, textHTMLMiles);
		grid.setWidget(7, 1, textBoxMiles);
		grid.setWidget(7, 2, errorMiles);
		//grid.setWidget(8, 0, fileHTML);
		//grid.setWidget(8, 1, upload);
		grid.setWidget(10, 0, submit);
		submit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();	
			}
		});

		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		setWidget(grid);
		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("registration");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("registration");
		History.fireCurrentHistoryState();		
		//HISTORY
		setStyleName("formPanel");

		addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {	
				//TODO fixa en update funktion s� om currentuder finns i databsen s� ska uppgifterna bara
				//uppdateras inte l�ggas till igen.

				submitOK = true;
				String fn = textBoxFnamn.getText();
				checkFName(fn);
				String ln = textBoxLnamn.getText();
				checkLName(ln);
				String em = textBoxEmail.getText();
				checkLEmail(em);
				String c = textBoxCity.getText();
				checkCity(c);
				String r = regionList.getItemText(regionList.getSelectedIndex());
				String g;
				if (btn1.getValue()){
					g="Man";
				}
				else if(btn2.getValue()){
					g="Kvinna";
				}
				else{
					g="ok�nd";
				}
				int by=0;
				try {
					by = Integer.parseInt(yearList.getItemText(yearList.getSelectedIndex()));
				} catch (NumberFormatException e) {
					errorBYear.setText("Ogiltigt &aring;rtal");
					submitOK=false;
				}
				int m=0;
				if(textBoxMiles.getValue()!= ""){
					try {
						m = Integer.parseInt(textBoxMiles.getValue());
					} catch (NumberFormatException e) {
						errorMiles.setText("Ogiltig input");
						submitOK=false;
					}

				}

				if(submitOK){
					MCUser mcuser = new MCUser(fn, ln, by, em, c, r, g, m, loginInfo.getUserID());
					if(loggedInUser==null){
						addUser(mcuser);
					}
					else{
						mcuser.setId(loggedInUser.getId());
						updateUser(mcuser);
					}
				}
			}

		});
	}
	protected void updateUser(final MCUser mcuser) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<Long> callback = new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Long result) {
				clearUserForm();
				setSuccessText(mcuser.getFirstName()+", din uppgifter �r uppdaterade!");
			}
		};
	
		testService.updateUser(mcuser, callback);

	}

	private ListBox getListBoxLan() {
		ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("Blekinge");
		widget.addItem("Dalarna");
		widget.addItem("Gotland");
		widget.addItem("Gavleborg");
		widget.addItem("Halland");
		widget.addItem("Jamtland");
		widget.addItem("Jonkoping");
		widget.addItem("Kalmar");
		widget.addItem("Kronoberg");
		widget.addItem("Norrbotten");
		widget.addItem("Skane");
		widget.addItem("Stockholm");
		widget.addItem("Sodermanland");
		widget.addItem("Uppsala");
		widget.addItem("Varmland");
		widget.addItem("Vasterbotten");
		widget.addItem("Vasternorrland");
		widget.addItem("Vastmanland");
		widget.addItem("Vastra Gotaland");
		widget.addItem("Orebro");
		widget.addItem("Ostergotland");
		widget.setVisibleItemCount(1);
		return widget;
	}

	private ListBox getListBoxYears() {
		ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("2002");
		widget.addItem("2001");
		widget.addItem("2000");
		widget.addItem("1999");
		widget.addItem("1998");
		widget.addItem("1997");
		widget.addItem("1996");
		widget.addItem("1995");
		widget.addItem("1994");
		widget.addItem("1993");
		widget.addItem("1992");
		widget.addItem("1991");
		widget.addItem("1990");
		widget.addItem("1989");
		widget.addItem("1988");
		widget.addItem("1987");
		widget.addItem("1986");
		widget.addItem("1985");
		widget.addItem("1984");
		widget.addItem("1983");
		widget.addItem("1982");
		widget.addItem("1981");
		widget.addItem("1980");
		widget.addItem("1979");
		widget.addItem("1978");
		widget.addItem("1977");
		widget.addItem("1976");
		widget.addItem("1975");
		widget.addItem("1974");
		widget.addItem("1973");
		widget.addItem("1972");
		widget.addItem("1971");
		widget.addItem("1970");
		widget.addItem("1969");
		widget.addItem("1968");
		widget.addItem("1967");
		widget.addItem("1966");
		widget.addItem("1965");
		widget.addItem("1964");
		widget.addItem("1963");
		widget.addItem("1962");
		widget.addItem("1961");
		widget.addItem("1960");
		widget.addItem("1959");
		widget.addItem("1958");
		widget.addItem("1957");
		widget.addItem("1956");
		widget.addItem("1955");
		widget.addItem("1954");
		widget.addItem("1953");
		widget.addItem("1952");
		widget.addItem("1951");
		widget.addItem("1950");
		widget.addItem("1949");
		widget.addItem("1948");
		widget.addItem("1947");
		widget.addItem("1946");
		widget.addItem("1945");
		widget.addItem("1944");
		widget.addItem("1943");
		widget.addItem("1942");
		widget.addItem("1941");
		widget.addItem("1940");
		widget.addItem("1939");
		widget.addItem("1938");
		widget.addItem("1937");
		widget.addItem("1936");
		widget.addItem("1935");
		widget.addItem("1934");
		widget.addItem("1933");
		widget.addItem("1932");
		widget.addItem("1931");
		widget.addItem("1930");
		widget.setVisibleItemCount(1);
		return widget;
	}

	@Override
	public void onValueChange(ValueChangeEvent event) {
		/*if (event.getValue().equals("start")){
            parent.centerPanel.clear();
            parent.centerPanel.add(parent.centerwidget);
        }*/
		if (event.getValue().equals("registration")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}
	private void checkUrl(String url) {
		//TODO
	}
	private void checkCity(String city) {
		boolean valid = city.matches("[a-�A-�]*");	
		if(!valid){
			errorCity.setText("Ogiltigt stadsnamn");
			submitOK=false;
		}
	}

	protected void checkLEmail(String em) {
		String[] tokens = em.split("@");
		if(tokens.length != 2 ||
				tokens[0].isEmpty() || 
				tokens[1].isEmpty() ){
			errorEmail.setText("Ogiltig email");
			submitOK=false;
		}

	}
	protected void checkFName(String text) {
		boolean valid = text.matches("[a-�A-�]*");	
		if(!valid || text.equals("") || text.length()<2){
			errorFnamn.setText("Ogiltigt namn");
			submitOK=false;
		}
	}
	protected void checkLName(String text) {
		boolean valid = text.matches("[a-�A-�]*");	
		if(!valid || text.equals("") || text.length()<2){
			errorLnamn.setText("Ogiltigt namn");
			submitOK=false;
		}
	}


}
