package sem8.intero.proj.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lang;
    private String label;
    private String description;
    private String alias;
    private String code;
    private String reference; // utilisé pour l'update du Bot Wikidata
    private boolean transcription; // 0 -> Code -> Infos / 1 -> Label -> Code
    private boolean type; // 0 -> Insertion / 1 -> Modification
    private String ville;
    private String codePostal;
    private String SIREN;
    private String SIRET;
    private String CA;

    public Bot() {
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isTranscription() {
        return transcription;
    }

    public void setTranscription(boolean transcription) {
        this.transcription = transcription;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSIREN() {
        return SIREN;
    }

    public void setSIREN(String sIREN) {
        SIREN = sIREN;
    }

    public String getSIRET() {
        return SIRET;
    }

    public void setSIRET(String sIRET) {
        SIRET = sIRET;
    }

    public String getCA() {
        return CA;
    }

    public void setCA(String cA) {
        CA = cA;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

}