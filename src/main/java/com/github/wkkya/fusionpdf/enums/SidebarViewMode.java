package com.github.wkkya.fusionpdf.enums;

public enum SidebarViewMode {

    NONE("无"),
    THUMBNAILS("缩略图"),
    OUTLINE("大纲"),
    ATTACHMENTS("附件")

    ;

    private final String name;

    SidebarViewMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
