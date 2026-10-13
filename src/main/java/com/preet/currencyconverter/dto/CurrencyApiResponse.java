package com.preet.currencyconverter.dto;

import java.util.Map;

public class CurrencyApiResponse {

    private Map<String,Double> data;

    public CurrencyApiResponse(){
    }



    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }
}
