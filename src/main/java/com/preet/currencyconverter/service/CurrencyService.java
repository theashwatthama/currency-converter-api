package com.preet.currencyconverter.service;

import com.preet.currencyconverter.Repo.ConversionHistoryRepo;
import com.preet.currencyconverter.dto.CurrencyApiResponse;
import com.preet.currencyconverter.entity.ConversionHistory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.springframework.web.client.RestClientException;


@Service
public class CurrencyService {

    private final RestClient restClient;
    private final ConversionHistoryRepo conversionHistoryRepo;

    public CurrencyService(RestClient restClient, ConversionHistoryRepo conversionHistoryRepo) {
        this.restClient = restClient;
        this.conversionHistoryRepo = conversionHistoryRepo;
    }

    public static final Logger log= LoggerFactory.getLogger(CurrencyService.class);

    @Value("${freecurrency.api.key}")
    private String apiKey;

    public Double convertCurrency(String fromCurrency, String toCurrency, double units){

        log.info( "Currency conversion started: {} to {}, units: {}",fromCurrency,toCurrency,units);

        if(units<0){
            log.warn("Invalid Conversion Amount: {}",units);
            throw new IllegalArgumentException(" Units must be greater than zero");
        }

        CurrencyApiResponse response;

           try {
               response=restClient.get()
                       .uri(uriBuilder -> uriBuilder
                               .scheme("https")
                               .host("api.freecurrencyapi.com")
                               .path("/v1/latest")
                               .queryParam("base_currency",fromCurrency)
                               .queryParam("currencies",toCurrency)
                               .build())
                       .header("apikey",apiKey)
                       .retrieve()
                       .body(CurrencyApiResponse.class);
           } catch (RestClientException e){
               log.error(
               "Failed to fetch exchange rate for {} to {}",
                       fromCurrency,
                       toCurrency,
                       e);

               throw  new IllegalStateException("Unable to fetch exchange rate. Please try again later.", e);
           }

        if(response==null || response.getData()==null || response.getData().get(toCurrency)==null){

            log.error("Exchange rate unavailable for {} to {}",fromCurrency,toCurrency);
            throw new IllegalStateException("Exchange rate not available");
        }

        double rate=response.getData().get(toCurrency);
        double convertedAmount=rate*units;

        ConversionHistory history = new ConversionHistory();
        history.setFromCurrency(fromCurrency);
        history.setToCurrency(toCurrency);
        history.setUnits(units);
        history.setConvertedAmount(convertedAmount);

        conversionHistoryRepo.save(history);

        log.info("Currency Conversion Successful: {} {} = {} {}",units,fromCurrency,convertedAmount,toCurrency);
        return convertedAmount;
    }


}
