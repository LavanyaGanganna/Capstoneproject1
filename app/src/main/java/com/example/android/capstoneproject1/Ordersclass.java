package com.example.android.capstoneproject1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavanya on 11/30/16.
 */

public class Ordersclass {
    String emailaddr;
    List<String> titles;
    String total;

    public Ordersclass(String emailaddr, List<String> titles, String total) {
        this.emailaddr = emailaddr;
        this.titles = titles;
        this.total = total;
    }

    public String getEmailaddr() {
        return emailaddr;
    }

    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
