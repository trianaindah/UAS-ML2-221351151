package com.uasml2.predictionofdeath.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.uasml2.predictionofdeath.R
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SimulationFragment : Fragment() {
    private lateinit var interpreter: Interpreter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_simulation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = FileUtil.loadMappedFile(requireContext(), "kematian_predict.tflite")
        interpreter = Interpreter(model)

        val btnPredict = view.findViewById<Button>(R.id.btnPredict)
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val resultText = view.findViewById<TextView>(R.id.textResult)

        btnPredict.setOnClickListener {
            val input = FloatArray(5)
            input[0] = getInputValue(view, R.id.inputAge)
            input[1] = getInputValue(view, R.id.inputSystolic)
            input[2] = getInputValue(view, R.id.inputCholesterol)
            input[3] = getInputValue(view, R.id.inputHemoglobin)
            input[4] = getInputValue(view, R.id.inputSerum)

            val inputBuffer = ByteBuffer.allocateDirect(4 * 5).order(ByteOrder.nativeOrder())
            input.forEach { inputBuffer.putFloat(it) }

            val outputBuffer = ByteBuffer.allocateDirect(4 * 32).order(ByteOrder.nativeOrder())
            interpreter.run(inputBuffer, outputBuffer)
            outputBuffer.rewind()

            val output = FloatArray(32)
            outputBuffer.asFloatBuffer().get(output)
            val result = if (output[0] > output[1]) "RENDAH" else "TINGGI"

            resultText.text = "Risiko Kematian: $result"

            val entries = output.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }

            val dataSet = BarDataSet(entries, "Output Probabilitas")
            dataSet.color = resources.getColor(R.color.teal_700, requireContext().theme)

            val barData = BarData(dataSet)
            barData.barWidth = 0.9f

            barChart.data = barData
            barChart.setFitBars(true)
            barChart.description = Description().apply { text = "Distribusi Output Model" }
            barChart.animateY(1000)
            barChart.invalidate()
        }
    }

    private fun getInputValue(view: View, id: Int): Float {
        val inputText = view.findViewById<EditText>(id).text.toString()
        return if (inputText.isNotEmpty()) inputText.toFloat() else 0f
    }
}
