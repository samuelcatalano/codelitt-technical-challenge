package com.codelitt.technical.exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.codelitt.technical.exercise.dto.country.CountryInfoDTO;
import com.codelitt.technical.exercise.exception.ServiceException;
import com.codelitt.technical.exercise.service.impl.CountryInfoServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CountryInfoServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private CountryInfoServiceImpl service;

  @Test
  void getCurrency_Success() throws ServiceException {
    var countryName = "Brazil";
    var countryInfo = new CountryInfoDTO();

    final LinkedHashMap<String, Object> currencies = new LinkedHashMap<>();
    // given
    currencies.put("BRL", "Brazilian Real");
    countryInfo.setCurrencies(currencies);

    // when
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)
    )).
    thenReturn(new ResponseEntity<>(List.of(countryInfo), HttpStatus.OK));

    // then verify
    var currency = service.getCurrency(countryName);
    assertEquals("BRL", currency);
  }

  @Test
  void getCurrency_CurrencyNotFound() {
    // given
    var countryName = "Unknown";
    var countryInfo = new CountryInfoDTO();

    final LinkedHashMap<String, Object> currencies = new LinkedHashMap<>();
    countryInfo.setCurrencies(currencies);

    // then
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)
    )).
    thenReturn(new ResponseEntity<>(List.of(countryInfo), HttpStatus.OK));

    // then verify
    var exception = assertThrows(ServiceException.class, () -> service.getCurrency(countryName));
    assertEquals("Error retrieving currency information! Message: Currency information not available for the country: " + countryName, exception.getMessage());
  }

  @Test
  void getCountryInfo_Success() throws ServiceException {
    // given
    var countryName = "United States";
    var countryInfo = new CountryInfoDTO();

    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)
    )).
    thenReturn(new ResponseEntity<>(List.of(countryInfo), HttpStatus.OK));

    // when
    var result = service.getCountryInfo(countryName);

    // then verify
    assertSame(countryInfo, result);
  }

  @Test
  void getCountryInfo_NotFound() {
    // given
    var countryName = "Vermont";

    // when
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)
    )).
    thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

    // then verify
    var exception = assertThrows(ServiceException.class, () -> service.getCountryInfo(countryName));
    assertEquals("Error retrieving information from 'restcountries.com'. Message: No information found for the country: " + countryName, exception.getMessage());
  }

  @Test
  void getCountryInfo_Error() {
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(),
        any(ParameterizedTypeReference.class)
    )).
    thenThrow(new RuntimeException("Error during the call to restcountries.com"));
    assertThrows(ServiceException.class, () -> service.getCountryInfo("Spain"));
  }

}
