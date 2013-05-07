/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.media.Image;

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

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		
	}
	
	public void setAbstract(String s){
		pdfAbstract.setText(s);
	}
	
	public void setModifiedDate(String s){
		modifiedDate.setText(s);
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
