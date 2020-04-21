package com.example.supermakettool;

import com.google.gson.annotations.Expose;

public class TblListsResponse {

    public int getPkIndexListas() {
        return pkIndexListas;
    }

    public void setPkIndexListas(int pkIndexListas) {
        this.pkIndexListas = pkIndexListas;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    @Expose
    private int pkIndexListas;
    @Expose
    private int fkUser;
    @Expose
    private String listName;


}
