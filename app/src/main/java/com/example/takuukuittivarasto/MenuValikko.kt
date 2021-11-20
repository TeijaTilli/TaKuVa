package com.example.takuukuittivarasto

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.takuukuittivarasto.R

class MenuValikko(var inflater: MenuInflater, var navController: NavController, var lisaaKuittiToiminto: Int, var fragment: Fragment?, var activity: AppCompatActivity?) {

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        inflater!!.inflate(R.menu.menu, menu)
        return true
    }

   fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.btnBarLisaaKuitti) {
            navController.navigate(lisaaKuittiToiminto)
            return true
        }
        if (id == R.id.btnBarTeema) {

            return true
        }
        if(fragment == null){
            return activity!!.onOptionsItemSelected(item)
        }
       else{
           return fragment!!.onOptionsItemSelected(item)
        }
   }
}