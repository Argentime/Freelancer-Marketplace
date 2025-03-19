package com.example.javalabs.repositories;

import com.example.javalabs.models.Freelancer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    @Query(value = "SELECT DISTINCT f.* FROM freelancers f " +
            "LEFT JOIN freelancer_skills fs on f.id = fs.freelancer_id " +
            "LEFT JOIN skills s on fs.skill_id = s.id " +
            "WHERE (:category IS NULL OR f.category = :category) " +
            "AND (:skillName IS NULL OR s.name = :skillName)", nativeQuery = true)
    List<Freelancer> findByCategoryAndSkill(@Param("category") String category,
                                            @Param("skillName") String skillName);
}