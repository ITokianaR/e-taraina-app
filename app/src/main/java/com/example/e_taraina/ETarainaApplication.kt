package com.example.e_taraina

import android.app.Application
import com.example.e_taraina.data.ComplaintStore
import com.example.e_taraina.data.local.AppDatabase

// point d'entrée pour construire la base Room une seule fois au
// démarrage de l'app, avec le vrai contexte applicatif
class ETarainaApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        ComplaintStore.init(this)
    }
}
