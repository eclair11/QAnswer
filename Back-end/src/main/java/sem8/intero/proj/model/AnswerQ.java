package sem8.intero.proj.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * AnswerQ
 */
public class AnswerQ {

    private static final String URL_LOGIN = "http://qanswer-core1.univ-st-etienne.fr/api/user/signin";
    private static final String URL_QUEST = "http://qanswer-core1.univ-st-etienne.fr/api/qa/fullInterpretation";
    private static final String USER = "QNico";
    private static final String PASS = "J*M'isuMdR*20";

    public static ArrayList<Object> sendQuestion(String question, String langue) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Token token = getToken(restTemplate, headers);
        return getAnswer(restTemplate, headers, token, question, langue);
    }

    public static Token getToken(RestTemplate restTemplate, HttpHeaders headers) {
        JSONObject body = new JSONObject();
        body.put("usernameOrEmail", USER);
        body.put("password", PASS);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        return restTemplate.postForObject(URL_LOGIN, entity, Token.class);
    }

    public static ArrayList<Object> getAnswer(RestTemplate restTemplate, HttpHeaders headers, Token token,
            String question,
            String langue) {
        ArrayList<Object> answers = new ArrayList<>();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token.getTokenType() + " " + token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_QUEST).queryParam("question", question)
                .queryParam("lang", langue);
        String uri = builder.build().toUriString();
        HttpEntity<String> request = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        JSONObject body = new JSONObject(request.getBody());
        JSONArray context = body.getJSONArray("qaContexts");
        Object inter = body.get("interpretation");
        answers.add(inter);
        for (int i = 0; i < context.length(); i++) {
            Object literal = context.getJSONObject(i).get("literal");
            if (!literal.equals(null)) {
                answers.add(literal);
            }
            int size = context.getJSONObject(i).getJSONArray("images").length();

            ArrayList<String> memoire = new ArrayList<>();
            boolean imagePresente = false;

            if (size > 0) {
                for (int j = 0; j < size; j++) {

                    Object image = context.getJSONObject(i).getJSONArray("images").get(0);

                    for (String string : memoire) {
                        if (string.equals(image.toString())) {
                            imagePresente = true;
                        }
                    }

                    if (!imagePresente) {
                        answers.add(image);
                        memoire.add(image.toString());
                    }
                    imagePresente = false;
                }
            }

            // Ensemble des liens
            JSONObject links = context.getJSONObject(i).getJSONObject("links");
            Object url = context.getJSONObject(i).get("uri");
            if (!url.equals(null)) {
                if (links.has("wikipedia")) {
                    String linksWikipedia = links.getString("wikipedia");
                    answers.add(linksWikipedia);
                }
                if (links.has("wikidata")) {
                    String linksWikidata = links.getString("wikidata");
                    answers.add(linksWikidata);
                }
            }

        }

        return answers;
    }
    
}