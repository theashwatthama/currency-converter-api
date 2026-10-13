package com.preet.currencyconverter.controller;

import com.preet.currencyconverter.dto.CurrencyConversionResponse;
import com.preet.currencyconverter.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrennncyController {

    private final CurrencyService currencyService;

    public CurrennncyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/convertCurrency")
    public CurrencyConversionResponse convertCurrency(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam double units){

        double convertedAmount= currencyService.convertCurrency(fromCurrency,toCurrency,units);
        return new CurrencyConversionResponse(
                fromCurrency,
                toCurrency,
                units,
                convertedAmount);
    }
}
