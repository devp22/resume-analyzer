package com.example.resume_analyzer;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Slf4j
@RequestMapping("/api")
public class ResumeController {

    @Autowired
    private FileService fileService;

    @GetMapping("/hello")
    public String greet(){
        return "Hello there!";
    }

    @PostMapping("/file")
    public ResponseEntity<String> file(@RequestBody String fileName){
        System.out.println("hello");
        System.out.println(fileName);
        return ResponseEntity.ok(fileName);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(fileService.uploadFile(file), HttpStatus.OK);
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file, @RequestParam("description") String description){
        System.out.println("description: "+description);
        String text = "";
        try {
            PDDocument pdDocument = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            text = pdfTextStripper.getText(pdDocument);
        } catch (Exception e) {
            System.out.println("Error in extracting text: "+e.getMessage());
        }
        String resume = text.replaceAll("[^a-zA-Z0-9+#.\\s-]", " ").toLowerCase();
        description = description.replaceAll("[^a-zA-Z0-9+#.\\s-]", " ").toLowerCase();

        List<String> resumeKeywords = new ArrayList<String>();
        List<String> descriptionKeywords = new ArrayList<String>();
        List<String> keywords = getKeyWords().stream().map(String::toLowerCase).toList();
        List<String> commonKeywords = new ArrayList<String>();
       

        for (String word : keywords) {
            if (Pattern.compile("\\b" + Pattern.quote(word) + "\\b").matcher(resume).find()) {
                resumeKeywords.add(word);
            }
            
            if (Pattern.compile("\\b" + Pattern.quote(word) + "\\b").matcher(description).find()) {
                descriptionKeywords.add(word);
            }
            
        }

        for(String word : descriptionKeywords){
            if(resumeKeywords.contains(word)){
                commonKeywords.add(word);
            }
        }
        System.out.println("resumekeywords: "+resumeKeywords);
        System.out.println("descriptionKeywords: "+descriptionKeywords);
        System.out.println("commonkeywords: "+commonKeywords);
        
        String finalMessage = "Your resume has "+commonKeywords.size()+" words out of "+descriptionKeywords.size()+" words from description.";
        finalMessage = finalMessage + "\n" + "description: " + descriptionKeywords;
        finalMessage = finalMessage + "\n" + "matched: " + commonKeywords;
        return ResponseEntity.ok(finalMessage);

    }

    public List<String> getKeyWords(){
        List<String> keyWords = new LinkedList<String>(Arrays.asList());
        try {
        ClassPathResource resource = new ClassPathResource("SkillsDataSet.csv");
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> data = csvReader.readAll();

            for(String[] row : data){
                for (String cell : row){
                    keyWords.add(cell);
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error in extracting text from dataset: "+e.getMessage());
        }

        return keyWords;
    }


}