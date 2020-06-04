package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

//defining value that will be use in activity cycle functions
private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_NUMBER1 = "number1"
private const val STATE_NUMBER1_STORED = "number1_Stored"

class MainActivity : AppCompatActivity() {

    //variables to hold the operation and type of calculation
    private var number1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating a common OnClickListener for buttons(0-9,.)
        val listner = View.OnClickListener { v ->
            val b = v as Button
            new_number.append(b.text)
        }

        //defining the buttons so that it can work,passing the listner that i created above
        button0.setOnClickListener(listner)
        button1.setOnClickListener(listner)
        button2.setOnClickListener(listner)
        button3.setOnClickListener(listner)
        button4.setOnClickListener(listner)
        button5.setOnClickListener(listner)
        button6.setOnClickListener(listner)
        button7.setOnClickListener(listner)
        button8.setOnClickListener(listner)
        button9.setOnClickListener(listner)
        buttonpoint.setOnClickListener(listner)

        //creating a common OnClickListener for buttons(+,-,*,/,%)
        val opListner = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            //adding try and catch statement for avoiding number format exception
            try {
                val value = new_number.text.toString().toDouble()
                performOperation(value,op)  //a function that will perform the operations , passing the parameters
            } catch (e: NumberFormatException){
                new_number.setText("")
            }
            pendingOperation = op
            operation_performing.text = pendingOperation

        }

        //defining the buttons so that it can work,passing the listner that i created above
        buttonequal.setOnClickListener(opListner)
        buttonplus.setOnClickListener(opListner)
        buttonminus.setOnClickListener(opListner)
        buttonmultiply.setOnClickListener(opListner)
        buttondivide.setOnClickListener(opListner)
        buttonpercent.setOnClickListener(opListner)

        //created a single setOnClickListener for negetive button only
        buttonneg.setOnClickListener { v: View? ->
            val value = new_number.text.toString()
            if(value.isEmpty()){
                new_number.setText("-")
            }
            else{
                //adding try and catch statement for avoiding number format exception
                try{
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    new_number.setText(doubleValue.toString())
                }catch (e:java.lang.NumberFormatException){
                    //new number was already "-" or "."
                    new_number.setText("")
                }
            }
        }

        //created a single setOnClickListener for back button only
        buttonback.setOnClickListener { v: View? ->
            new_number.text = ""
        }

        //created a single setOnClickListener for all clear button only
        buttonclear.setOnClickListener { v: View? ->
            new_number.text = ""
            operation_performing.text = ""
            result.text = ""
            number1 = null
            pendingOperation = ""
        }

    }

    //a function to perform the operations
    private fun performOperation(value:Double, operation: String){
        //in this function i am using double bang operator(!!) to avoid null pointer exception
        //also using lambdas to perform operations
        if(number1 == null){
            number1 = value
        }
        else{
            if (pendingOperation == "="){
                pendingOperation = operation
            }

            when(pendingOperation) {
                "=" -> number1 = value
                "/" -> number1 = if (value == 0.0) {
                    Double.NaN  // if we are dividing with 0
                } else{
                    number1!! / value
                }
                "*" -> number1 = number1!! * value
                "+" -> number1 = number1!! + value
                "-" -> number1 = number1!! - value
                "%" -> number1 = number1!! / 100
            }
        }
        result.setText(number1.toString())
        new_number.setText("")
    }

    //adding activity lifecycle functions to run the app efficiently
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(number1 != null){
            outState.putDouble(STATE_NUMBER1,number1!!)
            outState.putBoolean(STATE_NUMBER1_STORED,true)
        }
        outState.putString(STATE_PENDING_OPERATION,pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number1 = if(savedInstanceState.getBoolean(STATE_NUMBER1_STORED,false)){
            savedInstanceState.getDouble(STATE_NUMBER1)
        } else{
            null
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION,"")
        operation_performing.text = pendingOperation
    }
}