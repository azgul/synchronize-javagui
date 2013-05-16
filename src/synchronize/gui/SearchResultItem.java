/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize.gui;

import java.awt.Desktop;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.PushButton;

import synchronize.core.Database;

/**
 *
 * @author Lars
 */
public class SearchResultItem extends Border implements Bindable {
	public enum LANG {
		DK, EN, DE, ES, IT, SE, RU, PL, JP, CN;
		
		@Override
		public String toString(){
			String orig = super.name().toLowerCase();
			return "file:./res/flags/" + orig + ".png";
		}
		
		public URL getUrl() throws MalformedURLException{
			return new URL(toString());
		}
		
		public static LANG getLanguage(String lang){
			if (lang == null)
				return LANG.EN;
			
			switch(lang.toLowerCase()){
				case "dk":
					return LANG.DK;
				case "en":
					return LANG.EN;
				case "de":
					return LANG.DE;
				case "es":
					return LANG.ES;
				case "it":
					return LANG.IT;
				case "se":
					return LANG.SE;
				case "ru":
					return LANG.RU;
				case "pl":
					return LANG.PL;
				case "jp":
					return LANG.JP;
				case "cn":
					return LANG.CN;
				default:
					return LANG.EN;
			}
		}
	}
	
	@BXML private Label pdfAbstract = null;
	@BXML private PushButton viewPdf = null;
	@BXML private Label modifiedDate = null;
	@BXML private ImageView languageImage = null;
	@BXML private Label breadcrumbs = null;
	
	private String pdfPath = "";

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		
	}
	
	public void setAbstract(String s){
		pdfAbstract.setText(s);
	}
	
	public void setModifiedDate(String s){
		modifiedDate.setText(s);
	}
	
	public void setBreadcrumbs(String s){
		breadcrumbs.setText("Path: " + s);
	}
	
	public void setPdfPath(String s){
		pdfPath = s;
		viewPdf.getButtonPressListeners().add(new ButtonPressListener() {

			@Override
			public void buttonPressed(Button button) {
				openPdf();
			}
		});
	}
	
	protected void openPdf(){
		try {
			File pdfFile = new File(pdfPath);
			System.out.println(pdfPath);
			if (pdfFile.exists() && Desktop.isDesktopSupported()) { 
				Desktop.getDesktop().browse(new URI(pdfFile.toURI().toString().replace("file:/","")));
				Database.getInstance().triggerFileRead(pdfPath);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setLanguage(LANG lang){
		try{
			languageImage.setImage(lang.getUrl());
		}catch(Exception e){
			// TRollfacecee
			e.printStackTrace();
		}
	}
}
