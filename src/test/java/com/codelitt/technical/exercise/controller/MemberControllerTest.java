package com.codelitt.technical.exercise.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.enums.MemberType;
import com.codelitt.technical.exercise.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MemberService memberService;

  @Test
  void testCreateMember() throws Exception {
    final MemberDTO member = new MemberDTO();
    member.setFirstName("John");
    member.setLastName("Doe");
    member.setCountry("brasil");
    member.setTags(Arrays.asList("Backend", "Frontend", "Contractor"));
    member.setType(MemberType.CONTRACTOR);
    member.setSalary(BigDecimal.valueOf(1000));

    when(memberService.create(member)).thenReturn(member);

    mockMvc.perform(post("/api/members")
           .contentType(MediaType.APPLICATION_JSON)
           .content(new ObjectMapper().writeValueAsString(member)))
           .andExpect(status().isCreated());

  }

  @Test
  void testUpdateMember() throws Exception {
    Long id = 1L;
    final MemberDTO member = new MemberDTO();
    member.setFirstName("Sam");
    member.setLastName("Catalano");
    member.setCountry("france");
    member.setTags(Arrays.asList("DevOps", "Design", "Employee"));
    member.setType(MemberType.EMPLOYEE);
    member.setRole("Software Engineer");
    member.setSalary(BigDecimal.valueOf(1200));

    when(memberService.update(id, member)).thenReturn(member);

    mockMvc.perform(put("/api/members/" + id)
           .contentType(MediaType.APPLICATION_JSON)
           .content(new ObjectMapper().writeValueAsString(member)))
           .andExpect(status().isOk());
  }

  @Test
  void testFindMemberById() throws Exception {
    Long id = 1L;
    final MemberDTO member = new MemberDTO();

    when(memberService.findById(id)).thenReturn(member);

    mockMvc.perform(get("/api/members/" + id))
           .andExpect(status().isOk());
  }

  @Test
  void findAllMembers() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/members"))
           .andExpect(status().isOk());
  }

  @Test
  void testDeleteMemberById() throws Exception {
    Long id = 1L;
    doNothing().when(memberService).deleteById(id);

    mockMvc.perform(delete("/api/members/" + id))
           .andExpect(status().isNoContent());
  }
}
