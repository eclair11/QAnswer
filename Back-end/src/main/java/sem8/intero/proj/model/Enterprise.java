package sem8.intero.proj.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.opencsv.bean.CsvBindByName;

@Entity
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CsvBindByName
    String siren;

    @CsvBindByName
    String nic;

    @CsvBindByName
    String siret;

    @CsvBindByName
    String dateCreationEtablissement;

    @CsvBindByName
    String numeroVoieEtablissement;

    @CsvBindByName
    String indiceRepetitionEtablissement;

    @CsvBindByName
    String typeVoieEtablissement;

    @CsvBindByName
    String libelleVoieEtablissement;

    @CsvBindByName
    String codePostalEtablissement;

    @CsvBindByName
    String enseigne1Etablissement;

    @CsvBindByName
    String longitude;

    @CsvBindByName
    String latitude;

    public Enterprise() {
    }

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

    public String getDateCreationEtablissement() {
        return dateCreationEtablissement;
    }

    public void setDateCreationEtablissement(String dateCreationEtablissement) {
        this.dateCreationEtablissement = dateCreationEtablissement;
    }

    public String getNumeroVoieEtablissement() {
        return numeroVoieEtablissement;
    }

    public void setNumeroVoieEtablissement(String numeroVoieEtablissement) {
        this.numeroVoieEtablissement = numeroVoieEtablissement;
    }

    public String getIndiceRepetitionEtablissement() {
        return indiceRepetitionEtablissement;
    }

    public void setIndiceRepetitionEtablissement(String indiceRepetitionEtablissement) {
        this.indiceRepetitionEtablissement = indiceRepetitionEtablissement;
    }

    public String getTypeVoieEtablissement() {
        return typeVoieEtablissement;
    }

    public void setTypeVoieEtablissement(String typeVoieEtablissement) {
        this.typeVoieEtablissement = typeVoieEtablissement;
    }

    public String getLibelleVoieEtablissement() {
        return libelleVoieEtablissement;
    }

    public void setLibelleVoieEtablissement(String libelleVoieEtablissement) {
        this.libelleVoieEtablissement = libelleVoieEtablissement;
    }

    public String getCodePostalEtablissement() {
        return codePostalEtablissement;
    }

    public void setCodePostalEtablissement(String codePostalEtablissement) {
        this.codePostalEtablissement = codePostalEtablissement;
    }

    public String getEnseigne1Etablissement() {
        return enseigne1Etablissement;
    }

    public void setEnseigne1Etablissement(String enseigne1Etablissement) {
        this.enseigne1Etablissement = enseigne1Etablissement;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}