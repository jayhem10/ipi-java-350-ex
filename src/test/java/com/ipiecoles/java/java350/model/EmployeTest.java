package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(0);
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnneeAnciennete).isNull();
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheInfNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().minusYears(5), 1500d,1,1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(5);
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheSupNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().plusYears(5), 1500d,1,1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }



    //Tests sur getPrimeAnnuelle

    @Test
    public void testGetPrimeAnnuelle1(){
        //Given
        Integer performance = 1;
        String matricule = "T12345";
        Double tauxActivite = 1.0;
        Long nbAnneeAnciennete = 0L;

        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneeAnciennete),1500d, performance, tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then
        Double primeAttendue = 1000.0;
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    @Test
    public void testGetPrimeAnnuelle2(){
        //Given
        Integer performance = 2;
        String matricule = "T12345";
        Double tauxActivite = 1.0;
        Long nbAnneeAnciennete = 0L;

        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneeAnciennete),1500d, performance, tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then
        Double primeAttendue = 1000.0;
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    // Méthodes paramétrées

    @ParameterizedTest(name = "Perf {0}, matricule {1}, tauxActivite {2}, anciennete {3},  => prime {4}")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5, 0, 500.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "1, 'T12345', 1.0, 2, 1200.0",
    })

    // Données d'entrée qui peuvent varier => tempsPartiel, poste ( manager ou autre ), performance, dateEmbauche
    void testGetPrimeAnnuelleWithParams(Integer performance, String matricule, Double tauxActivite, long nbAnneesAnciennete, Double primeAttendue) {
        //Given, When, Then
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneesAnciennete),1500d, performance, tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then

        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

}
