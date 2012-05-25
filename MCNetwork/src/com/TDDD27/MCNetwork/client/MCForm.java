package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
/**
 * FormPanel för att registrera motorcyklar (MC)
 * @author FridaErik
 *
 */
public class MCForm extends FormPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private Grid grid = new Grid(6, 3);
	//private FileUpload upload = new FileUpload();
	private TextBox textBoxBrand = new TextBox();
	private HTML textLabelBrand = new HTML("M&auml;rke", true);
	private Label errorBrand = new Label("");
	private TextBox textBoxModel = new TextBox();
	private HTML textLabelModel = new HTML("Model	", true);
	private Label errorModel = new Label("");
	private TextBox textBoxYear = new TextBox();
	private HTML textLabelYear = new HTML("&Aring;r", true);
	private Label errorYear = new Label("");
	private TextBox textBoxUrl = new TextBox();
	private HTML textLabelUrl = new HTML("L&auml;nk f&ouml;r info om modellen", true);
	private Label errorUrl = new Label("");
	private Button submit = new Button("Submit");
	private Boolean submitOK = true;
	private HTML response = new HTML("", true);

	private MCNetwork parent;

	public MCForm(final MC MC, final MCUser loggedInUser, MCNetwork parent, final Boolean edit) {
		super();
		this.parent=parent;
		textBoxBrand.setName("textBoxBrand");
		textBoxModel.setName("textBoxModel");
		textBoxYear.setName("textBoxYear");
		textBoxUrl.setName("textBoxUrl");
		if(MC!=null){
			
			textBoxBrand.setText(MC.getBrand());
			textBoxModel.setText(MC.getModel());
			textBoxYear.setText(Integer.toString(MC.getYear()));
			textBoxUrl.setText(MC.getUrl());
		}

		grid.setWidget(0, 0, textLabelBrand);
		grid.setWidget(0, 1, textBoxBrand);
		grid.setWidget(0, 2, errorBrand);
		grid.setWidget(1, 0, textLabelModel);
		grid.setWidget(1, 1, textBoxModel);
		grid.setWidget(1, 2, errorModel);
		grid.setWidget(2, 0, textLabelYear);
		grid.setWidget(2, 1, textBoxYear);
		grid.setWidget(2, 2, errorYear);
		grid.setWidget(3, 0, textLabelUrl);
		grid.setWidget(3, 1, textBoxUrl);
		grid.setWidget(3, 2, errorUrl);

		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		//setWidget(grid);
		setStyleName("formPanel");
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				submit();	
			}
		});
		grid.setWidget(4, 1, submit);
		grid.setWidget(5, 0, response);
		this.add(grid);

		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("mcForm");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("mcForm");
		History.fireCurrentHistoryState();		
		//HISTORY
		//this.add(submit);

		//Testar en grej
		addSubmitHandler(new SubmitHandler() {
			/**
			 * Hämtar text och validerar innan det skickas till servern
			 */
			@Override
			public void onSubmit(SubmitEvent event) {	

				submitOK = true;
				String brand = textBoxBrand.getText();
				String model = textBoxModel.getText();
				int year=0;
				String url = textBoxUrl.getText();
				//validering
				checkBrand(brand);
				checkModel(model);
				if(textBoxYear.getValue()!= ""){
					try {
						year = Integer.parseInt(textBoxYear.getValue());
					} catch (NumberFormatException e) {
						errorYear.setText("Ogiltig input");
						submitOK=false;
					}
				}

				if(submitOK){
					//Skicka in MC plus anv‰ndare till DB
					
					if (!edit){
						MC newMC = new MC(brand, model, year, url);
						storeMC(newMC, loggedInUser);
					}
					if (edit){
						MC.setBrand(brand);
						MC.setModel(model);
						MC.setYear(year);
						MC.setUrl(url);
						updateMC(MC, loggedInUser);
					}
				}
			}
		});
	}
	
	private void storeMC(MC mc,	MCUser loggedInUser) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Boolean result) {
				parent.centerPanel.clear();
				parent.centerPanel.add(new MotorcyklarView(parent));
			}
		};

		testService.storeMC(mc, loggedInUser, callback);

	}
	private void updateMC(MC mc, MCUser loggedInUser) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(Boolean result) {
				parent.centerPanel.clear();
				parent.centerPanel.add(new MotorcyklarView(parent));
			}
		};

		testService.updateMC(mc, loggedInUser, callback);
	}

	/**
	 * Validering av brand
	 * @param brand
	 */
	private void checkBrand(String brand) {
		boolean valid = brand.matches("[a-öA-Ö]*");	
		if(!valid){
			errorBrand.setText("Ogiltigt m&auml;rke");
			submitOK=false;
		}
	}
	/**
	 * Validering av Model
	 * @param model
	 */
	private void checkModel(String model) {
		boolean valid = model.matches("[a-öA-Ö0-9]*");	
		if(!valid){
			errorModel.setText("Ogiltigt modellnamn");
			submitOK=false;
		}
	}
	//Getters och setters
	public String getBrand() {
		return textBoxBrand.getText();
	}

	public void setBrand(String brand) {
		this.textBoxBrand.setText(brand);
	}

	public String getErrorBrand() {
		return errorBrand.getText();
	}

	public void setErrorBrand(String error) {
		this.errorBrand.setText(error);
	}

	public String getModel() {
		return textBoxModel.getText();
	}

	public void setModel(String model) {
		this.textBoxModel.setText(model);
	}

	public String getErrorModel() {
		return errorModel.getText();
	}

	public void setErrorModel(String error) {
		this.errorModel.setText(error);
	}

	public String getYear() {
		return textBoxYear.getText();
	}

	public void setYear(String textBoxYear) {
		this.textBoxYear.setText(textBoxYear);
	}

	public String getErrorYear() {
		return errorYear.getText();
	}

	public void setErrorYear(String errorYear) {
		this.errorYear.setText(errorYear);
	}

	public String getUrl() {
		return textBoxUrl.getText();
	}

	public void setUrl(String textBoxUrl) {
		this.textBoxUrl.setText(textBoxUrl);
	}

	public String getErrorUrl() {
		return errorUrl.getText();
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl.setText(errorUrl);
	}

	public void clearFields() {
		textBoxBrand.setText("");
		textBoxModel.setText("");
		textBoxYear.setText("");
		textBoxUrl.setText("");


	}
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("mcForm")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}
	}




}
