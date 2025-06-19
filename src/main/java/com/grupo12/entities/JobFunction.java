package com.grupo12.entities;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_function")
@Getter
@Setter
@NoArgsConstructor
public class JobFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idJobFunction;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "functions")
    private Set<Employee> employees;

    public JobFunction(int idJobFunction, String name, String description) {
        super();
        this.idJobFunction = idJobFunction;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobFunction that = (JobFunction) o;
        return idJobFunction == that.idJobFunction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJobFunction);
    }

}
