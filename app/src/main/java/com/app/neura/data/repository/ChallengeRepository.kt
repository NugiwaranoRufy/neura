package com.app.neura.data.repository

import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeType

class ChallengeRepository {

    fun getChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                id = 1,
                question = "Hai 3 interruttori e una lampadina in una stanza chiusa. Puoi entrare nella stanza una sola volta. Come scopri quale interruttore controlla la lampadina?",
                options = listOf(
                    "Accendo e spengo gli interruttori a caso",
                    "Accendo un interruttore, aspetto, lo spengo, accendo un altro e poi entro",
                    "Entro due volte nella stanza",
                    "Non è possibile saperlo"
                ),
                correctIndex = 1,
                explanation = "Accendi il primo interruttore per qualche minuto, poi spegnilo e accendi il secondo. Entrando, se la lampadina è accesa è il secondo; se è spenta ma calda è il primo; se è spenta e fredda è il terzo.",
                type = ChallengeType.LOGIC
            ),
            Challenge(
                id = 2,
                question = "Un padre e un figlio hanno in totale 66 anni. Il padre ha l'età del figlio invertita. Quanti anni ha il figlio?",
                options = listOf(
                    "12",
                    "15",
                    "24",
                    "33"
                ),
                correctIndex = 1,
                explanation = "Le coppie invertite plausibili sono 12/21, 15/51, 24/42. Solo 15 + 51 = 66.",
                type = ChallengeType.LOGIC
            ),
            Challenge(
                id = 3,
                question = "Più ne togli, più diventa grande. Cos'è?",
                options = listOf(
                    "Un buco",
                    "Un numero",
                    "Una scatola",
                    "Una strada"
                ),
                correctIndex = 0,
                explanation = "Un buco: togliendo materiale, il buco aumenta.",
                type = ChallengeType.LATERAL
            )
        )
    }
}