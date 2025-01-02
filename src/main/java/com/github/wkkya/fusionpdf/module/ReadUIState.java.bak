package com.github.wkkya.fusionpdf.module;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(
        name = "ReadUIState",
        storages = {
                @Storage(StoragePathMacros.WORKSPACE_FILE)
        }
)
public class ReadUIState implements PersistentStateComponent<ReadUIState> {

    private int lastPage = 0;

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public ReadUIState getState() {
        return this;
    }

    @Override
    public void loadState(ReadUIState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
