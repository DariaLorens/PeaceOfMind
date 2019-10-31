package ru.lorens.peaceofmind

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.lorens.peaceofmind.rest.RestClient
import java.util.*


class MainActivity : AppCompatActivity() {

    private val date = Date().time
    private val days = (date - Consts.firstDay) / 86400000
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pieChart = findViewById<PieChart>(R.id.chart1)

        getData()

        addButton.setOnClickListener {
            showDialog(date)
//            val now = SharedPreference(this).getValueInt(DAYS)
//            SharedPreference(this).save(DAYS, now + 1)
//            Toast.makeText(this, "Day added. Now ${now + 1} days", Toast.LENGTH_SHORT).show()
//            invalidateDate(days, pieChart)
        }
    }


    private fun getData() {
//        val withDays = SharedPreference(this).getValueInt(DAYS).toFloat()
        GlobalScope.launch {
            val daysList = async {
                try {
                    RestClient.getClient.getDates().date
                } catch (e: Throwable) {
                    print(e)
                    null
                }
            }.await()

            if (daysList != null) {
                runOnUiThread {
                    val pieDataSet = PieDataSet(
                        listOf(
                            PieEntry((daysList.size + 1).toFloat(), "with"),
                            PieEntry(days.toFloat() - (daysList.size + 1).toFloat(), "without")
                        ), "Inducesmile"
                    )
                    invalidateDate(days, pieChart, pieDataSet)
                }
            }
        }
    }

    private fun setDate(date: Long) {
        GlobalScope.launch {
            try {
                val lastId = RestClient.getClient.getDates().date.last().id
                RestClient.getClient.setDate(
                    ru.lorens.peaceofmind.rest.Date(
                        id = lastId + 1,
                        date = date,
                        desc = null
                    )
                )
                runOnUiThread {
                    getData()
                }
            } catch (e: Throwable) {
                print(e)
            }
        }
    }

    private fun invalidateDate(days: Long, pieChart: PieChart, pieDataSet: PieDataSet) {
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieDataSet.setColors(
            Color.parseColor("#0892A5"),
            Color.parseColor("#06908F")
        )

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueTextSize(13f)
        pieData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        pieChart.animateXY(2000, 2000)
        pieChart.centerText = generateCenterSpannableText(days)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.holeRadius = 60f
        pieChart.setDrawCenterText(true)
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTypeface(
            Typeface.createFromAsset(
                assets,
                "OpenSans-Regular.ttf"
            )
        )
        pieChart.setEntryLabelTextSize(17f)
        pieChart.invalidate()
        pieChart.visibility = View.VISIBLE
    }

    private fun showDialog(date: Long) {
        // Create custom dialog object
        val dialog = Dialog(this, R.style.Dialog)
        // Include dialog.xml file
        dialog.setContentView(R.layout.add_dialog)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        val wlp = dialog.window!!.attributes
        wlp.gravity = Gravity.CENTER
        dialog.window!!.attributes = wlp
        val buttonYes = dialog.findViewById<View>(R.id.buttonYes) as Button
        val buttonNo = dialog.findViewById<View>(R.id.buttonNo) as Button
        buttonYes.setOnClickListener {
            setDate(date)
            dialog.dismiss()
        }
        buttonNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}

private fun generateCenterSpannableText(days: Long): SpannableString {
    val s = SpannableString("Days of all\n${days.toInt()}")
    s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
    s.setSpan(ForegroundColorSpan(Color.parseColor("#ffffff")), s.length - 14, s.length, 0)
    return s
}
