package com.codelitt.technical.exercise.dto.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryInfoDTO implements Serializable {

  private CountryName name;
  private transient LinkedHashMap<String, Object> currencies;
}
