package com.example.contacts.activities;

import androidx.annotation.CallSuper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.R;
import com.example.contacts.Validation;

import java.util.ArrayList;

public class MainActivity extends DBActivity implements Validation {
    protected EditText editName, editPhone, editTypeOfDoctor;
    protected Button btnInsert;
    protected Button btnCovidInfo;
    protected ListView listView;
    protected NewsActivity newsActivity;

    protected void FillListView() throws Exception{
        final ArrayList<String> listResults
                = new ArrayList<>();
        SelectSQL(
                "SELECT * FROM DOCTORS " +
                        "ORDER BY Name ",
                null,
                (ID, Name, Phone, TypeOfDoctor)->
                        listResults.add(ID+"\t"+Name+"\t"+TypeOfDoctor+"\t"+Phone+"\n")

        );
        listView.clearChoices();
        ArrayAdapter<String> arrayAdapter=
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.activity_list_view,
                        R.id.textView,
                        listResults
                );
        listView.setAdapter(arrayAdapter);

    }



    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=findViewById(R.id.editName);
        editPhone=findViewById(R.id.editPhone);
        editTypeOfDoctor=findViewById(R.id.editTypeOfDoctor);
        btnInsert=findViewById(R.id.btnInsert);
        listView=findViewById(R.id.listView);
        btnCovidInfo=(Button) findViewById(R.id.covidInformation);
        btnCovidInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        Validation.Validate(editName,
                editPhone,
                editTypeOfDoctor,
                () -> btnInsert.setEnabled(true),
                () -> btnInsert.setEnabled(false));

        //on text change events
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validation.Validate(editName,
                                    editPhone,
                                    editTypeOfDoctor,
                        () -> btnInsert.setEnabled(true),
                        () -> btnInsert.setEnabled(false));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editName.addTextChangedListener(watcher);
        editPhone.addTextChangedListener(watcher);
        editTypeOfDoctor.addTextChangedListener(watcher);

        try {
            InitDB();
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("",e.getLocalizedMessage().toString());
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected="";
                TextView clickedText = view.findViewById(R.id.textView);
                selected=clickedText.getText().toString();
                String [] elements = selected.split("\t");
                elements[3]=elements[3].trim();
                Intent intent= new Intent(MainActivity.this, DoctorsActivity.class);
                Bundle b= new Bundle();
                b.putString("ID", elements[0]);
                b.putString("Name", elements[1]);
                b.putString("TypeOfDoctor", elements[2]);
                b.putString("Phone", elements[3]);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);



            }
        });

        btnInsert.setOnClickListener(view->{
            try{
                ExecSQL(
                        "INSERT INTO DOCTORS(Name, Phone, TypeOfDoctor ) " +
                                "VALUES(?, ?, ?) ",
                        new Object[]{
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editTypeOfDoctor.getText().toString()
                        },
                        ()->Toast.makeText(getApplicationContext(), "INSERT SUCCESS",
                                Toast.LENGTH_LONG).show()
                );
                FillListView();

            }catch (Exception e){
                e.printStackTrace();
                Log.d("",e.getLocalizedMessage().toString());
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }


        });





    }
}