package com.mrx.tiketku;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_buy_ticket, btnmines, btnplus;
    TextView textjumlahtiket, texttotalharga, textmybalance;
    Integer valuejumlahtiket = 1;
    Integer mybalance = 117;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 50;
    ImageView notice_balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btnmines = findViewById(R.id.btnmines);
        btnplus = findViewById(R.id.btnplus);
        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.texttotoalharga);
        textmybalance = findViewById(R.id.textmybalance);
        notice_balance = findViewById(R.id.notice_balance);

        //setting value baru untuk beberapa komponen
        textjumlahtiket.setText((valuejumlahtiket.toString()));
        valuetotalharga = valuehargatiket * valuejumlahtiket;
        texttotalharga.setText("Rp. " + valuetotalharga+"");
        textmybalance.setText("Rp. " + mybalance+"");

        //secara default btnmines hide
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);
        notice_balance.setVisibility(View.GONE);

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
                Intent gotosuccessticket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                startActivity(gotosuccessticket);
            }
        });
    }
}
