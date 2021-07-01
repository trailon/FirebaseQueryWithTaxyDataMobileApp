package com.example.yazlab2p2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class MainActivity extends AppCompatActivity {
    public TextView dataview;
    public EditText mesafe;
    public Spinner gün1,gün2;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String tipbirbir="",tipbiruc="",tipikibir="",tipikiiki="",tipikiuc="";
    public String[] day1items = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    public String[] day2items = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataview = findViewById(R.id.Veriler);
        mesafe = findViewById(R.id.Mesafe);
        gün1 = findViewById(R.id.day1);
        gün2 = findViewById(R.id.day2);
        ArrayAdapter<String> day1adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, day1items);
        ArrayAdapter<String> day2adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, day2items);
        gün1.setAdapter(day1adapter);
        gün2.setAdapter(day2adapter);
        final CollectionReference taxizonelookup = db.collection("taxizonelookup");
        final CollectionReference yellowtripdata202012 = db.collection("yellowtripdata202012");
        final Button tip1button = findViewById(R.id.tip1button);
        final Button tip2button = findViewById(R.id.tip2button);
        final Button tip3button = findViewById(R.id.tip3button);
        tip1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, tip1button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.tip1menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(item.getTitle().equals("Tip 1 - 1")){

                            Query tip11 = yellowtripdata202012.orderBy("passenger_count", Query.Direction.DESCENDING).limit(5);
                            tip11.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            tipbirbir=tipbirbir+"Yolcu Sayısı = "+document.get("passenger_count").toString() + " Gün:"+document.get("tpep_dropoff_datetime")+"\n";
                                        }
                                    }
                                }
                            });
                            dataview.setText(tipbirbir);
                            tipbirbir="";
                        }else if(item.getTitle().equals("Tip 1 - 2")){
                        }else if(item.getTitle().equals("Tip 1 - 3")){
                            Query tip13 = yellowtripdata202012.orderBy("trip_distance", Query.Direction.DESCENDING).limit(5);
                            tip13.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            tipbiruc=tipbiruc+"Mesafe = "+document.get("trip_distance").toString() + " Gün:"+document.get("tpep_dropoff_datetime")+"\n";
                                        }
                                    }
                                }
                            });
                            dataview.setText(tipbiruc);
                            tipbiruc="";
                        };
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
        tip2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, tip2button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.tip2menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(item.getTitle().equals("Tip 2 - 1")){
                            String gün1text = gün1.getSelectedItem().toString();
                            String gün2text = gün2.getSelectedItem().toString();
                            String gün1temp = "2020-12-"+gün1text+" 00:00:00";
                            String gün2temp = "2020-12-"+gün2text+" 00:00:00";
                            Query tip21 = yellowtripdata202012.whereGreaterThan("tpep_dropoff_datetime",gün1temp).whereLessThan("tpep_dropoff_datetime",gün2temp);
                            tip21.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            tipikibir=tipikibir+"Gün: "+document.get("tpep_dropoff_datetime").toString()+" Mesafe: "+document.get("trip_distance").toString()+"\n";
                                        }
                                    }
                                }
                            });
                            dataview.setText(tipikibir);
                            tipikibir="";
                        }else if(item.getTitle().equals("Tip 2 - 2")){
                            Query tip22 = yellowtripdata202012.orderBy("trip_distance",Query.Direction.ASCENDING).limit(5);
                            tip22.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            tipikiiki=tipikiiki+"Gün: "+document.get("tpep_dropoff_datetime").toString()+" Mesafe: "+document.get("trip_distance").toString()+"\n";
                                        }
                                    }
                                }
                            });
                            dataview.setText(tipikiiki);
                            tipikiiki="";
                        }else if(item.getTitle().equals("Tip 2 - 3")){
                            String gün1text = gün1.getSelectedItem().toString();
                            String gün2text = gün2.getSelectedItem().toString();
                            String gün1temp = "2020-12-"+gün1text+" 00:00:00";
                            String gün2temp = "2020-12-"+gün2text+" 00:00:00";
                            Query tip23 = yellowtripdata202012.whereGreaterThan("tpep_dropoff_datetime",gün1temp).whereLessThan("tpep_dropoff_datetime",gün2temp);
                            tip23.orderBy("trip_distance",Query.Direction.ASCENDING).limit(5);
                            tip23.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            tipikiuc=tipikiuc+"Gün: "+document.get("tpep_dropoff_datetime").toString()+" Mesafe: "+document.get("trip_distance").toString()+"\n";
                                        }
                                    }
                                }
                            });
                            dataview.setText(tipikiuc);
                            tipikiuc="";
                        };
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
        tip3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, tip3button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.tip3menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
    }
}