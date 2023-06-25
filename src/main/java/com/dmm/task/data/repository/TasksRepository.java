package com.dmm.task.data.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmm.task.data.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
	@Query("select a from Tasks a where a.date between :from and :to and name = :name")
	
	List<Tasks> findByDateBetween(@Param("from") LocalDateTime start, @Param("to") LocalDateTime end, @Param("name") String name);
	List<Tasks> findAllByDateBetween(@Param("from") LocalDateTime start, @Param("to") LocalDateTime end);
}
