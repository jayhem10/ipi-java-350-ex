package com.ipiecoles.java.java350.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void test(){
        //Given
        //Insérer des données en base

        //When
        //Exécuter des requêtes en base
        employeRepository.findLastMatricule();

        //Then
    }

}