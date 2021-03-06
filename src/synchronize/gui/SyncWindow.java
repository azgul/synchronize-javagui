package synchronize.gui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;
import org.apache.pivot.wtk.TextInputSelectionListener;
import org.apache.pivot.wtk.TreeView;
import synchronize.core.UIObservers;
import synchronize.listeners.CategoryListener;
import synchronize.model.Category;

public class SyncWindow extends Frame implements Bindable {	
    @BXML private FileBrowserSheet fileBrowserSheet;
    @BXML private TabPane tabPane;
    @BXML private TreeView categories;
    @BXML public Border progress;
	@BXML private ImageView categoryImage;

    private MenuHandler menuHandler = new MenuHandler.Adapter() {
        TextInputContentListener textInputTextListener = new TextInputContentListener.Adapter() {
            @Override
            public void textChanged(TextInput textInput) {
                updateActionState(textInput);
            }
        };

        TextInputSelectionListener textInputSelectionListener = new TextInputSelectionListener() {
            @Override
            public void selectionChanged(TextInput textInput, int previousSelectionStart,
                int previousSelectionLength) {
                updateActionState(textInput);
            }
        };

        @Override
        public void configureMenuBar(Component component, MenuBar menuBar) {
            if (component instanceof TextInput) {
                TextInput textInput = (TextInput)component;

                updateActionState(textInput);
                Action.getNamedActions().get("paste").setEnabled(true);

                textInput.getTextInputContentListeners().add(textInputTextListener);
                textInput.getTextInputSelectionListeners().add(textInputSelectionListener);
            } else {
                Action.getNamedActions().get("cut").setEnabled(false);
                Action.getNamedActions().get("copy").setEnabled(false);
                Action.getNamedActions().get("paste").setEnabled(false);
            }
        }

        @Override
        public void cleanupMenuBar(Component component, MenuBar menuBar) {
            if (component instanceof TextInput) {
                TextInput textInput = (TextInput)component;
                textInput.getTextInputContentListeners().remove(textInputTextListener);
                textInput.getTextInputSelectionListeners().remove(textInputSelectionListener);
            }
        }

        private void updateActionState(TextInput textInput) {
            Action.getNamedActions().get("cut").setEnabled(textInput.getSelectionLength() > 0);
            Action.getNamedActions().get("copy").setEnabled(textInput.getSelectionLength() > 0);
        }
    };

    public SyncWindow() {
        Action.getNamedActions().put("file", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });

        Action.getNamedActions().put("synchronize", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });

        Action.getNamedActions().put("help", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });

        Action.getNamedActions().put("about", new Action(false) {
            @Override
            public void perform(Component source) {
            }
        });
    }
	
	private void setCategoryImage(String image){
		try{
			System.out.println("Setting image to: " + image);
			URL path = new URL(image);
			categoryImage.setImage(path);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
    	/*DropShadowDecorator backDecorator = new DropShadowDecorator(1,3,3);
		backDecorator.setShadowColor("#000000");
		backDecorator.setShadowOpacity(0.8f);
		backBorder.getDecorators().add(backDecorator);*/
		
		UIObservers.addCategoryListener(new CategoryListener() {

			@Override
			public void onSelect(Category category) {
				System.out.println("Selected category: ");
				System.out.println(category);
				setCategoryImage(category.getImage());
			}

			@Override
			public void onNoneSelected() {
				// Set default image
			}
		});
    }
}