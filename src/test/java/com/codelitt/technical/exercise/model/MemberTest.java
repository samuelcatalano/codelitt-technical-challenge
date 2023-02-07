package com.codelitt.technical.exercise.model;

import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.enums.MemberType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
class MemberTest {

  private final ModelMapper mapper = new ModelMapper();
  private MemberDTO memberDTO;

  @BeforeEach
  void setup() {
    memberDTO = MemberDTO.builder()
               .firstName("John")
               .lastName("Doe")
               .tags(Arrays.asList("Backend", "Frontend", "DevOps"))
               .type(MemberType.CONTRACTOR)
               .salary(BigDecimal.valueOf(5000))
               .contractDuration(24)
               .country("Brazil")
               .build();
  }

  @Test
  void testMemberMapping() throws Exception {
    final Member member = this.mapper.map(memberDTO, Member.class);
    Assertions.assertEquals(member.getFirstName(), memberDTO.getFirstName());
    Assertions.assertEquals(member.getLastName(), memberDTO.getLastName());
    Assertions.assertEquals(member.getTags(), memberDTO.getTags());
    Assertions.assertEquals(member.getSalary(), memberDTO.getSalary());
    Assertions.assertEquals(member.getCountry(), memberDTO.getCountry());
  }
}
