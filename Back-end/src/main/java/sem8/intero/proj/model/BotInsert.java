package sem8.intero.proj.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;

/**
 * Bot
 */
public class BotInsert {

    final static String siteIri = "https://qanswer-eu.univ-st-etienne.fr/index.php";

    /* Assure la connexion au bot de QAnswer_Wikidata */
    public static ApiConnection connexion() throws LoginFailedException {

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection connexionBotWiki = new ApiConnection("https://qanswer-eu.univ-st-etienne.fr/api.php");

        // Put in the first place the user with which you created the bot account
        // Put as password what you get when you create the bot account
        connexionBotWiki.login("M1wiki", "Rayman@alf2r2b1c266029j5ejlt317li2je704");

        return connexionBotWiki;
    }

    /**
     * Example for getting information about an entity, here the example of Pierre
     * Maret, Q1342 For more examples give a look at:
     * https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
     */
    public static ItemDocument infoEntity(WikibaseDataFetcher wbdf, String entity) throws MediaWikiApiErrorException {
        /* entity de la forme par exemple : "Q256" */
        ItemDocument info = (ItemDocument) wbdf.getEntityDocument(entity);
        // System.out.println(info.getEntityId());
        System.out.println(info.getLabels());
        // System.out.println(info.getStatementGroups());
        // System.out.println(info.getDescriptions());
        return info;
    }

    /*
     * Retourne une ArrayList contenant les ID des entités en fonction d'un label
     * donné
     */
    public static ArrayList<String> chercheIDByLabel(WikibaseDataFetcher wbdf, String label, String lang)
            throws MediaWikiApiErrorException {

        ArrayList<String> id = new ArrayList<>();

        // Example to search for an ID given the label
        List<WbSearchEntitiesResult> entities = wbdf.searchEntities(label, lang);
        for (WbSearchEntitiesResult entity : entities) {
            ItemDocument ent = (ItemDocument) wbdf.getEntityDocument(entity.getEntityId());
            // ent.getStatementGroups().get(0).getStatements().get(0).getClaim().getValue().toString();
            if (!entity.getEntityId().isEmpty()) {
                System.out.println(entity.getEntityId());
                id.add(entity.getEntityId());
            }
        }
        return id;
    }

    /* Fonction principale de l'insertion de données par le Bot QAnswer/Wikidata */
    public static String insertEntiteBot(String label, String description, String lang)
            throws MediaWikiApiErrorException, IOException, LoginFailedException {

        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        // ItemDocument info = infoEntity(wbdf, "Q2");

        // ArrayList<String> IDList = chercheIDByLabel(wbdf, "Saint-Etienne");

        // Example for writing information about an entity, here the example of creating
        // MisterX living in Chambon Sur Lignon
        // For more examples give a look at:
        // https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/EditOnlineDataExample.java
        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);

        /* Récupération des informations des propriétés */
        PropertyDocument propertyTravaille = (PropertyDocument) wbdf.getEntityDocument("P163");
        // System.out.println(propertyTravaille.getLabels());

        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
        // Statement statement1 = StatementBuilder
        // .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
        // .withValue(Datamodel.makeStringValue("bluuuu")).build();

        StatementBuilder s = StatementBuilder.forSubjectAndProperty(noid, propertyTravaille.getPropertyId());
        s.withValue(Datamodel.makeItemIdValue("Q252", siteIri));

        Statement stat = s.build();

        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid).withLabel(label, lang)
                .withDescription(description, lang).withStatement(stat).build();

        if (wbdf.searchEntities(label).isEmpty()) {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument, "Statement created by our bot");
            return "Vous avez créé l'entité " + itemDocument.findLabel("en");
        } else {
            System.out.println("Inutile, l'entité " + itemDocument.findLabel("en") + " existe déjà");
            return "Inutile, l'entité " + itemDocument.findLabel("en") + " existe déjà";
        }

    }

    /* Retourne 'true' si une entité est présente, 'false' sinon */
    public static boolean estEntitePresente(String label) throws LoginFailedException, MediaWikiApiErrorException {

        /* Connexion à la base et instanciation des objets WikiFetcher et WikiEditor */
        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        if (wbdf.searchEntities(label).isEmpty()) {
            return false;
        }

        return true;
    }

    /* Mise à jour de données existantes */
    public static String updateBot(String reference, String label, String description, String lang)
            throws LoginFailedException, MediaWikiApiErrorException, IOException {

        /* Connexion à la base et instanciation des objets WikiFetcher et WikiEditor */
        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);

        /* Recherche de L'Id de l'entité ou de la propriété */
        ArrayList<String> IDList = chercheIDByLabel(wbdf, reference, lang);
        String IdPouQ = IDList.get(0);

        /* IdValue propre à l'update */
        ItemIdValue idValue = Datamodel.makeWikidataItemIdValue(IdPouQ);
        long revisionId = wbdf.getEntityDocument(IdPouQ).getRevisionId();

        /*  */
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(idValue).withLabel(label, lang)
                .withDescription(description, lang).withRevisionId(revisionId).build();

        ItemDocument newItemDocument = wbde.editItemDocument(itemDocument, false, "test d'update");
        return "Vous avez mis à jour l'entité " + itemDocument.findLabel("en");

    }

    /* Cherche l'information sur */
    public static String rechercheBot() throws LoginFailedException {

        /* Connexion à la base et instanciation des objets WikiFetcher */
        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        return "";
    }

    /**
     * Traduit un label d'entité en code et inversement type : false => code ->
     * entité : true => entité -> code
     * 
     * @throws MediaWikiApiErrorException
     */
    public static String transcritEntiteCode(String label, String code, String lang, boolean type)
            throws LoginFailedException, MediaWikiApiErrorException {

        /* Connexion à la base et instanciation des objets WikiFetcher */
        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        System.out.println(label);
        System.out.println(code);
        System.out.println(lang);
        System.out.println(type);

        if (type == true) {
            /* Recherche de L'Id de l'entité ou de la propriété avec le label */

            ArrayList<String> infoId = new ArrayList<>();
            infoId = chercheIDByLabel(wbdf, label, lang);
            if (infoId.isEmpty()) {
                return "A moins d'une erreur d'ortographe de votre part, cette référence n'est pas encore inscrite dans la database";
            } else {
                String IDCode = infoId.get(0);
                return IDCode;
            }

        } else {
            /* Recherche des infos avec l'Id de l'entité ou de la propriété */
            ItemDocument infoEntite;

            if (label.startsWith("Q")) {
                if (wbdf.getEntityDocument(label) != null) {
                    infoEntite = (ItemDocument) wbdf.getEntityDocument(label);
                    String ReponseLabel = infoEntite.findLabel(lang);
                    String ReponseDescription = infoEntite.findDescription(lang);
                    return "[Code] => " + label + " / " + 
                        "\n[Label] => " + ReponseLabel + " / " + 
                        "\n[Description] => " + ReponseDescription;
                } 
                else {
                    return "L'entité [" + label + "] n'existe pas encore";
                }
            } else {
                return "Arrêtez de tapper n'importe quoi SVP ! Une entité commence par Q";
            }

        }

    }

    /* Retourne une proporiete en fonction d'un code de type 'P163' */
    public static String infoPropiete(String propriete) throws MediaWikiApiErrorException, LoginFailedException {

        ApiConnection con = connexion();
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);

        /* Récupération des informations des propriétés */
        PropertyDocument propertyTravaille = (PropertyDocument) wbdf.getEntityDocument("P163");
        System.out.println(propertyTravaille.getLabels());
        

        //ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
        // Statement statement1 = StatementBuilder
        // .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
        // .withValue(Datamodel.makeStringValue("bluuuu")).build();

        return propriete;

    }

}
