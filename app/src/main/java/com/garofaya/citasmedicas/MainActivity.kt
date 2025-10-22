package com.garofaya.citasmedicas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.garofaya.citasmedicas.databinding.ActivityMainBinding
import com.garofaya.citasmedicas.db.LocalDataBase

class MainActivity : AppCompatActivity() {

    private lateinit var txtNombreUsuario: TextView
    private lateinit var txtDNIUsuario: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del Toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Configuración del FAB
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab)
                .show()
        }

        // Configuración del Navigation Drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_citas,
                R.id.nav_editarusuario
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Acceso al header del Navigation Drawer
        val headerView: View = navView.getHeaderView(0)
        txtNombreUsuario = headerView.findViewById(R.id.txtNombreUsuario)
        txtDNIUsuario = headerView.findViewById(R.id.txtDNIUsuario)

        // Obtener datos desde el Intent (enviados desde LoginActivity)
        val nombreIntent = intent.getStringExtra("nombreUsuario")
        val dniIntent = intent.getStringExtra("dniUsuario")

        if (nombreIntent != null && dniIntent != null) {
            // Si vienen datos del login, los mostramos directamente
            txtNombreUsuario.text = nombreIntent
            txtDNIUsuario.text = dniIntent
        } else {
            // Si no vienen (usuario ya guardado en base local), los obtenemos de la BD
            obtenerDatos()
        }
    }

    private fun obtenerDatos() {
        val localBase = LocalDataBase.getInstance(this)
        val usuarioDao = localBase.usuarioDao()
        val usuario = usuarioDao.listar()
        if (usuario != null) {
            txtNombreUsuario.text = usuario.name
            txtDNIUsuario.text = usuario.dni
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_cerrarsesion -> {
                cerrarSesion()
                irLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cerrarSesion() {
        val localBase = LocalDataBase.getInstance(this)
        val usuarioDao = localBase.usuarioDao()
        val usuario = usuarioDao.listar()
        if (usuario != null) {
            usuarioDao.eliminar(usuario)
        }
    }

    private fun irLogin() {
        val intentLogin = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
