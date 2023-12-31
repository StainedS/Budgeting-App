package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_monthly_analytics);


        settingsToolbar=findViewById(R.id.my_Feed_Toolbar);
        setSupportActionBar(settingsToolbar);//support action barr
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Monthly Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId=mAuth.getCurrentUser().getUid();
        expensesRef= FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef=FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

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
    }
}