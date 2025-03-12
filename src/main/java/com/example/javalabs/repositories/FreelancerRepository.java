package com.example.javalabs.repositories;

import com.example.javalabs.models.Freelancer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    @Query("SELECT DISTINCT f FROM Freelancer f " +
            "LEFT JOIN f.skills s " +
            "WHERE (:category IS NULL OR f.category = :category) " +
            "AND (:skillName IS NULL OR s.name = :skillName)")
    List<Freelancer> findByCategoryAndSkill(@Param("category") String category,
                                            @Param("skillName") String skillName);
}