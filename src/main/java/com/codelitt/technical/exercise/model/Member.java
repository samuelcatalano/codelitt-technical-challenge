package com.codelitt.technical.exercise.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.codelitt.technical.exercise.enums.MemberType;
import com.codelitt.technical.exercise.model.base.BaseModel;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "member")
public class Member extends BaseModel {

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "salary")
  private BigDecimal salary;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private MemberType type;

  @Column(name = "contract_duration")
  private Integer contractDuration;

  @Column(name = "role")
  private String role;

  @ElementCollection
  @CollectionTable(name = "member_tags", joinColumns = @JoinColumn(name = "member_id"))
  @Column(name = "tags")
  private List<String> tags;

  @Column(name = "country")
  private String country;

  @Column(name = "currency")
  private String currency;

  public void setCountry(String country) {
    this.country = StringUtils.capitalize(country);
  }
}
