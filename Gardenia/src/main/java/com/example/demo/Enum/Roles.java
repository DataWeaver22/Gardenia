package com.example.demo.Enum;

public enum Roles {
	
	TERRITORY_SALES_OFFICER("Territory Sales Officer"),
	AREA_SALES_EXECUTIVES("Area Sales Executives");

    private String name;

    Roles(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }    
}
