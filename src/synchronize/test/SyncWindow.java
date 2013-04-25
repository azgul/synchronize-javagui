package synchronize.test;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.effects.DropShadowDecorator;

public class SyncWindow extends Window implements Bindable {
	
	@BXML PushButton back;
	@BXML Border backBorder;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		DropShadowDecorator backDecorator = new DropShadowDecorator(1,3,3);
		backDecorator.setShadowColor("#000000");
		backDecorator.setShadowOpacity(0.8f);
		backBorder.getDecorators().add(backDecorator);
	}

}
