package com.example.multi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    TextToSpeech t1;
    Spinner languageSpinner;
    Locale selectedLocale = Locale.UK; // Default language
    HashMap<String, Locale> languageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.editText);
        languageSpinner = findViewById(R.id.languageSpinner);
        Button convertButton = findViewById(R.id.convert);


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    t1.setLanguage(selectedLocale); // Set default language
                }
            }
        });


        setupLanguageSpinner();


        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });


        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                selectedLocale = languageMap.get(selectedLanguage); // Get Locale for selected language
                t1.setLanguage(selectedLocale);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void convert() {
        String tospeak = e1.getText().toString().trim();
        if (!tospeak.isEmpty()) {
            t1.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            e1.setError("Please enter some text");
        }
    }


    private void setupLanguageSpinner() {

        String[] languages = {"English (UK)", "English (US)", "French", "German", "Spanish", "Italian", "Hindi","Japanese"};
        languageMap = new HashMap<>();
        languageMap.put("English (UK)", Locale.UK);
        languageMap.put("English (US)", Locale.US);
        languageMap.put("French", Locale.FRENCH);
        languageMap.put("German", Locale.GERMAN);
        languageMap.put("Spanish", new Locale("es", "ES"));
        languageMap.put("Italian", Locale.ITALIAN);
        languageMap.put("Hindi", new Locale("hi", "IN"));
        languageMap.put("Japanese", Locale.JAPANESE);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
}
