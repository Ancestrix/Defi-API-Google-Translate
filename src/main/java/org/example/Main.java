package org.example;
import com.google.cloud.translate.v3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    // Detecting the language of a text string
    public static String detectLanguage(String projectId, String text) throws IOException {

        String res = null;
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            DetectLanguageRequest request =
                    DetectLanguageRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setContent(text)
                            .build();

            DetectLanguageResponse response = client.detectLanguage(request);
            // Display list of detected languages sorted by detection confidence.
            // The most probable language is first.
            for (DetectedLanguage language : response.getLanguagesList()) {
                // The language detected

                //System.out.printf("Language code: %s\n", language.getLanguageCode());

                res=language.getLanguageCode();
                // Confidence of detection result for this language

                //System.out.printf("Confidence: %s\n", language.getConfidence());
            }
        }
        return res;
    }

    public static void translateText(String projectId, String targetLanguage, String text) throws IOException {

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            // Display the translation for each input text provided
            for (Translation translation : response.getTranslationsList()) {
                System.out.printf("Translated text: %s\n", translation.getTranslatedText());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner texte = new Scanner(System.in);
        System.out.println("Veuillez entrer votre texte à traduire :");
        String Text=texte.nextLine();
        List<String> langues = Arrays.asList("af", "am", "ar", "az", "be", "bg", "bn", "bs", "ca", "ceb", "co", "cs", "cy", "da", "de", "el", "en", "eo", "es",
                "et", "eu", "fa", "fi", "fr", "fy", "ga", "gd", "gl", "gu", "ha", "haw", "hi", "hmn", "hr", "ht", "hu", "hy", "id", "ig", "is", "it", "iw",
                "ja", "jw", "ka", "kk", "km", "kn", "ko", "ku", "ky", "la", "lb", "lo", "lt", "lv", "mg", "mi", "mk", "ml", "mn", "mr", "ms", "mt", "my", "ne",
                "nl", "no", "ny", "pa", "pl", "ps", "pt", "ro", "ru", "sd", "si", "sk", "sl", "sm", "sn", "so", "sq", "sr", "st", "su", "sv", "sw", "ta", "te",
                "tg", "th", "tl", "tr", "uk", "ur", "uz", "vi", "xh", "yi", "yo", "zh-CN", "zh-TW", "zu");
        Random rand = new Random();
        int random_loop = rand.nextInt(20);
        System.out.println("Le texte peut être traduit dans "+langues.size()+" langues différentes. Le texte va passer par "
                +random_loop+" traductions ");
        for (int i=0;i<random_loop;i++){
            translateText("defit-371911",langues.get(rand.nextInt(langues.size())),Text);
            if (i==random_loop-1){
                translateText("defit-371911",detectLanguage("defit-371911",Text),Text);
            }
        }

    }
}