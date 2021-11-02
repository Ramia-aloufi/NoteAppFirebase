package com.example.noteappfirebase1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    lateinit var btn:Button
    lateinit var et1:EditText
    lateinit var rv:RecyclerView
    private val vm by lazy{ViewModelProvider(this).get(VM::class.java)}
    var TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        et1 = findViewById(R.id.editTextTextPersonName)
        rv =  findViewById(R.id.rv)
        updaterv()
        btn.setOnClickListener {
            var note = et1.text.toString()
            if(note.isNotEmpty()) {
                vm.addNote(Note(null,note))
                et1.text.clear()
                it.hideKeyboard()
            }else{
                Toast.makeText(this,"Write a note",Toast.LENGTH_SHORT).show()
            }

}
    }



    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun updaterv(){
        vm.retrive().observe(this) {
                note ->
            rv.adapter = MyAdapter(note,this)
            rv.layoutManager = LinearLayoutManager(this)

        }

    }

    fun preUpdate(item:Note) {
        var txtt = EditText(this)
        txtt.setText(item.note)
        AlertDialog.Builder(this)
            .setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                vm.update(txtt.text.toString(), item)
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
            .setTitle("Update Note")
            .setView(txtt)
            .create()
            .show()
    }

    fun preDelete(item:Note){
        AlertDialog.Builder(this)
            .setPositiveButton("delete", DialogInterface.OnClickListener{
                    _,_ -> vm.delete(item)

            })
            .setNegativeButton("No", DialogInterface.OnClickListener{
                    dialog,_ -> dialog.cancel()
            })
            .setTitle("Delete Note?")
            .create()
            .show()
    }


}







