package com.example.android.capstoneproject1;

/**
 * Created by lavanya on 11/11/16.
 */

public class MenuList {
    int icons;
    String item;

    public MenuList(int icons, String items) {
        this.icons = icons;
        this.item = items;
    }

    public int getIcons() {
        return icons;
    }

    public void setIcons(int icons) {
        this.icons = icons;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
