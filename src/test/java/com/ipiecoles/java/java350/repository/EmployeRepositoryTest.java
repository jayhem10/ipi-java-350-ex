package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.model.Employe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;


    @Test
    void testFindLastMatricule0Employe(){
        //Given
        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    void testFindLastMatricule1Employe(){
        //Given
        //Insérer des données en base
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        //When
        //Exécuter des requêtes en base
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    void testFindLastMatriculeNEmployes(){
        //Given
        //Insérer des données en base
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jon", "M40325", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jones", "C06432", LocalDate.now(), 1500d, 1, 1.0));
        //When
        //Exécuter des requêtes en base
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("40325");
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWith(){
        //Given
        //Insérer des données en base
        employeRepository.save(new Employe("Ellington", "Duke", "C12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Parker", "Charlie", "C40325", LocalDate.now(), 1500d, 3, 1.0));
        employeRepository.save(new Employe("John", "Coltrane", "C06432", LocalDate.now(), 1500d, 2, 1.0));
        //When
        //Exécuter des requêtes en base
        Double performanceAvg = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        //Then
        Assertions.assertThat(performanceAvg).isEqualTo(2);

    }

    @BeforeEach
    @AfterEach
    public void purgeBdd(){
        employeRepository.deleteAll();
    }
}