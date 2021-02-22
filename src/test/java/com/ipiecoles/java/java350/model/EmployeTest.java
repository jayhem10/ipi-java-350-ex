package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.Java350Application;
import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.Serializable;
import java.time.LocalDate;

class EmployeTest {

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnneeAnciennete).isNull();
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheInfNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().minusYears(5), 1500d,1,1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(5);
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheSupNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().plusYears(5), 1500d,1,1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }


    //Tests sur getPrimeAnnuelle

    @Test
    void testGetPrimeAnnuelle1(){
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
    void testGetPrimeAnnuelle2(){
        //Given
        Integer performance = 2;
        String matricule = "T12345";
        Double tauxActivite = 1.0;
        Long nbAnneeAnciennete = 0L;

        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneeAnciennete),1500d, performance, tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then
        Double primeAttendue = 2300.0;
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    // Méthodes paramétrées

    @ParameterizedTest(name = "Perf {0}, matricule {1}, txActivite {2}, anciennete {3} => prime {4}")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5, 0, 500.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "1, 'T12345', 1.0, 2, 1200.0",
            })
    void testGetPrimeAnnuelle(Integer performance, String matricule, Double tauxActivite, Long nbAnneesAnciennete,
                                     Double primeAttendue){
        //Given
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 1500d,
                performance, tauxActivite);
        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    @Test
    void testGetPrimeAnnuelleMatriculeNull(){
        //Given
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);
        //When
        Double prime = employe.getPrimeAnnuelle();
        //Then
        Assertions.assertThat(prime).isEqualTo(1000.0);
    }



    //Tests sur la méthode augmenterSalaire

    //Test augmentation de base
    @Test
    void testSalaireAugmentation(){
        //Given
            Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);
            Double pourcentage = 5d;
        //When
            Object newSalaire = employe.augmenterSalaire(pourcentage);

        //Then
            Assertions.assertThat(newSalaire).isEqualTo(1575d);
    }

    //ifAugmentationWithNégatifOuZero
    @Test
    void testSalairePourcentageAugmentationNegatifOrZero() {
        //Given
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);
        Double pourcentageNegatif = -5d;
        Double pourcentageZero = 0d;

        //When
        Object newSalaire = employe.augmenterSalaire(pourcentageNegatif);
        Object newSalaire2 = employe.augmenterSalaire(pourcentageZero);

        //Then
        Assertions.assertThat(newSalaire).isEqualTo("Le salaire ne peut être augmenté avec un pourcentage négatif ou égal à 0.");
        Assertions.assertThat(newSalaire2).isEqualTo("Le salaire ne peut être augmenté avec un pourcentage négatif ou égal à 0.");
    }

    // IfSalaireEmployeIsEqualToZero
    @Test
    void testSalaireIfZero()  {
        //Given
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 0d, 1, 1.0);
        Double pourcentage = 5d;

        //When
        Object newSalaire = employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(newSalaire).isEqualTo("Le salaire ne peut être égal à 0.");
    }

    // IfSalaireEmployeIsNull
    @Test
    void testSalaireIfNull() {
        //Given
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), null, 1, 1.0);

        //When
        Object newSalaire = employe.augmenterSalaire(5);

        //Then
        Assertions.assertThat(newSalaire).isEqualTo("Le salaire ne peut être null.");
    }

//Test calcul nbRtt
    @ParameterizedTest
    @CsvSource({
            "'2019-01-01', 8",
            "'2020-01-01', 10",
            "'2021-01-01', 10",
            "'2022-01-01', 10",
            "'2032-01-01', 11"
    })
    void testNbRTT(LocalDate date, Integer nbDeRTTAttendu) {
        //Given
        Employe employe = new Employe("Mandela", "Nelson", "M00001", LocalDate.now(), 1600d, 6, 1d);

        //When
        Integer nbRtt = employe.getNbRtt(date);

        //Then
        Assertions.assertThat(nbRtt).isEqualTo(nbDeRTTAttendu);
    }



}
