package sem8.intero.proj;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import sem8.intero.proj.model.Demande;
import sem8.intero.proj.model.Token;
import sem8.intero.proj.repository.DemandeRepository;

/**
 * AnswerController
 */
@Controller
public class AnswerController {

    private static final String URL_LOGIN = "http://qanswer-core1.univ-st-etienne.fr/api/user/signin";
    private static final String URL_QUEST = "http://qanswer-core1.univ-st-etienne.fr/api/qa/fullInterpretation";
    private static final String USER = "QNico";
    private static final String PASS = "J*M'isuMdR*20";

    @Autowired
    public DemandeRepository DemandeRepo;

    @RequestMapping("/")
    public String index(Model m) {
        m.addAttribute("demande", new Demande());
        m.addAttribute("afficheDemande", DemandeRepo.findAll());
        return "index";
    }

    @PostMapping("/demande")
    public String demande(Model m, Demande d, RedirectAttributes redirect) {
        DemandeRepo.save(d);
        ArrayList<Object> answer = sendQuestion(d.getQuestion(), d.getLangue());
        redirect.addFlashAttribute("answer", answer);
        return "redirect:/";
    }

    private ArrayList<Object> sendQuestion(String question, String langue) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Token token = getToken(restTemplate, headers);
        return getAnswer(restTemplate, headers, token, question, langue);
    }

    private Token getToken(RestTemplate restTemplate, HttpHeaders headers) {
        JSONObject body = new JSONObject();
        body.put("usernameOrEmail", USER);
        body.put("password", PASS);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        return restTemplate.postForObject(URL_LOGIN, entity, Token.class);
    }

    private ArrayList<Object> getAnswer(RestTemplate restTemplate, HttpHeaders headers, Token token, String question,
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
            if (size > 0) {
                for (int j = 0; j < size; j++) {
                    Object image = context.getJSONObject(i).getJSONArray("images").get(j);
                    answers.add(image);
                }
            }
        }
        return answers;
    }

    /* API Version Javascript */
    @RequestMapping("/recherche")
    public String recherche(Model m) {
        return "recherche";
    }

}