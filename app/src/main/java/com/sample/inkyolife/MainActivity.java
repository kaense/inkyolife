package com.sample.inkyolife;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    EditText edit_age, edit_saving, edit_payment;
    TextView kekka, kekka_nokori;
    float die = 0, left_years = 0, left_months = 0, months = 0,payment = 0,saving = 0,age = 0;
    static protected SharedPreferences sharedPreferences;
    static protected SharedPreferences.Editor editor;
    static protected float key_age, key_saving, key_payment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kekka = (TextView) findViewById(R.id.kekka);
        kekka_nokori = (TextView) findViewById(R.id.kekka_nokori);
        //bikou = (TextView) findViewById(R.id.bikou);

        edit_age = (EditText) findViewById(R.id.age);
        edit_saving = (EditText) findViewById(R.id.saving);
        edit_payment = (EditText) findViewById(R.id.payment);

        edit_age.addTextChangedListener(this);
        edit_saving.addTextChangedListener(this);
        edit_payment.addTextChangedListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        editor.apply();
        key_age = sharedPreferences.getFloat("key_age", 37f);
        key_saving = sharedPreferences.getFloat("key_saving", 500f);
        key_payment = sharedPreferences.getFloat("key_payment", 14f);

        if (key_age == (int) key_age) {
            edit_age.setText(String.format("%d", (int) key_age));
        } else {
            edit_age.setText(String.format("%s", key_age));
        }
        if (key_saving == (int) key_saving) {
            edit_saving.setText(String.format("%d", (int) key_saving));
        } else {
            edit_saving.setText(String.format("%s", key_saving));
        }
        if (key_payment == (int) key_payment) {
            edit_payment.setText(String.format("%d", (int) key_payment));
        } else {
            edit_payment.setText(String.format("%s", key_payment));
        }


        MobileAds.initialize(getApplicationContext(), "");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        keisan();
        //createLineChart();
    }

    public void keisan() {
        try {

            SpannableStringBuilder paymentText = (SpannableStringBuilder) this.edit_payment.getText();
            payment = Float.parseFloat(paymentText.toString());
            SpannableStringBuilder savingText = (SpannableStringBuilder) this.edit_saving.getText();
            saving = Float.parseFloat(savingText.toString());
            SpannableStringBuilder ageText = (SpannableStringBuilder) this.edit_age.getText();
            age = Float.parseFloat(ageText.toString());

            editor.putFloat("key_age", age);
            editor.putFloat("key_saving", saving);
            editor.putFloat("key_payment", payment);
            editor.apply();

            if (!(payment == 0)) {
                months = saving / payment;
                left_months = months % 12;
                left_years = months / 12;
                die = age + left_years;
                kekka.setText(String.valueOf((int) die));
                kekka_nokori.setText("(残り" + (int) left_years + "年" + (int) left_months + "か月)");
            } else {
                kekka.setText("∞");
                kekka_nokori.setText("残り∞年∞か月");
                //kekka.setBackgroundResource(R.color.yellow);
            }
            if (die >= 80) {
                //kekka.setBackgroundResource(R.color.sakura);
            } else if (die >= 65) {
                //kekka.setBackgroundResource(R.color.green);
            } else if (die > 0) {
                //kekka.setBackgroundResource(R.color.white);
            }
            //bikou.setText();
        } catch (Exception e) {
            kekka.setText("----");
            kekka_nokori.setText("残り--年--か月");
            //kekka.setBackgroundResource(R.color.colorPrimary);
        }
    }

/*
    private void createLineChart() {
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
        lineChart.setDescription("グラフ");

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.setDrawGridBackground(true);
        //lineChart.setDrawLineShadow(false);
        lineChart.setEnabled(true);

        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDoubleTapToZoomEnabled(true);

        lineChart.setHighlightPerDragEnabled(true);
        //lineChart.setDrawHighlightArrow(true);
        lineChart.setHighlightPerTapEnabled(true);

        lineChart.setScaleEnabled(true);

        lineChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        lineChart.setData(createLineChartData());

        lineChart.invalidate();
        // アニメーション
        //lineChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    // LineChartの設定
    private LineData createLineChartData() {
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        //int age1,age2,age3,age4;
        //age1 = (int)(age+(die-age)*1/5);
        // X軸
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add(String.valueOf((int)age));

        ArrayList<Entry> valuesA = new ArrayList<>();
        valuesA.add(new Entry(saving,0));

        int ten_age = ((int)age/10)*10;
        int count = 0;
        for(int i=1;(ten_age+10)<die;i++){
            count++;
            ten_age = ten_age + 10;
            xValues.add(String.valueOf(ten_age));
            valuesA.add(new Entry((int)(saving-((float)ten_age-age)*payment*12),i));
        }
        xValues.add(String.valueOf((int)die));

        //int saving1,saving2,saving3,saving4;
        //saving1 = (int)(saving-((float)ten_age-age)*payment*12);
        // valueA

        //valuesA.add(new Entry(saving/5*4, 1));

        valuesA.add(new Entry(0,count+1));


        LineDataSet valuesADataSet = new LineDataSet(valuesA,"aa");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        lineDataSets.add(valuesADataSet);

        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }
    */
}

            /*
            float die = 0, pension_add = 0, syushi = 0, life_add = 0, salary_add = 0;

            syushi = salary - payment;//毎月の収支
            life_add = -saving / syushi;//何か月生きられるか
            die = age + life_add / 12;//何歳で死ぬ
            pension_add = (old - age) * 12;//年金まであと何か月
            salary_add = (retire - age) * 12;//給料なくなるまであと何か月

            if (retire >= old && die >= old) {//年金が先
                saving = saving + pension_add * syushi;//年金受給開始時点での貯金 表示したい
                syushi = salary + pension - payment;
                life_add = -saving / syushi;
                die = old + life_add / 12;//年金開始時+何か月
                if (die >= retire) {
                    saving = saving + (retire-old) * syushi;
                    syushi = -payment;
                    life_add = -saving / syushi;
                    die = retire + life_add / 12;
                }
            } else if (retire < old && die >= retire) {//収支止まるのが先
                saving = saving + salary_add * syushi;//収支止まる時点での貯金
                syushi = -payment;
                life_add = -saving / syushi;
                die = retire + life_add / 12;
                if (die >= old) {
                    saving = saving + (old-retire) * syushi;//年金受給開始時点での貯金 表示したいritaia
                    syushi = salary + pension - payment;
                    life_add = -saving / syushi;
                    die = old + life_add / 12;//年金開始時+何か月
                }
            }*/
/*
            if (die > old && age < old) {
                saving = saving + pension_add * syushi;//年金受給開始時点での貯金 表示したい
                syushi = salary + pension - payment;
                life_add = -saving / syushi;
                die = old + life_add / 12;//年金開始時+何か月
            } else if (die > old && age >= old) {
                syushi = salary + pension - payment;
                life_add = -saving / syushi;
                die = age + life_add / 12;//年金開始時+何か月
            }

            if (die > retire) {
                saving = saving + (retire - age) * 12 * syushi;
                syushi = -payment;
                die = -saving / syushi + retire;
            }

            if (die > old && age < old) {
                saving = saving + pension_add * syushi;//年金受給開始時点での貯金 表示したい
                syushi = salary + pension - payment;
                life_add = -saving / syushi;
                die = old + life_add / 12;//年金開始時+何か月
            } else if (die > old && age >= old) {
                syushi = salary + pension - payment;
                life_add = -saving / syushi;
                die = age + life_add / 12;//年金開始時+何か月
            }
*/