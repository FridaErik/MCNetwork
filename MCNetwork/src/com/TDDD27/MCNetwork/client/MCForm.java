package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.MC;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class MCForm extends FormPanel{

	private Grid grid = new Grid(4, 3);
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

	public MCForm(MC MC, MCNetwork parent) {
		super();

		textBoxBrand.setName("textBoxBrand");
		textBoxModel.setName("textBoxModel");
		textBoxYear.setName("textBoxYear");
		textBoxUrl.setName("textBoxUrl");
		
		textBoxBrand.setText(MC.getBrand());
		textBoxModel.setText(MC.getModel());
		textBoxYear.setText(Integer.toString(MC.getYear()));
		textBoxUrl.setText(MC.getUrl());

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
		setWidget(grid);
		setStyleName("formPanel");
		submit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();	
			}
		});
		
		this.add(grid);
		this.add(submit);


	}

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




}
