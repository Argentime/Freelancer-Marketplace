package com.example.javalabs.repositories;

import com.example.javalabs.models.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    List<Freelancer> findByCategory(String category);

    // JPQL-запрос
    @Query("SELECT f FROM Freelancer f JOIN f.skills s WHERE s.name = :skillName")
    List<Freelancer> findBySkillName(@Param("skillName") String skillName);

    // Native SQL-запрос (альтернатива)
    @Query(value = "SELECT DISTINCT f.* FROM freelancers f " +
            "JOIN freelancer_skills fs ON f.id = fs.freelancer_id " +
            "JOIN skills s ON fs.skill_id = s.id " +
            "WHERE s.name = :skillName", nativeQuery = true)
    List<Freelancer> findBySkillNameNative(@Param("skillName") String skillName);
}