package com.example.supermakettool;

import com.google.gson.annotations.Expose;

public class TblCategoryResponse {

    @Expose
    int pkCategory;
    @Expose
    String categoryName;


    public int getPkCategory() {
        return pkCategory;
    }

    public void setPkCategory(int pkCategory) {
        this.pkCategory = pkCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
