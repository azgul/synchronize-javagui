/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronize;

import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.PushButton;

/**
 *
 * @author Lars
 */
public class SearchResultItem extends Border implements Bindable {
	@BXML private Label pdfAbstract = null;
	@BXML private PushButton viewPdf = null;
	@BXML private Label modifiedDate = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources rsrcs) {
		
	}
	
	public void setAbstract(String s){
		pdfAbstract.setText(s);
	}
	
	public void setModifiedDate(String s){
		modifiedDate.setText(s);
	}
}
