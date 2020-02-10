package sem8.intero.proj;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import sem8.intero.proj.Repository.DemandeRepository;
import sem8.intero.proj.model.Demande;
import sem8.intero.proj.model.Token;
import sem8.intero.proj.model.Upload;

/**
 * IndexController
 */
@Controller
public class IndexController {

    private static final String URL_LOGIN = "http://qanswer-core1.univ-st-etienne.fr/api/user/signin";
    private static final String URL_QUEST = "http://qanswer-core1.univ-st-etienne.fr/api/qa/fullInterpretation";
    private static final String USER = "QNico";
    private static final String PASS = "J*M'isuMdR*20";
    private static final String LANG = "fr";
    //private static final String DATA = "wikidata";

    @Autowired
    public DemandeRepository DemandeRepo;

    @RequestMapping("/")
    public String index(Model m) {
        m.addAttribute("demande", new Demande());
        m.addAttribute("afficheDemande", DemandeRepo.findAll());
        return "index";
    }

    @RequestMapping("/parser")
    public String parser(Model m) {
        m.addAttribute("upload", new Upload());
        return "parser";
    }

    @PostMapping("/demande")
    public String demande(Model m, Demande d, RedirectAttributes redirect) {
        DemandeRepo.save(d);
        ArrayList<Object> answer = sendQuestion(d.getQuestion());
        redirect.addFlashAttribute("answer", answer);
        return "redirect:/";
    }

    @PostMapping("/addlinks")
    public String addLinks(Model m, @ModelAttribute("upload") Upload upload) {
        Document doc = null;
        try {
            doc = Jsoup.connect(upload.getLink()).get();
        } catch (IOException e) {
            System.err.println(e);
        }
        m.addAttribute("document", doc);
        return "display";
    }

    @PostMapping("/addfiles")
    public String addFiles(Model m, @ModelAttribute("upload") Upload upload) throws IOException {
        String uploadRootPath = "src/main/resources/PDF2TXT/";
        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        MultipartFile[] fileDatas = upload.getFiles();
        for (MultipartFile fileData : fileDatas) {
            String name = fileData.getOriginalFilename();
            if (name != null && name.length() > 0) {
                String link = uploadRootDir.getAbsolutePath() + File.separator + name;
                parserPDF(fileData, link);
            }
        }
        return "redirect:/parser";
    }

    private void parserPDF(MultipartFile data, String link) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdfDoc = PDDocument.load(data.getInputStream());
        String parsedText = pdfStripper.getText(pdfDoc);
        PrintWriter pw = new PrintWriter(link + ".txt");
        pw.print(parsedText);
        pw.close();
    }

    private <T> List<T> parserCSV(Class<T> type, String fileName) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        File file = new ClassPathResource(fileName).getFile();
        MappingIterator<T> readValues = mapper.reader(type).with(bootstrapSchema).readValues(file);
        return readValues.readAll();
    }

    private ArrayList<Object> sendQuestion(String question) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Token token = getToken(restTemplate, headers);
        return getAnswer(restTemplate, headers, token, question);
    }

    private Token getToken(RestTemplate restTemplate, HttpHeaders headers) {
        JSONObject body = new JSONObject();
        body.put("usernameOrEmail", USER);
        body.put("password", PASS);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        return restTemplate.postForObject(URL_LOGIN, entity, Token.class);
    }

    private ArrayList<Object> getAnswer(RestTemplate restTemplate, HttpHeaders headers, Token token, String question) {
        ArrayList<Object> answers = new ArrayList<>();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token.getTokenType() + " " + token.getAccessToken());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_QUEST).queryParam("question", question)
                .queryParam("lang", LANG);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> request = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
        JSONObject body = new JSONObject(request.getBody());
        JSONArray context = body.getJSONArray("qaContexts");
        for (int i = 0; i < context.length(); i++) {
            Object answer = context.getJSONObject(i).get("literal");
            answers.add(answer);
        }
        return answers;
    }

}