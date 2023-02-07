package com.codelitt.technical.exercise.service;

import jakarta.persistence.EntityNotFoundException;

import com.codelitt.technical.exercise.dto.MemberDTO;
import com.codelitt.technical.exercise.exception.ServiceException;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface MemberService {

  MemberDTO create(MemberDTO member) throws ServiceException;

  MemberDTO update(Long id, MemberDTO member) throws ServiceException;

  MemberDTO findById(Long id) throws EntityNotFoundException;

  List<MemberDTO> findAllMembers() throws ServiceException;

  void deleteById(Long id) throws EntityNotFoundException, DataIntegrityViolationException;

}