package org.iplantc.de.commons.client.views.dialogs;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.commons.share.CommonsModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

/**
 * @author aramsey
 */
public class AgaveAuthPrompt extends ConfirmMessageBox {

    private static AgaveAuthPrompt instance;

    public static AgaveAuthPrompt getInstance() {
        if (instance == null) {
            instance = new AgaveAuthPrompt();
        }
        return instance;
    }

    public interface AgaveAuthAppearance {

        String agaveRedirectTitle();

        String agaveRedirectMessage();

        String authenticateBtnText();

        String declineAuthBtnText();
    }

    static AgaveAuthAppearance appearance = GWT.create(AgaveAuthAppearance.class);
    UserInfo userInfo;

    private AgaveAuthPrompt(String title, String message, final String uri) {
        super(title, message);

        this.userInfo = UserInfo.getInstance();
        addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    Window.Location.replace(uri == null ? userInfo.getAgaveRedirect() : uri);
                }
            }
        });
        
        TextButton authButton = getButton(PredefinedButton.YES);
        authButton.setText(appearance.authenticateBtnText());
        
        TextButton noBtn = getButton(PredefinedButton.NO);
        noBtn.setText(appearance.declineAuthBtnText());

        setWidth(500);
    }


    @Override
    public void show() {
        super.show();

        ensureDebugId(CommonsModule.IDs.AGAVE_AUTH_DLG);
    }

    private AgaveAuthPrompt() {
        this(appearance.agaveRedirectTitle(), appearance.agaveRedirectMessage(), null);
    }

    public AgaveAuthPrompt(String uri) {
        this(appearance.agaveRedirectTitle(), appearance.agaveRedirectMessage(), uri);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.YES).asWidget().ensureDebugId(baseID + ".authBtn");
        getButton(PredefinedButton.NO).asWidget().ensureDebugId(baseID + ".noAuthBtn");
    }
}
