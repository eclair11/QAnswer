package sem8.intero.proj.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.opencsv.bean.CsvBindByName;

/**
 * Enterprise
 */
@Entity
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
/*
    @CsvBindByName(column = "SIREN")
    String siren;

    @CsvBindByName(column = "NIC")
    String nic;

    @CsvBindByName
    String siret;

    @CsvBindByName(column = "Date de création de l'établissement")
    String creation;

    @CsvBindByName(column = "Tranche de l'effectif de l'établissement")
    String effectif;

    @CsvBindByName(column = "Catégorie de l'entreprise")
    String categorie;

    @CsvBindByName(column = "Numéro de voie de l'établissement")
    String num_voie;

    @CsvBindByName(column = "Indice de répétition de l'établissement")
    String ind_repet;

    @CsvBindByName(column = "Type de voie de l'établissement")
    String type_voie;

    @CsvBindByName(column = "Libellé de la voie de l'établissement")
    String nom_voie;

    @CsvBindByName(column = "Code postal de l'établissement")
    String code_postal;

    @CsvBindByName(column = "Enseigne de l'établissement 1")
    String enseigne;

    @CsvBindByName(column = "Section de l'établissement")
    String section;

    @CsvBindByName(column = "Groupe de l'établissement")
    String groupe;

    @CsvBindByName(column = "Civilité de la personne physique")
    String type_personne;

    @CsvBindByName(column = "Prénom de la personne physique 1")
    String prenom_personne;

    @CsvBindByName(column = "Nom de la personne physique")
    String nom_personne;

    @CsvBindByName(column = "Nature juridique de l'unité légale")
    String nature_juridique;

    @CsvBindByName(column = "Géolocalisation de l'établissement")
    String localisation;
*/
    public Enterprise() {
    }
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiren() {
        return siren;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getEffectif() {
        return effectif;
    }

    public void setEffectif(String effectif) {
        this.effectif = effectif;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNum_voie() {
        return num_voie;
    }

    public void setNum_voie(String num_voie) {
        this.num_voie = num_voie;
    }

    public String getInd_repet() {
        return ind_repet;
    }

    public void setInd_repet(String ind_repet) {
        this.ind_repet = ind_repet;
    }

    public String getType_voie() {
        return type_voie;
    }

    public void setType_voie(String type_voie) {
        this.type_voie = type_voie;
    }

    public String getNom_voie() {
        return nom_voie;
    }

    public void setNom_voie(String nom_voie) {
        this.nom_voie = nom_voie;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getEnseigne() {
        return enseigne;
    }

    public void setEnseigne(String enseigne) {
        this.enseigne = enseigne;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getType_personne() {
        return type_personne;
    }

    public void setType_personne(String type_personne) {
        this.type_personne = type_personne;
    }

    public String getPrenom_personne() {
        return prenom_personne;
    }

    public void setPrenom_personne(String prenom_personne) {
        this.prenom_personne = prenom_personne;
    }

    public String getNom_personne() {
        return nom_personne;
    }

    public void setNom_personne(String nom_personne) {
        this.nom_personne = nom_personne;
    }

    public String getNature_juridique() {
        return nature_juridique;
    }

    public void setNature_juridique(String nature_juridique) {
        this.nature_juridique = nature_juridique;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
*/
}