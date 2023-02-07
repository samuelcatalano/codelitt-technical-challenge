package com.codelitt.technical.exercise.service;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;

import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.enums.MemberType;
import com.codelitt.technical.exercise.exception.ServiceException;
import com.codelitt.technical.exercise.model.Member;
import com.codelitt.technical.exercise.repository.MemberRepository;
import com.codelitt.technical.exercise.service.impl.MemberServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock
  private CountryInfoService countryInfoService;

  @Mock
  private MemberRepository repository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private MemberServiceImpl memberService;

  @Test
  void create_whenInputIsValid_thenReturnNewMemberDTO() throws ServiceException {
    // given
    final MemberDTO member = new MemberDTO();
    member.setFirstName("John");
    member.setLastName("Doe");
    member.setType(MemberType.CONTRACTOR);
    member.setCountry("US");
    member.setRole("");

    final Member entity = new Member();
    entity.setId(1L);
    entity.setFirstName("John");
    entity.setLastName("Doe");
    entity.setType(MemberType.CONTRACTOR);
    entity.setCountry("US");
    entity.setRole("");
    entity.setCurrency("USD");

    // when
    when(modelMapper.map(member, Member.class)).thenReturn(entity);
    when(countryInfoService.getCurrency("US")).thenReturn("USD");
    when(repository.save(entity)).thenReturn(entity);
    when(modelMapper.map(entity, MemberDTO.class)).thenReturn(member);

    // then verify
    final MemberDTO result = memberService.create(member);
    assertEquals(member, result);
  }

  @Test
  void create_WithValidInput_ShouldReturnCreatedMember() throws Exception {
    // given
    final MemberDTO member = new MemberDTO();
    member.setCountry("country");

    final Member entity = new Member();
    when(modelMapper.map(member, Member.class)).thenReturn(entity);
    when(countryInfoService.getCurrency("country")).thenReturn("currency");
    when(repository.save(entity)).thenReturn(entity);
    when(modelMapper.map(entity, MemberDTO.class)).thenReturn(member);

    // when
    final MemberDTO result = memberService.create(member);

    // then verify
    verify(modelMapper, times(1)).map(member, Member.class);
    verify(countryInfoService, times(1)).getCurrency("country");
    verify(repository, times(1)).save(entity);
    verify(modelMapper, times(1)).map(entity, MemberDTO.class);
    assertSame(member, result);
  }

  @Test
  void create_whenCurrencyNotFound_thenThrowServiceException() {
    // given
    final MemberDTO member = new MemberDTO();
    member.setType(MemberType.EMPLOYEE);
    member.setCountry("UKE");
    member.setRole("Developer");

    // when
    try {
      when(countryInfoService.getCurrency(member.getCountry())).thenReturn(null);
    } catch (ServiceException e) {
      throw new RuntimeException(e);
    }

    var exception = assertThrows(ServiceException.class, () -> {
      memberService.create(member);
    });

    // then verify
    assertEquals("Currency not found for country: UKE", exception.getMessage());
  }

  @Test
  void testUpdate_whenIdIsInvalid_thenEntityNotFoundExceptionIsThrown() {
    // given
    final MemberDTO member = new MemberDTO();

    // when
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    // then verify
    assertThrows(EntityNotFoundException.class, () -> memberService.update(1L, member));
  }

  @Test
  void testCreate_WhenMemberHasTypeEmployeeButNoRole_ShouldThrowServiceException() throws ServiceException {
    // given
    final MemberDTO member = MemberDTO.builder()
        .firstName("John")
        .lastName("Doe")
        .type(MemberType.EMPLOYEE)
        .country("US")
        .build();

    // when
    when(countryInfoService.getCurrency("US")).thenReturn("USD");

    // then verify
    try {
      memberService.create(member);
      fail("Expected a ServiceException to be thrown");
    } catch (ServiceException e) {
      assertEquals("If the member type is Employee, then we need to set their role before persist!", e.getMessage());
    }
  }

  @Test
  void testUpdate_WhenMemberNotFound_ShouldThrowEntityNotFoundException() throws ServiceException {
    // given
    final MemberDTO member = MemberDTO.builder()
        .firstName("John")
        .lastName("Doe")
        .type(MemberType.EMPLOYEE)
        .role("developer")
        .country("US")
        .build();

    // when
    lenient().when(countryInfoService.getCurrency("US")).thenReturn("USD");
    lenient().when(repository.findById(1L)).thenReturn(Optional.empty());

    // then verify
    try {
      memberService.update(1L, member);
      fail("Expected an EntityNotFoundException to be thrown");
    } catch (EntityNotFoundException e) {
      assertEquals("Member not found with id: 1", e.getMessage());
    }
  }

  @Test
  void testUpdate_WhenIdIsInvalid_ThenThrowEntityNotFoundException() {
    // given
    final MemberDTO memberDTO = MemberDTO.builder().firstName("member").country("country").build();
    Long id = 1L;

    // when
    when(repository.findById(id)).thenReturn(Optional.empty());

    // then verify
    var exception = assertThrows(EntityNotFoundException.class, () -> memberService.update(id, memberDTO));
    assertEquals("Member not found with id: " + id, exception.getMessage());
  }

  @Test
  void testFindById_ShouldReturnFoundMember() {
    // given
    Long id = 1L;
    final Member member = new Member();
    final MemberDTO memberDTO = new MemberDTO();

    // when
    when(repository.findById(eq(id))).thenReturn(java.util.Optional.of(member));
    when(modelMapper.map(eq(member), eq(MemberDTO.class))).thenReturn(memberDTO);

    // then verify
    final MemberDTO result = memberService.findById(id);
    assertEquals(result, memberDTO);
  }

  @Test
  void testFindById_EntityNotFoundException() {
    // given
    Long id = 1L;
    // when
    when(repository.findById(eq(id))).thenReturn(Optional.empty());

    // then verify
    assertThrows(EntityNotFoundException.class, () -> memberService.findById(id));
  }

  @Test
  void findAllMembers_ShouldReturnListOfMembersDTO_WhenMembersExist() throws ServiceException {
    // given
    final Member member1 = new Member();
    final Member member2 = new Member();
    final List<Member> members = Arrays.asList(member1, member2);

    // when
    when(repository.findAll()).thenReturn(members);

    // then verify
    final List<MemberDTO> result = memberService.findAllMembers();
    assertEquals(2, result.size());
  }

  @Test
  void findAllMembers_ShouldThrowServiceException_WhenErrorRetrievingMembers() throws ServiceException {
    when(repository.findAll()).thenThrow(new RuntimeException("Error retrieving members"));
    assertThrows(ServiceException.class, () -> memberService.findAllMembers());
  }

  @Test
  void deleteById_whenIdExists_shouldDeleteMember() throws EntityNotFoundException, DataIntegrityViolationException {
    // given
    Long id = 1L;
    final Member member = new Member();
    member.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(member));

    // when
    memberService.deleteById(id);

    // then verify
    verify(repository, times(1)).deleteById(id);
  }

  @Test
  void deleteById_whenIdDoesNotExist_shouldThrowEntityNotFoundException() throws EntityNotFoundException, DataIntegrityViolationException {
    // given
    Long id = 1L;

    // when
    when(repository.findById(id)).thenReturn(Optional.empty());

    // then verify
    assertThrows(EntityNotFoundException.class, () -> memberService.deleteById(id));
  }

  @Test
  void deleteById_whenDataIntegrityException_shouldThrowDataIntegrityViolationException() throws EntityNotFoundException, DataIntegrityViolationException {
    // given
    Long id = 1L;
    final Member member = new Member();
    member.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(member));
    doThrow(new DataIntegrityViolationException("error deleting member")).when(repository).deleteById(id);

    // when & then
    assertThrows(DataIntegrityViolationException.class, () -> memberService.deleteById(id));
  }
}
