package com.codelitt.technical.exercise.repository;

import com.codelitt.technical.exercise.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
}