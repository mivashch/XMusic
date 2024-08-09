package com.example.xmusic

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.gson.Gson
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.json.JSONObject
import java.io.File
import androidx.recyclerview.widget.RecyclerView


class LibFragment : Fragment(R.layout.fragment_lib) {
    private lateinit var authService: AuthorizationService

    private val authResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            writeAuthToJSON(data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout = view.findViewById<LinearLayout>(R.id.horizontalLayout)
        val plus = view.findViewById<ImageView>(R.id.plusIcon)
        authService = AuthorizationService(requireContext())
        val filePath = File("/data/data/com.example.xmusic/files/chaquopy/AssetFinder/app/res/data/ytdata.json")


        val jsonString: String? = try {
            filePath.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (jsonString != null) {
            val gson = Gson()
            val library: Library = gson.fromJson(jsonString, Library::class.java)
            val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = PlaylistAdapter(library.playlists) { playlist ->
                Toast.makeText(requireContext(), playlist.title, Toast.LENGTH_SHORT).show()
            }

        }
            plus.setOnClickListener {
            showDialogWindow()
        }
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }

    }

    private fun showDialogWindow() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        val username = dialogView.findViewById<EditText>(R.id.username)
        val password = dialogView.findViewById<EditText>(R.id.password)
        val cancelButton = dialogView.findViewById<Button>(R.id.CancelButton)
        val logButton = dialogView.findViewById<Button>(R.id.LogButton)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        logButton.setOnClickListener {
            startAuthProcess()
//            callPythonScript()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startAuthProcess() {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.google.com/o/oauth2/auth"), // Authorization endpoint
            Uri.parse("https://oauth2.googleapis.com/token") // Token endpoint
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            "65351908139-j5m64a682skgum08bqtttl5696qmg357.apps.googleusercontent.com",
            ResponseTypeValues.CODE,
            Uri.parse("com.example.xmusic:/oauth2redirect")
        ).setScope("https://www.googleapis.com/auth/youtube").build()

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authResultLauncher.launch(authIntent)
    }

    private fun writeAuthToJSON(data: Intent?) {
        val response = AuthorizationResponse.fromIntent(data!!)
        val ex = AuthorizationException.fromIntent(data)
        if (response != null) {
            val tokenRequest = response.createTokenExchangeRequest()
            authService.performTokenRequest(tokenRequest) { tokenResponse, tokenEx ->
                if (tokenResponse != null) {
                    val accessToken = tokenResponse.accessToken
                    val refreshToken = tokenResponse.refreshToken
                    val expiresIn = tokenResponse.accessTokenExpirationTime

                    if (expiresIn != null) {
                        // Створіть JSON-об'єкт з токенами
                        val oauthData = JSONObject()
                        oauthData.put("scope", "https://www.googleapis.com/auth/youtube")
                        oauthData.put("token_type", "Bearer")
                        oauthData.put("access_token", accessToken)
                        oauthData.put("refresh_token", refreshToken)
                        oauthData.put("expires_at", expiresIn)
                        oauthData.put("expires_in", (expiresIn - System.currentTimeMillis()) / 1000)

                        // Збережіть JSON-об'єкт у файл
                        val file = File(requireContext().filesDir, "oauth.json")
                        file.writeText(oauthData.toString())
                        callPythonScript()
                    } else {
                        // Обробка випадку, коли expiresIn є null
                        Toast.makeText(requireContext(), "Помилка: expiresIn є null", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun callPythonScript() {
        val python = Python.getInstance()
        val pythonFile = python.getModule("YTauth")
        val func = pythonFile.get("Ytauth")
        val oauthPath = File(requireContext().filesDir, "oauth.json").absolutePath
        if (func != null) {
            func.call(oauthPath)
        }
//        val oauthPath = File(requireContext().filesDir, "oauth.json").absolutePath
//        pythonFile.callAttr("fetch_and_store_data", oauthPath)

    }

    companion object {
        private const val AUTH_REQUEST_CODE = 1001
    }
}
