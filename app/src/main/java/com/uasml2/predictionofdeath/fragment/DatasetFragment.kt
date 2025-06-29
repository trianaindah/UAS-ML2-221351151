package com.uasml2.predictionofdeath.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.uasml2.predictionofdeath.R
import java.io.*

class DatasetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dataset, container, false)
        val tableLayout = view.findViewById<TableLayout>(R.id.datasetTable)

        try {
            val reader = BufferedReader(
                InputStreamReader(requireContext().assets.open("kematian_dataset.csv"))
            )
            val lines = reader.readLines()

            val maxRows = 20 // batasi agar ringan
            lines.take(maxRows).forEachIndexed { index, line ->
                val tableRow = TableRow(requireContext())
                val cells = line.split(",")

                cells.forEach { cellText ->
                    val textView = TextView(requireContext()).apply {
                        text = cellText
                        setPadding(16, 12, 16, 12)
                        textSize = if (index == 0) 16f else 14f
                        setTextColor(resources.getColor(
                            if (index == 0) android.R.color.black else android.R.color.darker_gray,
                            null
                        ))
                        setBackgroundResource(R.drawable.cell_border)
                        if (index == 0) {
                            setTypeface(null, Typeface.BOLD)
                            setBackgroundColor(resources.getColor(android.R.color.holo_blue_light, null))
                        }
                    }
                    tableRow.addView(textView)
                }

                tableLayout.addView(tableRow)
            }

        } catch (e: Exception) {
            val errorRow = TableRow(requireContext())
            val errorText = TextView(requireContext()).apply {
                text = "Gagal memuat dataset: ${e.message}"
                setPadding(16, 16, 16, 16)
            }
            errorRow.addView(errorText)
            tableLayout.addView(errorRow)
        }

        return view
    }
}
