package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDelete extends DBActivity implements Validation {

    protected EditText editName, editTel, editEmail;
    protected Button btnUpdate, btnDelete;
    protected String ID;


    protected boolean MatchString (String regex1, String text) {
        final String regex = regex1; //;
        final String string = text;
        /*"+35955555555\n"
                + "032/23-23-23\n"
                + "02/555555555\n"
                + "+359 (32) 55555555\n"
                + "23 23 23\n"
                + "00359 (32) 555555:232\n"
                + "(+359) 33333333";*/

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            return true;
        }

        return false;
    }

    protected void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(UpdateDelete.this,
                MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName=findViewById(R.id.editName);
        editTel=findViewById(R.id.editTel);
        editEmail=findViewById(R.id.editEmail);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editTel.setText(b.getString("Tel"));
            editEmail.setText(b.getString("Email"));
        }

        Validation.Validate(editName,
                editTel,
                editEmail,
                () -> btnUpdate.setEnabled(true),
                () -> btnUpdate.setEnabled(false));

        btnDelete.setOnClickListener(view->{
            try{
                ExecSQL(
                        "DELETE FROM KONTAKTI " +
                                "WHERE ID = ?",
                        new Object[]{ID},
                        ()->Toast.makeText(getApplicationContext(),
                                "DELETE SUCCEEDED",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });

        btnUpdate.setOnClickListener(view->{
            try{
                ExecSQL(
                        "UPDATE KONTAKTI SET " +
                                "Name = ?, " +
                                "Tel = ?, " +
                                "Email = ? " +
                                "WHERE ID = ? ",
                        new Object[]{
                                editName.getText().toString(),
                                editTel.getText().toString(),
                                editEmail.getText().toString(),
                                ID
                        },
                        ()->Toast.makeText(getApplicationContext(),
                                "UPDATE SUCCEEDED",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });




    }
}