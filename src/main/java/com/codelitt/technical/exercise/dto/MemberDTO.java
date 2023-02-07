package com.codelitt.technical.exercise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.codelitt.technical.exercise.enums.MemberType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO implements Serializable {

  @NotBlank(message = "First name must not be blank")
  private String firstName;

  @NotBlank(message = "Last name must not be blank")
  private String lastName;

  @Min(value = 0, message = "Salary must not be negative")
  private BigDecimal salary;

  @NotNull(message  = "Member type is required: EMPLOYEE or CONTRACTOR")
  private MemberType type;

  @Min(value = 0, message = "Contract duration must not be negative")
  private Integer contractDuration;

  private String role;

  @Size(min = 1, message = "At least one tag must be specified")
  private List<String> tags;

  @NotBlank(message = "Country must not be blank")
  private String country;

}
