package com.codelitt.technical.exercise.service.impl;

import lombok.extern.slf4j.Slf4j;

import com.codelitt.technical.exercise.dto.country.CountryInfoDTO;
import com.codelitt.technical.exercise.exception.ServiceException;
import com.codelitt.technical.exercise.service.CountryInfoService;
import com.codelitt.technical.exercise.service.base.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CountryInfoServiceImpl extends BaseService implements CountryInfoService {

  private final RestTemplate restTemplate;

  @Autowired
  public CountryInfoServiceImpl(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Retrieves the currency of a country using the getCountryInfo method.
   *
   * @param countryName the name of the country to retrieve the currency for.
   * @return a String representing the currency code for the country.
   * @throws ServiceException in case of error or if currency information was not available for the country.
   */
  @Override
  public String getCurrency(final String countryName) throws ServiceException {
    try {
      var countryInfo = getCountryInfo(countryName);
      var currencies = countryInfo.getCurrencies();
      if (currencies.isEmpty()) {
        throw new ServiceException("Currency information not available for the country: " + countryName);
      }
      return currencies.keySet().iterator().next();
    } catch (final Exception e) {
      log.error("Error retrieving currency information! Message: {}", e.getMessage(), e);
      throw new ServiceException("Error retrieving currency information! Message: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves information about a country using the "restcountries.com" API.
   *
   * @param countryName the name of the country to retrieve information for.
   * @return a CountryInfoDTO object with information about the country.
   * @throws ServiceException in case of error or if no information was found for the country.
   */
  @Override
  public CountryInfoDTO getCountryInfo(final String countryName) throws ServiceException {
    try {
      var result = restTemplate.exchange(baseUrl + countryName, HttpMethod.GET, getCustomHeaders(), new ParameterizedTypeReference<List<CountryInfoDTO>>(){}).getBody();
      if (result == null || result.isEmpty()) {
        throw new ServiceException("No information found for the country: " + countryName);
      }

      return result.get(0);
    } catch (final Exception e) {
      log.error("Error retrieving information from 'restcountries.com'. Message: {}", e.getMessage(), e);
      throw new ServiceException("Error retrieving information from 'restcountries.com'. Message: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a custom HTTP entity with headers for making requests to the "restcountries.com" API.
   * @return a HttpEntity<List<CountryInfoDTO>> with custom headers.
   */
  private HttpEntity<List<CountryInfoDTO>> getCustomHeaders() {
    return new HttpEntity<>(getHttpHeaders());
  }
}
