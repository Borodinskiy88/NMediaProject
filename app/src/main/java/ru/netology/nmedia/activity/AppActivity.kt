package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.viewmodel.AuthViewModel

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                .navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val authViewModel by viewModels<AuthViewModel>()

        var previousMenuProvider: MenuProvider? = null
        authViewModel.data.observe(this) {
            previousMenuProvider?.let(::removeMenuProvider)
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_auth, menu)
                    menu.setGroupVisible(R.id.unauthorized, !authViewModel.authorized)
                    menu.setGroupVisible(R.id.authorized, authViewModel.authorized)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.login -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_loginFragment)
                            true
                        }
                        R.id.registration -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_registrationFragment)
                            true
                        }
                        R.id.logout -> {
                            MaterialAlertDialogBuilder(this@AppActivity)
                                .setTitle(R.string.logout)
                                .setMessage(R.string.log_out_of_your_account)
                                .setNegativeButton(R.string.no) { dialog, _ ->
                                    dialog.cancel()
                                }
                                .setPositiveButton(R.string.yes) { _, _ ->
                                    AppAuth.getInstance().removeAuth()
                                    Toast.makeText(
                                        this@AppActivity,
                                        R.string.logged_out_of_your_account,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                .show()
                            true
                        }
                        else -> false
                    }
            }.also { previousMenuProvider = it })
        }

        checkGoogleApiAvailability()
    }

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}