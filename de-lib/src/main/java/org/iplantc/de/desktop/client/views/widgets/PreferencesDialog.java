package org.iplantc.de.desktop.client.views.widgets;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.shared.Preferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author sriram, jstroot
 */

public class PreferencesDialog extends IPlantDialog implements DialogHideEvent.DialogHideHandler {

    public interface HtmlLayoutContainerTemplate extends XTemplates {
        @XTemplate(source = "PreferencesHelpTemplate.html")
        SafeHtml getTemplate();
    }

    private final PreferencesView.PreferencesViewAppearance appearance;
    private PreferencesView.Presenter presenter;
    private TextButton defaultsBtn;

    @Inject
    PreferencesDialog(final PreferencesView.PreferencesViewAppearance appearance,
                      final PreferencesView.Presenter presenter) {
        super(true);
        this.presenter = presenter;
        this.appearance = appearance;
        setHeading(appearance.preferences());
        getButton(PredefinedButton.OK).setText(appearance.done());
        getButton(PredefinedButton.CANCEL);
        defaultsBtn = new TextButton(appearance.restoreDefaults());
        defaultsBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                presenter.setDefaultValues();
            }
        });
        addDialogHideHandler(this);
        getButtonBar().insert(defaultsBtn, 0);
        addHelp(constructHelpView());
        add(presenter.getView());
    }

    public void show(DesktopView.Presenter desktopPresenter, UserSettings settings) {
        presenter.go(desktopPresenter, settings);
        super.show();

        ensureDebugId(Preferences.Ids.PREFERENCES_DLG);
    }

    @Override
    protected void onButtonPressed(TextButton button) {
        if (button == getButton(PredefinedButton.OK)) {
            presenter.getView().flush();
            if (presenter.getView().isValid()) {
                super.onButtonPressed(button);
                hide();
            }
        } else if (button == defaultsBtn) {
            presenter.setDefaultValues();
            super.onButtonPressed(button);
        } else if (button == getButton(PredefinedButton.CANCEL)) {
            super.onButtonPressed(button);
        }
    }



    private Widget constructHelpView() {
        HtmlLayoutContainerTemplate
                templates = GWT.create(HtmlLayoutContainerTemplate.class);
        HtmlLayoutContainer c = new HtmlLayoutContainer(templates.getTemplate());
        c.add(new HTML(appearance.notifyEmail()), new HtmlData(".emailHeader"));
        c.add(new HTML(appearance.notifyEmailHelp()), new HtmlData(".emailHelp"));
        c.add(new HTML(appearance.rememberFileSectorPath()), new HtmlData(".filePathHeader"));
        c.add(new HTML(appearance.rememberFileSectorPathHelp()), new HtmlData(".filePathHelp"));
        c.add(new HTML(appearance.saveSession()), new HtmlData(".saveSessionHeader"));
        c.add(new HTML(appearance.saveSessionHelp()), new HtmlData(".saveSessionHelp"));
        c.add(new HTML(appearance.defaultOutputFolder()), new HtmlData(".defaultOp"));
        c.add(new HTML(appearance.defaultOutputFolderHelp()), new HtmlData(".defaultOpHelp"));
        return c.asWidget();
    }

    @Override
    public void onDialogHide(DialogHideEvent event) {
        if (Dialog.PredefinedButton.OK.equals(event.getHideButton())) {
            presenter.saveUserSettings();
            hide();
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        getButton(PredefinedButton.OK).ensureDebugId(baseID + Preferences.Ids.DONE);
        getButton(PredefinedButton.CANCEL).ensureDebugId(baseID + Preferences.Ids.CANCEL);
        defaultsBtn.ensureDebugId(baseID + Preferences.Ids.DEFAULTS_BTN);
    }
}
