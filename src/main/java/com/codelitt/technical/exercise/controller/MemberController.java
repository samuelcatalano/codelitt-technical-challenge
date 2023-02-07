package com.codelitt.technical.exercise.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.codelitt.technical.exercise.controller.base.BaseRESTController;
import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.exception.ApiException;
import com.codelitt.technical.exercise.exception.ServiceException;
import com.codelitt.technical.exercise.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/members")
public class MemberController extends BaseRESTController {

  private final MemberService memberService;

  @Autowired
  public MemberController(final MemberService memberService) {
    this.memberService = memberService;
  }

  /**
   * Creates a new Member.
   *
   * @param member the Member data to create.
   * @return a ResponseEntity with the created Member and HTTP status CREATED.
   *         In case of error, returns a ResponseEntity with HTTP status INTERNAL_SERVER_ERROR.
   */
  @PostMapping
  public ResponseEntity<MemberDTO> create(@Valid @RequestBody final MemberDTO member) throws ApiException {
    try {
      final MemberDTO createdMember = memberService.create(member);
      return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    } catch (final ServiceException e) {
      log.error("Error creating Member: {}", e.getMessage(), e);
      throw new ApiException("Error creating Member: {}", e);
    }
  }

  /**
   * Updates a Member by its id.
   *
   * @param id the id of the Member to update.
   * @param member the Member data to update.
   * @return a ResponseEntity with the updated Member and HTTP status OK.
   *         In case of error or if the Member with the given id was not found, returns a ResponseEntity with HTTP status INTERNAL_SERVER_ERROR.
   */
  @PutMapping("/{id}")
  public ResponseEntity<MemberDTO> update(@PathVariable final Long id, @Valid @RequestBody final MemberDTO member) {
    try {
      final MemberDTO updatedMember = memberService.update(id, member);
      return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    } catch (ServiceException | EntityNotFoundException e) {
      log.error("Error updating Member with id: {} - {}", id, e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieves a Member by its id.
   *
   * @param id the id of the Member to retrieve.
   * @return a ResponseEntity with the found Member and HTTP status OK.
   *         In case the Member with the given id was not found, returns a ResponseEntity with HTTP status NOT_FOUND.
   */
  @GetMapping("/{id}")
  public ResponseEntity<MemberDTO> findById(@PathVariable final Long id) {
      final MemberDTO foundMember = memberService.findById(id);
      return new ResponseEntity<>(foundMember, HttpStatus.OK);
  }

  /**
   * Retrieves a list of all members by calling the `findAllMembers` method from the `memberService` and returns it as a response with an HTTP status of OK.
   *
   * @return a response entity containing a list of MemberDTO objects representing all members and an HTTP status of OK
   * @throws ServiceException if there is an error retrieving the members from the memberService
   */
  @GetMapping
  public ResponseEntity<List<MemberDTO>> findAllMembers() throws ServiceException {
    final List<MemberDTO> foundMembers = memberService.findAllMembers();
    return new ResponseEntity<>(foundMembers, HttpStatus.OK);
  }

  /**
   * Deletes a Member by its id.
   *
   * @param id the id of the Member to delete.
   * @return a ResponseEntity with HTTP status NO_CONTENT.
   *         In case the Member with the given id was not found, throws an EntityNotFoundException.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) throws EntityNotFoundException {
    memberService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
