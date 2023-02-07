package com.codelitt.technical.exercise.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.enums.MemberType;
import com.codelitt.technical.exercise.exception.ServiceException;
import com.codelitt.technical.exercise.model.Member;
import com.codelitt.technical.exercise.repository.MemberRepository;
import com.codelitt.technical.exercise.service.CountryInfoService;
import com.codelitt.technical.exercise.service.MemberService;
import com.codelitt.technical.exercise.service.base.BaseService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl extends BaseService implements MemberService {

  private static final String NO_MEMBER_FOUND_WITH_ID = "There is no member found with id: ";

  private final CountryInfoService countryInfoService;

  private final MemberRepository repository;
  private final ModelMapper modelMapper;

  @Autowired
  public MemberServiceImpl(final CountryInfoService countryInfoService, final MemberRepository repository, final ModelMapper modelMapper) {
    this.countryInfoService = countryInfoService;
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  /**
   * Create a new member using a MemberDTO object.
   *
   * @param member The MemberDTO object containing the information for the new member.
   * @return A MemberDTO object representing the created member.
   * @throws ServiceException If there is an error while creating the member, or if the currency information is not found.
   */
  @Override
  public MemberDTO create(final MemberDTO member) throws ServiceException {
    var currency = countryInfoService.getCurrency(member.getCountry());
    if (currency == null) {
      throw new ServiceException("Currency not found for country: " + member.getCountry());
    }

    var type = member.getType();
    if ((type == MemberType.EMPLOYEE && (member.getRole() == null || member.getRole().equals("")))) {
      throw new ServiceException("If the member type is Employee, then we need to set their role before persist!");
    }

    var entity = this.modelMapper.map(member, Member.class);
    entity.setCurrency(currency);

    try {
      var result = repository.save(entity);
      return this.modelMapper.map(result, MemberDTO.class);
    } catch (final Exception e) {
      log.error("Error persisting a new Member: {}", e.getMessage(), e);
      throw new ServiceException("Error persisting a new Member", e);
    }
  }

  /**
   * Update an existing member with a given id using a MemberDTO object.
   *
   * @param id The id of the member to be updated.
   * @param member The MemberDTO object containing the updated information.
   * @return A MemberDTO object representing the updated member.
   * @throws EntityNotFoundException If a member with the given id is not found.
   * @throws ServiceException If there is an error while updating the member, or if the currency information is not found.
   */
  @Override
  public MemberDTO update(final Long id, final MemberDTO member) throws ServiceException {
    var entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
    var currency = countryInfoService.getCurrency(member.getCountry());
    if (currency == null) {
      throw new ServiceException("Currency not found for country: " + member.getCountry());
    }

    var type = member.getType();
    if ((type == MemberType.EMPLOYEE && (member.getRole() == null || member.getRole().equals("")))) {
      throw new ServiceException("If the member type is Employee, then we need to set their role before updating");
    }

    modelMapper.map(member, entity);
    entity.setCurrency(currency);

    try {
      entity = this.repository.save(entity);
      return this.modelMapper.map(entity, MemberDTO.class);
    } catch (final Exception e) {
      log.error("Error updating an existed Member: {}", e.getMessage(), e);
      throw new ServiceException("Error updating an existed Member", e);
    }
  }

  /**
   * Retrieve a {@link MemberDTO} object with the given id.
   *
   * @param id The id of the member to be retrieved.
   * @return The {@link MemberDTO} object corresponding to the id.
   * @throws EntityNotFoundException if no member is found with the given id.
   */
  @Override
  public MemberDTO findById(final Long id) throws EntityNotFoundException {
    return repository.findById(id)
          .map(member -> this.modelMapper.map(member, MemberDTO.class))
          .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_FOUND_WITH_ID + id));
  }

  /**
   * Retrieves a list of all members and maps them to MemberDTO objects.
   *
   * @return a list of MemberDTO objects representing all members
   * @throws ServiceException if there is an error retrieving the members from the repository
   */
  @Override
  public List<MemberDTO> findAllMembers() throws ServiceException {
    try {
      final List<MemberDTO> membersDTO = new ArrayList<>();
      var members = repository.findAll();
      var memberList = Streamable.of(members).toList();

      memberList.forEach(member -> membersDTO.add(this.modelMapper.map(member, MemberDTO.class)));

      return membersDTO;
    } catch (final Exception e) {
      log.error("Error retrieving all existing members: {}", e.getMessage(), e);
      throw new ServiceException("Error retrieving all existing members", e);
    }
  }

  /**
   * Deletes a Member by its id.
   *
   * @param id the id of the Member to be deleted.
   * @throws EntityNotFoundException if no Member is found with the given id.
   * @throws DataIntegrityViolationException if there's a constraint violation while deleting the Member.
   */
  @Override
  public void deleteById(final Long id) throws EntityNotFoundException, DataIntegrityViolationException {
    repository.findById(id).orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_FOUND_WITH_ID + id));
    try {
      repository.deleteById(id);
    } catch (final DataIntegrityViolationException e) {
      log.error("Error deleting Member with id: " + id + " - " + e.getMessage(), e);
      throw new DataIntegrityViolationException("Error deleting Member with id: " + id + " - " + e.getMessage(), e);
    }
  }
}
