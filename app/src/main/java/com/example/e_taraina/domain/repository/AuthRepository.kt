package com.example.e_taraina.domain.repository

import com.example.e_taraina.domain.models.User

// contrat côté domain, le reste de l'app dépend de ça et pas de
// l'implémentation concrète, comme ça on peut la changer sans tout casser
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}
