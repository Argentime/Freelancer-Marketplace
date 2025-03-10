package com.example.javalabs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<Freelancer> freelancers;

    // Конструкторы, геттеры и сеттеры
    public Skill() {}

    public Skill(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Freelancer> getFreelancers() { return freelancers; }
    public void setFreelancers(Set<Freelancer> freelancers) { this.freelancers = freelancers; }
}