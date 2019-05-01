package com.sfxcode.sapphire.extension.control;

import com.sfxcode.sapphire.extension.skin.DataListViewSkin;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;


public class DataListView<S> extends Control {


    public DataListView() {
        getStyleClass().add("data-list-view");
    }

    protected Skin<DataListView<S>> createDefaultSkin() {
        return new DataListViewSkin<S>(this);
    }

    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("datalistview.css").toExternalForm();
    }
}
