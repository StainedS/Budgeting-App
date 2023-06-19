package com.example.budgetingapp;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DailyAnalyticsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;
    private FirebaseAuth mAuth;
    private String onlineUserId="";
    private DatabaseReference expensesRef,personalRef;
    private TextView totalBudgetAmountTextView,analyticsTransportAmount,analyticsFoodAmount,analyticsHouseExpensesAmount,analyticsEntertainmentAmount, analyticsApparelAmount,analyticsPersonalExpensesAmount,analyticsOtherAmount;
    private TextView analyticsEducationAmount,analyticsCharityAmount,analyticsHealthAmount;
    private RelativeLayout linearLayoutFood,linearLayoutTransport,linearLayoutFoodHouse,linearLayoutEntertainment,linearLayoutEducation;
    private RelativeLayout linearLayoutCharity,linearLayoutApparel,linearLayoutHealth,linearLayoutPersonalExp,linearLayoutOther,linearLayoutAnalysis;
    private AnyChartView anyChartView;
    private TextView monthSpentAmount,monthRatioSpending,progress_ratio_transport,progress_ratio_food,progress_ratio_house,progress_ratio_ent,progress_ratio_edu,progress_ratio_cha,progress_ratio_app,progress_ratio_hes,progress_ratio_per,progress_ratio_oth;
    private ImageView status_Image_transport,status_Image_food,status_Image_house,status_Image_ent,status_Image_edu,status_Image_cha,status_Image_app,status_Image_hea,status_Image_per,status_Image_oth, monthRatioSpending_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_analytics);

        settingsToolbar=findViewById(R.id.my_Feed_Toolbar);
        setSupportActionBar(settingsToolbar);//support action barr
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Today Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId=mAuth.getCurrentUser().getUid();
        expensesRef= getInstance().getReference("expenses").child(onlineUserId);
        personalRef= getInstance().getReference("personal").child(onlineUserId);

        totalBudgetAmountTextView=findViewById(R.id.totalBudgetAmountTextView);

        monthSpentAmount=findViewById(R.id.monthSpentAmount);
        linearLayoutAnalysis=findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending=findViewById(R.id.monthRatioSpending);
        monthRatioSpending_Image=findViewById(R.id.monthRatioSpending_Image);

        analyticsTransportAmount=findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount=findViewById(R.id.analyticsFoodAmount);
        analyticsHouseExpensesAmount=findViewById(R.id.analyticsHouseExpensesAmount);
        analyticsEntertainmentAmount=findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount=findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount=findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount=findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount=findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalExpensesAmount=findViewById(R.id.analyticsPersonalExpensesAmount);
        analyticsOtherAmount=findViewById(R.id.analyticsOtherAmount);

        // Recycler views
        linearLayoutTransport=findViewById(R.id.linearLayoutTransport);
        linearLayoutFood=findViewById(R.id.linearLayoutFood);
        linearLayoutFoodHouse=findViewById(R.id.linearLayoutFoodHouse);
        linearLayoutEntertainment=findViewById(R.id.linearLayoutEntertainment);
        linearLayoutEducation=findViewById(R.id.linearLayoutEducation);
        linearLayoutCharity=findViewById(R.id.linearLayoutCharity);
        linearLayoutApparel=findViewById(R.id.linearLayoutApparel);
        linearLayoutHealth=findViewById(R.id.linearLayoutHealth);
        linearLayoutPersonalExp=findViewById(R.id.linearLayoutPersonalExp);
        linearLayoutOther=findViewById(R.id.linearLayoutOther);


        progress_ratio_transport=findViewById(R.id.progress_ratio_transport);
        progress_ratio_food=findViewById(R.id.progress_ratio_food);
        progress_ratio_house=findViewById(R.id.progress_ratio_house);
        progress_ratio_cha=findViewById(R.id.progress_ratio_cha);
        progress_ratio_ent=findViewById(R.id.progress_ratio_ent);
        progress_ratio_edu=findViewById(R.id.progress_ratio_edu);
        progress_ratio_app=findViewById(R.id.progress_ratio_app);
        progress_ratio_hes=findViewById(R.id.progress_ratio_hea);
        progress_ratio_per=findViewById(R.id.progress_ratio_per);
        progress_ratio_oth=findViewById(R.id.progress_ratio_oth);



        status_Image_transport =findViewById(R.id.status_Image_transport);
        status_Image_food=findViewById(R.id.status_Image_food);
        status_Image_house=findViewById(R.id.status_Image_house);
        status_Image_ent=findViewById(R.id.status_Image_ent);
        status_Image_edu=findViewById(R.id.status_Image_edu);
        status_Image_cha=findViewById(R.id.status_Image_cha);
        status_Image_app=findViewById(R.id.status_Image_app);
        status_Image_hea=findViewById(R.id.status_Image_hea);
        status_Image_per=findViewById(R.id.status_Image_per);
        status_Image_oth=findViewById(R.id.status_Image_oth);


        anyChartView=findViewById(R.id.anyChartView);


        getTotalWeekTransportExpenses();
        getTotalWeekFoodExpenses();
        getTotalWeekHouseExpenses();
        getTotalWeekEntertainmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekPersonalExpenses();
        getTotalWeekOtherExpenses();
        getTotalDaySpending();

        new java.util.Timer().schedule(new TimerTask() {
                                           @Override
                                           public void run() {
                                               loadGraph();

                                               setStatusAndImageResource();
                                           }
                                       },2000

        );
    }

    private void getTotalDaySpending() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;

                    }
                    totalBudgetAmountTextView.setText("Total day's spending :Rs "+totalAmount);
                    monthSpentAmount.setText("Total amount :Rs "+totalAmount);
                }
                else {
                   totalBudgetAmountTextView.setText("You have not spent today");
                   anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalWeekOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Other"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayOther").setValue(totalAmount);
                }
                else {
                    linearLayoutOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPersonalExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Personal Expenses"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalExpensesAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayPer").setValue(totalAmount);
                }
                else {
                    linearLayoutPersonalExp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Health"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHealthAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayHea").setValue(totalAmount);
                }
                else {
                    linearLayoutHealth.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekApparelExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Apparel and Services"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsApparelAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayApp").setValue(totalAmount);
                }
                else {
                    linearLayoutApparel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekCharityExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Charity"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsCharityAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayChar").setValue(totalAmount);
                }
                else {
                    linearLayoutCharity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEducationExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Education"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEducationAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayEdu").setValue(totalAmount);
                }
                else {
                    linearLayoutEducation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Entertainment"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayEnt").setValue(totalAmount);
                }
                else {
                    linearLayoutEntertainment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "House Expenses"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseExpensesAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayHouse").setValue(totalAmount);
                }
                else {
                    linearLayoutFoodHouse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Food"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayFood").setValue(totalAmount);
                }
                else {
                    linearLayoutFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTotalWeekTransportExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Calendar cal = Calendar.getInstance();
        String date= dateFormat.format(cal.getTime());
        String itemNday = "Transport"+date;

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("dayTrans").setValue(totalAmount);
                }
                else {
                    linearLayoutTransport.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int traTotal;
                    if (snapshot.hasChild("dayTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }
                    int foodTotal;
                    if (snapshot.hasChild("dayFood")) {
                        foodTotal = parseInt(snapshot.child("dayFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    int houseTotal;
                    if (snapshot.hasChild("dayHouse")) {
                        houseTotal = parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }
                    int entTotal;
                    if (snapshot.hasChild("dayEnt")) {
                        entTotal = Integer.parseInt(snapshot.child("dayEnt").getValue().toString());
                    } else {
                        entTotal = 0;
                    }
                    int eduTotal;
                    if (snapshot.hasChild("dayEdu")) {
                        eduTotal = Integer.parseInt(snapshot.child("dayEdu").getValue().toString());
                    } else {
                        eduTotal = 0;
                    }
                    int chaTotal;
                    if (snapshot.hasChild("dayCha")) {
                        chaTotal = Integer.parseInt(snapshot.child("dayCha").getValue().toString());
                    } else {
                        chaTotal = 0;
                    }
                    int appTotal;
                    if (snapshot.hasChild("dayApp")) {
                        appTotal = Integer.parseInt(snapshot.child("dayApp").getValue().toString());
                    } else {
                        appTotal = 0;
                    }
                    int heaTotal;
                    if (snapshot.hasChild("daHea")) {
                        heaTotal = Integer.parseInt(snapshot.child("dayHea").getValue().toString());
                    } else {
                        heaTotal = 0;
                    }
                    int perTotal;
                    if (snapshot.hasChild("dayPer")) {
                        perTotal = Integer.parseInt(snapshot.child("dayPer").getValue().toString());
                    } else {
                        perTotal = 0;
                    }
                    int othTotal;
                    if (snapshot.hasChild("dayOther")) {
                        othTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        othTotal = 0;
                    }
                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport",traTotal));
                    data.add(new ValueDataEntry("House exp",houseTotal));
                    data.add(new ValueDataEntry("Food",foodTotal));
                    data.add(new ValueDataEntry("Entertainment",entTotal));
                    data.add(new ValueDataEntry("Education",eduTotal));
                    data.add(new ValueDataEntry("Charity",chaTotal));
                    data.add(new ValueDataEntry("Apparel",appTotal));
                    data.add(new ValueDataEntry("Health",heaTotal));
                    data.add(new ValueDataEntry("Personal",perTotal));
                    data.add(new ValueDataEntry("other",othTotal));
                    pie.data(data);
                    pie.title("Daily Analytics");
                    pie.labels().position("outside");
                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d,0d,10d,0d);
                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);
                }
                else{
                    Toast.makeText(DailyAnalyticsActivity.this,"Child does not exist",Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this,"Child does not exist",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    float traTotal;
                    if (snapshot.hasChild("dayTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }
                    float foodTotal;
                    if (snapshot.hasChild("dayFood")) {
                        foodTotal = parseInt(snapshot.child("dayFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    float houseTotal;
                    if (snapshot.hasChild("dayHouse")) {
                        houseTotal = parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }
                    float entTotal;
                    if (snapshot.hasChild("dayEnt")) {
                        entTotal = Integer.parseInt(snapshot.child("dayEnt").getValue().toString());
                    } else {
                        entTotal = 0;
                    }
                    float eduTotal;
                    if (snapshot.hasChild("dayEdu")) {
                        eduTotal = Integer.parseInt(snapshot.child("dayEdu").getValue().toString());
                    } else {
                        eduTotal = 0;
                    }
                    float chaTotal;
                    if (snapshot.hasChild("dayCha")) {
                        chaTotal = Integer.parseInt(snapshot.child("dayCha").getValue().toString());
                    } else {
                        chaTotal = 0;
                    }
                    float appTotal;
                    if (snapshot.hasChild("dayApp")) {
                        appTotal = Integer.parseInt(snapshot.child("dayApp").getValue().toString());
                    } else {
                        appTotal = 0;
                    }
                    float heaTotal;
                    if (snapshot.hasChild("dayes")) {
                        heaTotal = Integer.parseInt(snapshot.child("daytea").getValue().toString());
                    } else {
                        heaTotal = 0;
                    }
                    float perTotal;
                    if (snapshot.hasChild("dayPer")) {
                        perTotal = Integer.parseInt(snapshot.child("dayPer").getValue().toString());
                    } else {
                        perTotal = 0;
                    }
                    float othTotal;
                    if (snapshot.hasChild("dayOther")) {
                        othTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        othTotal = 0;
                    }
                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("today")) {
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("today").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;
                    }

                    //Getting Ratios
                    float traRatio;
                    if (snapshot.hasChild("dayTransRatio")) {
                        traRatio = Integer.parseInt(snapshot.child("dayTransRatio").getValue().toString());
                    } else {
                        traRatio = 0;
                    }
                    float foodRatio;
                    if (snapshot.hasChild("dayFoodRatio")) {
                        foodRatio = Integer.parseInt(snapshot.child("dayFoodRatio").getValue().toString());
                    } else {
                        foodRatio = 0;
                    }
                    float houseRatio;
                    if (snapshot.hasChild("dayHouseRatio")) {
                        houseRatio = Integer.parseInt(snapshot.child("dayHouseRatio").getValue().toString());
                    } else {
                        houseRatio = 0;
                    }
                    float entRatio;
                    if (snapshot.hasChild("dayEntRatio")) {
                        entRatio = Integer.parseInt(snapshot.child("dayEntRatio").getValue().toString());
                    } else {
                        entRatio = 0;
                    }
                    float eduRatio;
                    if (snapshot.hasChild("dayEduRatio")) {
                        eduRatio = Integer.parseInt(snapshot.child("dayEduRatio").getValue().toString());
                    } else {
                        eduRatio = 0;
                    }
                    float chaRatio;
                    if (snapshot.hasChild("dayCharRatio")) {
                        chaRatio = Integer.parseInt(snapshot.child("dayCharRatio").getValue().toString());
                    } else {
                        chaRatio = 0;
                    }
                    float appRatio;
                    if (snapshot.hasChild("dayAppRatio")) {
                        appRatio = Integer.parseInt(snapshot.child("dayAppRatio").getValue().toString());
                    } else {
                        appRatio = 0;
                    }
                    float heaRatio;
                    if (snapshot.hasChild("dayHealthRatio")) {
                        heaRatio = Integer.parseInt(snapshot.child("dayHealthRatio").getValue().toString());
                    } else {
                        heaRatio = 0;
                    }
                    float perRatio;
                    if (snapshot.hasChild("dayPerRatio")) {
                        perRatio = Integer.parseInt(snapshot.child("dayPerRatio").getValue().toString());
                    } else {
                        perRatio = 0;
                    }
                    float othRatio;
                    if (snapshot.hasChild("dayOtherRatio")){
                        othRatio = Integer.parseInt(snapshot.child("dayOtherRatio").getValue().toString());
                    }else{
                        othRatio=0;
                    }

                    float monthTotalSpentAmountRatio;
                    if(snapshot.hasChild("dailyBudget")) {
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("dailyBudget").getValue().toString());
                    }else{
                        monthTotalSpentAmountRatio=0;
                    }

                    float monthPercent=(monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if(monthPercent<50) {
                        monthRatioSpending.setText(monthPercent + " %" + " used of" + monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.red);
                    }else if(monthPercent>50 && monthPercent<100){
                        monthRatioSpending.setText(monthPercent+" %"+"used of"+monthTotalSpentAmountRatio+". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.blue);
                    }
                    else{
                        monthRatioSpending.setText(monthPercent+" %"+"used of"+monthTotalSpentAmountRatio+". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.green);
                    }
                        float transportPercent=(traTotal/traRatio)*100;
                        if(transportPercent<50) {
                            progress_ratio_transport.setText(transportPercent + " %" + "used of" + traRatio + ". Status:");
                            status_Image_transport.setImageResource(R.drawable.red);
                        }else if(transportPercent>50 && transportPercent<100){
                                progress_ratio_transport.setText(transportPercent + " %" + "used of" + traRatio + ". Status:");
                            status_Image_transport.setImageResource(R.drawable.blue);
                        }
                        else {
                            progress_ratio_transport.setText(transportPercent + " %" + "used of" + traRatio + ". Status:");
                            status_Image_transport.setImageResource(R.drawable.green);
                        }

                    float foodPercent=(foodTotal/foodRatio)*100;
                    if(foodPercent<50) {
                        progress_ratio_transport.setText(foodPercent + " % " + "used of" + foodRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(foodPercent>50 && foodPercent<100){
                        progress_ratio_transport.setText(foodPercent + " %" + "used of" + foodRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(foodPercent + " %" + "used of" + foodRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }

                    float housePercent=(houseTotal/houseRatio)*100;
                    if(housePercent<50) {
                        progress_ratio_transport.setText(housePercent + " %" + "used of" + houseRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(housePercent>50 && housePercent<100){
                        progress_ratio_transport.setText(housePercent+ " %" + "used of" + houseRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(housePercent + " %" + "used of" + houseRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float entPercent=(entTotal/entRatio)*100;
                    if(entPercent<50) {
                        progress_ratio_transport.setText(entPercent + " %" + "used of" + entRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(entPercent>50 && entPercent<100){
                        progress_ratio_transport.setText(entPercent + " %" + "used of" + entRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(entPercent + " %" + "used of" + entRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float eduPercent=(eduTotal/eduRatio)*100;
                    if(eduPercent<50) {
                        progress_ratio_transport.setText(eduPercent + " %" + "used of" + eduRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(eduPercent>50 && eduPercent<100){
                        progress_ratio_transport.setText(eduPercent + " %" + "used of" + eduRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(eduPercent + " %" + "used of" + eduRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float chaPercent=(chaTotal/chaRatio)*100;
                    if(chaPercent<50) {
                        progress_ratio_transport.setText(chaPercent + " %" + "used of" + chaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(chaPercent>50 && chaPercent<100){
                        progress_ratio_transport.setText(chaPercent + " %" + "used of" + chaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(chaPercent + " %" + "used of" + chaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float appPercent=(appTotal/appRatio)*100;
                    if(appPercent<50) {
                        progress_ratio_transport.setText(appPercent + " %" + "used of" + appRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(appPercent>50 && appPercent<100){
                        progress_ratio_transport.setText(appPercent + " %" + "used of" + appRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(appPercent + " %" + "used of" + appRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float heaPercent=(heaTotal/heaRatio)*100;
                    if(heaPercent<50) {
                        progress_ratio_transport.setText(heaPercent + " %" + "used of" + heaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(heaPercent>50 && heaPercent<100){
                        progress_ratio_transport.setText(heaPercent + " %" + "used of" + heaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(heaPercent + " %" + "used of" + heaRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float perPercent=(perTotal/perRatio)*100;
                    if(perPercent<50) {
                        progress_ratio_transport.setText(perPercent + " %" + "used of" + perRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(perPercent>50 && perPercent<100){
                        progress_ratio_transport.setText(perPercent + " %" + "used of" + perRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(perPercent + " %" + "used of" + perRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }
                    float otherPercent=(othTotal/othRatio)*100;
                    if(otherPercent<50) {
                        progress_ratio_transport.setText(otherPercent + " %" + "used of" + othRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }else if(otherPercent>50 && otherPercent<100){
                        progress_ratio_transport.setText(otherPercent + " %" + "used of" + othRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.blue);
                    }
                    else {
                        progress_ratio_transport.setText(otherPercent + " %" + "used of" + othRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }

                }
                else{
                    Toast.makeText(DailyAnalyticsActivity.this,"setStatusAndImageResource Errors",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}