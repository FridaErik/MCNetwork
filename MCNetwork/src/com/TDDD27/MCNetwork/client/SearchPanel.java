/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Frida
 *
 */
public class SearchPanel extends HorizontalPanel {

	/**
	 * 
	 */
	public SearchPanel() {
		this.setVerticalAlignment(ALIGN_MIDDLE);
		HTML searchHTML = new HTML("<searchp>S&ouml;k p&aring; anv&auml;ndarnamn:</searchp>", true);
		TextBox searchBox = new TextBox();
		Button searchBtn = new Button("Hitta >>");
		this.add(searchHTML);
		this.add(searchBox);
		this.add(searchBtn);
		
	}

}
