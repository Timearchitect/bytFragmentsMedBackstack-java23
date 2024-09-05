package se.gritacademy.bytfragmentsmedbackstack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var fc: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) // findViewbyId efter denna
        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this

// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.menu,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

        fc = findViewById(R.id.fragmentContainerView)
        // val fm: FragmentManager = supportFragmentManager
        // var bf2:BlankFragment2 = BlankFragment2()
        //val fragmentManager = supportFragmentManager
        Toast.makeText(
            baseContext,
            "amount of fragments now: " + supportFragmentManager.backStackEntryCount,
            Toast.LENGTH_SHORT
        ).show()

        findViewById<Button>(R.id.button).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, BlankFragment2::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("1")  //regar att det är en sida att gå tillbaka ifrån
                .commit()
            supportFragmentManager.executePendingTransactions() // execute pending fragments
            Toast.makeText(
                baseContext,
                "amount of fragments now: " + supportFragmentManager.backStackEntryCount,
                Toast.LENGTH_SHORT
            ).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {
        Log.i("alrik", "onBackPressed: BACK!!!")
        // return
        if (supportFragmentManager.backStackEntryCount == 0) {
            Toast.makeText(
                baseContext,
                "cant go back anymore, select \"Quit\" in the spinner to exit ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        super.onBackPressed()
        Toast.makeText(
            baseContext,
            "amount of fragments now: " + supportFragmentManager.backStackEntryCount,
            Toast.LENGTH_SHORT
        ).show()

    }

    fun <T : Fragment> changeFragment(fragClass: Class<T>) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragClass, null)
            .setReorderingAllowed(true)
            .addToBackStack("1")  //regar att det är en sida att gå tillbaka ifrån
            .commit()
        supportFragmentManager.executePendingTransactions() // execute pending fragments
        Toast.makeText(
            baseContext,
            "amount of fragments now: " + supportFragmentManager.backStackEntryCount,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> changeFragment(BlankFragment::class.java)

            1 -> changeFragment(BlankFragment2::class.java)

            2 -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                moveTaskToBack(true);
                exitProcess(0)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}