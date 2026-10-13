package com.preet.currencyconverter.dto;

public class CurrencyConversionResponse {

    private String fromCurrency;
    private String toCurrency;
    private double units;
    private double convertedAmount;

    public CurrencyConversionResponse() {
    }

    public CurrencyConversionResponse(String fromCurrency, String toCurrency, double units, double convertedAmount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.units = units;
        this.convertedAmount = convertedAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getUnits() {
        return units;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }
}
