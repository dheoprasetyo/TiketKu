package com.mrx.tiketku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_buy_ticket, btnmines, btnplus;
    TextView textjumlahtiket, texttotalharga, textmybalance, nama_wisata, lokasi, ketentuan;
    Integer valuejumlahtiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView notice_balance;
    DatabaseReference reference, reference2, reference3;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";


    //generate nomor integer secara rancdom
    Integer nomor_transaksi = new Random().nextInt();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);
        getUsernameLocal();
        //mengambil data dar intent
        Bundle bundle = getIntent().getExtras();
        String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btnmines = findViewById(R.id.btnmines);
        btnplus = findViewById(R.id.btnplus);
        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.texttotalharga);
        textmybalance = findViewById(R.id.textmybalance);
        nama_wisata = findViewById(R.id.nama_wisata);
        notice_balance = findViewById(R.id.notice_balance);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        //setting value baru untuk beberapa komponen
        textjumlahtiket.setText(valuejumlahtiket.toString());




        //secara default btnmines hide
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);
        notice_balance.setVisibility(View.GONE);

        //mengambil data user dari firebase

        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("Rp. " + mybalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //megambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("Rp. " + valuetotalharga+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valuejumlahtiket += 1;
                textjumlahtiket.setText((valuejumlahtiket.toString()));

                if (valuejumlahtiket>1){
                    btnmines.animate().alpha(1).setDuration(300).start();
                    btnmines.setEnabled(true);
                }
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("Rp. " + valuetotalharga+"");
                if (valuetotalharga>mybalance){
                    btn_buy_ticket.animate().translationY(250).alpha(0).setDuration(350);
                    btn_buy_ticket.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_balance.setVisibility(View.VISIBLE);
                }
            }
        });

        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valuejumlahtiket -= 1;
                textjumlahtiket.setText((valuejumlahtiket.toString()));

                if (valuejumlahtiket<2){
                    btnmines.animate().alpha(0).setDuration(300).start();
                    btnmines.setEnabled(false);
                }
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("Rp. " + valuetotalharga+"");
                if (valuetotalharga<mybalance){
                    btn_buy_ticket.animate().translationY(0).alpha(1).setDuration(350);
                    btn_buy_ticket.setEnabled(true);
                    textmybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_balance.setVisibility(View.GONE);
                }
            }
        });



        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mengambil data user kepada firebase dan membuat tbale baru myticket

                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valuejumlahtiket);
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gotosuccessticket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccessticket);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
