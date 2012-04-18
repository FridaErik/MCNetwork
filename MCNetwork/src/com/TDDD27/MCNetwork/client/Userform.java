/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Frida
 *
 */
public class Userform extends FormPanel {
	//SNälla
	private Grid grid = new Grid(11, 2);
	private FileUpload upload = new FileUpload();
	private TextBox textBoxFnamn = new TextBox();
	private Label textLabelFnamn = new Label("Förnamn");
	private TextBox textBoxLnamn = new TextBox();
	private Label textLabelLnamn = new Label("Efternamn");
	private TextBox textBoxEmail = new TextBox();
	private Label textLabelEmail = new Label("Email");
	private TextBox textBoxCity = new TextBox();
	private Label textLabelCity = new Label("Stad");
	
	private Label textLabelGender = new Label("Kön");
	private RadioButton btn1 = new RadioButton("group", "Man");
	private RadioButton btn2 = new RadioButton("group", "Kvinna");
	private RadioButton btn3 = new RadioButton("group", "Okänd");
	private HorizontalPanel radioBtnPanel = new HorizontalPanel();
	
	private Label fileLabel = new Label("Upload Something");
	private Button submit = new Button("Submit");

    public Userform() {
        super();
        textBoxFnamn.setName("textBoxFnamn");
        textBoxLnamn.setName("textBoxLnamn");
        textBoxEmail.setName("textBoxEmail");
        textBoxCity.setName("textBoxCity");
        
        
        
        grid.setWidget(0, 0, textLabelFnamn);
        grid.setWidget(0, 1, textBoxFnamn);
        grid.setWidget(1, 0, textLabelLnamn);
        grid.setWidget(1, 1, textBoxLnamn);
        grid.setWidget(2, 0, textLabelEmail);
        grid.setWidget(2, 1, textBoxEmail);
        grid.setWidget(3, 0, textLabelCity);
        grid.setWidget(3, 1, textBoxCity);
        grid.setWidget(4, 0, textLabelGender);
        radioBtnPanel.add(btn1);
        radioBtnPanel.add(btn2);
        radioBtnPanel.add(btn3);
        grid.setWidget(4, 1, radioBtnPanel);
        
        upload.setName("upload");
        grid.setWidget(9, 0, fileLabel);
        grid.setWidget(9, 1, upload);
        
        submit.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					submit();	
				}
        });
        grid.setWidget(10, 0, submit);

        //setAction("/someAction");

        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);
        setWidget(grid);
        setStyleName("formPanel");

        addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				Window.alert("Submit!");
				
			}
                
        });
    }



}
