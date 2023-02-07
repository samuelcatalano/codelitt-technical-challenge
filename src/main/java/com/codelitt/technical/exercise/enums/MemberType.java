package com.codelitt.technical.exercise.enums;

public enum MemberType {

  EMPLOYEE,
  CONTRACTOR;

  public static MemberType fromValue(String value) {
    return valueOf(value.toUpperCase());
  }
}
