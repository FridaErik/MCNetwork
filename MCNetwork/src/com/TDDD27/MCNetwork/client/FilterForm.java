/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;
import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * @author Frida
 *
 */
public class FilterForm extends FormPanel{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private VerticalPanel formframe = new VerticalPanel();
	private ScrollPanel resultframe = new ScrollPanel();
	private HorizontalPanel mainframe = new HorizontalPanel();
	
	private Grid grid = new Grid(13, 3);
	private final FlexTable resultTable = new FlexTable();
	private HTML textHTMLFname = new HTML("<filterH2>F&ouml;rnamn:</filterH2>", true);
	private HTML textHTMLLname = new HTML("<filterH2>Efternamn:</filterH2>", true);
	private TextBox fNameBox=new TextBox();
	private TextBox lNameBox=new TextBox();
	private HTML textHTMLFYear = new HTML("<filterH2>F&ouml;delse&aring;r:</filterH2>", true);
	private HTML textHTMLFYearUp = new HTML("<filterp>&Oumlvre gr&auml;ns</filterp>", true);
	private HTML textHTMLFYearDown = new HTML("<filterp>Undre gr&auml;ns</filterp>", true);
	private ListBox yearList1;
	private ListBox yearList2;
	private HTML textHTMLLan = new HTML("<filterH2>L&auml;n:</filterH2>", true);
	private ListBox lanList;
	private HTML textHTMLMiles = new HTML("<filterH2>K&ouml;rda mil:</filterH2>", true);
	private HTML textHTMLMilesUp = new HTML("<filterp>&Oumlvre gr&auml;ns</filterp>", true);
	private HTML textHTMLMilesDown = new HTML("<filterp>Undre gr&auml;ns</filterp>", true);
	private TextBox milesUpBox=new TextBox();
	private TextBox milesDownBox=new TextBox();
	private HTML textHTMLCity = new HTML("<filterH2>Stad:</filterH2>", true);
	private TextBox cityBox=new TextBox();
	private HTML errorOnSubmit = new HTML("", true);
	private MCNetwork myParent;
	/**
	 * 
	 */
	public FilterForm(MCNetwork parent) {
		super();
		myParent=parent;
		HTML titleText = new HTML("<FilterH1>H&auml;r kan du hitta andra medlemmar</FilterH1>", true);
		HTML infoText = new HTML("<p>Ange parametrar f&ouml;r att begr&auml;nsa s&ouml;komr&aring;det. " +
				"De parametrar du l&auml;mnar som de &auml;r begr&auml;nsar inte s&ouml;kningen", true);
		infoText.setWidth("290px");


		grid.addStyleName("MainFilterForm");
		milesUpBox.setWidth("40px");
		milesDownBox.setWidth("40px");

		yearList1=getListBoxYears();
		yearList1.setItemSelected(0, true);
		yearList2=getListBoxYears();
		yearList2.setItemSelected(72, true);
		grid.setWidget(0, 0, textHTMLFname);
		grid.setWidget(0, 1, textHTMLLname);
		grid.setWidget(1, 0, fNameBox);
		grid.setWidget(1, 1, lNameBox);
		grid.setWidget(2, 0, textHTMLFYear);
		grid.setWidget(3, 0, textHTMLFYearUp);
		grid.setWidget(3, 1, textHTMLFYearDown);
		grid.setWidget(4, 0, yearList1);
		grid.setWidget(4, 1, yearList2);
		lanList=getListBoxLan();
		grid.setWidget(5, 0, textHTMLLan);
		grid.setWidget(6, 0, lanList);
		grid.setWidget(7, 0, textHTMLCity);
		grid.setWidget(7, 1, cityBox);
		grid.setWidget(8, 0, textHTMLMiles);
		grid.setWidget(9, 1, textHTMLMilesUp);
		grid.setWidget(9, 0, textHTMLMilesDown);
		grid.setWidget(10, 1, milesUpBox);
		grid.setWidget(10, 0, milesDownBox);
		Button submit = new Button("Hitta nya personer >>");
		grid.setWidget(11, 0, submit);

		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		formframe.add(titleText);
		formframe.add(infoText);
		formframe.add(grid);
		formframe.setWidth("290px");
		mainframe.add(formframe);
		setWidget(mainframe);

		submit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();	
			}
		});

		addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				errorOnSubmit.setHTML("");
				Boolean submitOK = true;
				int yearup = 0;
				int yeardown = 0;
				int milesup = 0;
				int milesdown = 0;
				if(milesUpBox.getValue()=="" || milesDownBox.getValue()==""){
					try {
						yearup = Integer.parseInt(yearList1.getValue(yearList1.getSelectedIndex()));
						yeardown = Integer.parseInt(yearList2.getValue(yearList2.getSelectedIndex()));
						milesup = 999999999;
						milesdown = 0;
					} catch (NumberFormatException e) {
						submitOK =false;
						System.out.println("N�nting gick snett n�r v�rdena fr�n ListBox, f�rs�k 1");
					}
				}
				else{
					try {
						yearup = Integer.parseInt(yearList1.getValue(yearList1.getSelectedIndex()));
						yeardown = Integer.parseInt(yearList2.getValue(yearList2.getSelectedIndex()));
						milesup = Integer.parseInt(milesUpBox.getValue());
						milesdown = Integer.parseInt(milesDownBox.getValue());
					} catch (NumberFormatException e) {
						submitOK =false;
						System.out.println("N�nting gick snett n�r v�rdena fr�n ListBox f�rs�k 2");
						errorOnSubmit = new HTML("<error>De parametrar du angett &auml;r ej giltiga, kontrollera s&aring; du angett siffor ej text f&ouml;r k&ouml;rda mil</error>", true);
						formframe.add(errorOnSubmit);
					}
				}
				String fname = fNameBox.getText();
				String lname = lNameBox.getText();
				String lan = lanList.getValue(lanList.getSelectedIndex());
				String city = cityBox.getText();
				submitOK = checkInput(fname, lname, city, yearup, yeardown, milesup, milesdown);


				if(submitOK){
					resultTable.clear();
					sendSearchParameters(yearup, yeardown, milesup, milesdown, lan, city, lname, fname);
				}
				else{
					errorOnSubmit = new HTML("<error>De parametrar du angett &auml;r ej giltiga, kontrollera s&aring; &ouml;vre gr&auml;ns > undre gr&auml;ns samt stadsnamnet</error>", true);
					formframe.add(errorOnSubmit);
				}

			}
		});

	}

	protected void sendSearchParameters(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String lname, String fname) {
		System.out.println("So far so good");
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<ArrayList<User>> callback = new AsyncCallback<ArrayList<User>>() {
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				addResult(result);
			}
		};


		testService.searchUsers(yearup, yeardown, milesup, milesdown, lan, city, fname, lname, callback);

	}

	protected void addResult(final List<User> result) {
		
		ClickHandler userRowCheck = new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell src = resultTable.getCellForEvent(event);
				int rowIndex = src.getRowIndex();
				System.out.println("Cell Selected: userRowCheck Handler, rowIndex: " + rowIndex);
				User user = result.get(rowIndex-1);
				System.out.println("User id: " + user.getId());
				SendToUserPage(user);
			}
		};
		resultTable.addClickHandler(userRowCheck);
		resultTable.setStyleName("resultTable");
		resultframe.setStyleName("resultFrame");
		resultTable.setWidget(0, 0, new HTML("F&ouml;rnamn:", true));
		resultTable.getCellFormatter().addStyleName(0, 0, "resultTableHeader");
		resultTable.setWidget(0, 1, new HTML("Efternamn:", true));
		resultTable.getCellFormatter().addStyleName(0, 1, "resultTableHeader");
		resultTable.setWidget(0, 2, new HTML("F&ouml;delse&aring;r:", true));
		resultTable.getCellFormatter().addStyleName(0, 2, "resultTableHeader");
		resultTable.setWidget(0, 3, new HTML("L&auml;n:", true));
		resultTable.getCellFormatter().addStyleName(0, 3, "resultTableHeader");
		resultTable.setWidget(0, 4, new HTML("Bostadsort:", true));
		resultTable.getCellFormatter().addStyleName(0, 4, "resultTableHeader");
		resultTable.setWidget(0, 5, new HTML("K&ouml;rda mil:", true));
		resultTable.getCellFormatter().addStyleName(0, 5, "resultTableHeader");
		if(!result.isEmpty()){
			for(int i=1; i<=result.size(); i++){
				resultTable.getRowFormatter().addStyleName(i, "resultTableRowStyle");
				HTML fnameHTML = new HTML(result.get(i-1).getFirstName(), true);
				resultTable.setWidget(i, 0, fnameHTML);
				HTML lnameHTML = new HTML(result.get(i-1).getLastName(), true);
				resultTable.setWidget(i, 1, lnameHTML);
				HTML bYearHTML = new HTML(Integer.toString(result.get(i-1).getBirthYear()), true);
				resultTable.setWidget(i, 2, bYearHTML);
				HTML regionHTML = new HTML(result.get(i-1).getRegion(), true);
				resultTable.setWidget(i, 3, regionHTML);
				HTML cityHTML = new HTML(result.get(i-1).getCity(), true);
				resultTable.setWidget(i, 4, cityHTML);
				HTML milesHTML = new HTML(Integer.toString(result.get(i-1).getMilesDriven()), true);
				resultTable.setWidget(i, 5, milesHTML);
			}
		}
		resultframe.add(resultTable);
		resultframe.setSize("430px", "450px");
		mainframe.add(resultframe);


	}

	protected void SendToUserPage(User user) {
		UserView centerwidget = new UserView(user);
		myParent.centerPanel.clear();
		myParent.centerPanel.add(centerwidget);

	}

	protected Boolean checkInput(String fname, String lname, String city, int yearup, int yeardown, int milesup, int milesdown) {
		boolean validcity = city.matches("[a-�A-�]*");	
		boolean validfname = fname.matches("[a-�A-�]*");
		boolean validlname = lname.matches("[a-�A-�]*");	
		boolean validyearlimits = yearup>=yeardown;
		boolean validmileslimits = milesup>=milesdown;
		if(!validcity || !validyearlimits || !validmileslimits || !validfname || !validlname){
			return false;
		}
		return true;
	}



	private ListBox getListBoxLan() {
		ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("Alla");
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
		widget.addItem("Vasta Gotaland");
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

}
