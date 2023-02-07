package com.codelitt.technical.exercise.service;

import com.codelitt.technical.exercise.dto.country.CountryInfoDTO;
import com.codelitt.technical.exercise.exception.ServiceException;

public interface CountryInfoService {

  CountryInfoDTO getCountryInfo(String countryName) throws ServiceException;

  String getCurrency(String countryName) throws ServiceException;
}
