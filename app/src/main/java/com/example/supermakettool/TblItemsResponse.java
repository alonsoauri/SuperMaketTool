package com.example.supermakettool;

import com.google.gson.annotations.Expose;

public class TblItemsResponse {

    @Expose
    public int fkUser;
    @Expose
    public int fkIndexListas;
    @Expose
    public int pkId;
    @Expose
    public int fkCb;
    @Expose
    public int fkItem;
    @Expose
    public String itemName;
    @Expose
    public Double cantidad;
    @Expose
    public int fkCategory;
    @Expose
    public Double price;
    @Expose
    public String um;


    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public int getFkIndexListas() {
        return fkIndexListas;
    }

    public void setFkIndexListas(int fkIndexListas) {
        this.fkIndexListas = fkIndexListas;
    }

    public int getPkId() {
        return pkId;
    }

    public void setPkId(int pkId) {
        this.pkId = pkId;
    }

    public int getFkCb() {
        return fkCb;
    }

    public void setFkCb(int fkCb) {
        this.fkCb = fkCb;
    }

    public int getFkItem() {
        return fkItem;
    }

    public void setFkItem(int fkItem) {
        this.fkItem = fkItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public int getFkCategory() {
        return fkCategory;
    }

    public void setFkCategory(int fkCategory) {
        this.fkCategory = fkCategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }
}
