package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //GIVEN
        String nom = "Doe";
        String prenom ="John";
        Poste poste =Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempPartiel = 1.0;
        //Simuler qu'aucun matricule n'est présent
        when(employeRepository.findLastMatricule()).thenReturn(null);


        when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //WHEN

        employeService.embaucheEmploye(nom,prenom, poste, niveauEtude,tempPartiel);


        //THEN

        // creer un capteur d'argument de la classe employe
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);

        // le mock va récuperer la valeur passer en parametre de la méthode save.lors de son premiere appel
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        // grace au captor
        Employe employe = employeCaptor.getValue();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");

    }

    @Test
    void testEmbaucheLimiteMatricule() {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'il y a 99999 employés en base (ou du moins que le matricule le plus haut
        //est X99999
        when(employeRepository.findLastMatricule()).thenReturn("99999");
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }

    }

    @Test
    void testEmbaucheEmployeExisteDeja() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule renvoie un employé (un employé a été embauché entre temps)
        when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        }catch (Exception e){
            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }
    }

    @ParameterizedTest
    @CsvSource({
            " 0 , 1 ", //Cas 1
            " 2399 , 1 ", //Cas 2
            " 2851 , 1 ", //Cas 3
            " 3151 , 3 ", //Cas 4
            " 3601 , 6", //Cas 5
    })
    void testCalculPerformanceCommercial(Long caTraite, Integer newPerformance) throws EmployeException {
        //Given
        String matricule = "C00001";
        Long objectifCa = 3000L;
        Employe employe = new Employe("Mercury","Freddy",matricule,LocalDate.now(), Entreprise.SALAIRE_BASE,1,1d);
        when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1d);
        when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());
        Employe employeWithNewPerformance = employeCaptor.getValue();
        Assertions.assertThat(employeWithNewPerformance.getPerformance()).isEqualTo(newPerformance);
    }

    @ParameterizedTest
    @CsvSource({
            "C00001, -1000, 1000, Le chiffre d'affaire traité ne peut être négatif ou null !",
            "C00001, , 1000, Le chiffre d'affaire traité ne peut être négatif ou null !",

            "C00001, 1000, -1000, L'objectif de chiffre d'affaire ne peut être négatif ou null !",
            "C00001, 1000, , L'objectif de chiffre d'affaire ne peut être négatif ou null !",

            "T00001, 1000, 1000, Le matricule ne peut être null et doit commencer par un C !",
            ", 1000, 1000, Le matricule ne peut être null et doit commencer par un C !",

            "C00001, 1000, 1000, Le matricule C00001 n'existe pas !",
    })
    void testCalculPerformanceCommercialExceptions(String matricule, Long caTraite, Long objectifCa, String exceptionMessage) {
        //Given
        try {
            //When
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait du lancer une exception");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo(exceptionMessage);
        }
    }
}


