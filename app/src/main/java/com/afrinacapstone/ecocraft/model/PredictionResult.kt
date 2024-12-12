package com.afrinacapstone.ecocraft.model

import android.os.Parcelable
import com.afrinacapstone.ecocraft.api.response.CraftsItem
import com.afrinacapstone.ecocraft.api.response.CraftsItemHistoryPrediction
import com.afrinacapstone.ecocraft.api.response.GetCraftsResponse
import com.afrinacapstone.ecocraft.api.response.GetHistoryPredictionItemResponse
import com.afrinacapstone.ecocraft.api.response.PostPredictionsResponse
import com.afrinacapstone.ecocraft.api.response.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Craft(

    val image: String,

    val materials: List<String>,

    val id: String,

    val title: String,

    val steps: List<String>,

    val user: CraftUser?

    ) : Parcelable

@Parcelize
data class PredictionResult(

    val result: String,

    val crafts: List<Craft>,

    val percentage: String,

    val materialImage: String? = null

) : Parcelable

@Parcelize
data class CraftUser(

    val name: String,

    val photo: String,

    val id: String
) : Parcelable

fun PostPredictionsResponse.toPredictionResult(): PredictionResult {
    val crafts = data.crafts.map { it.toCraft() }
    return PredictionResult(
        result = data.result,
        crafts = crafts,
        percentage = data.percentage,
    )
}

fun CraftsItem.toCraft(): Craft {
    return Craft(
        image = image,
        materials = materials,
        id = id,
        title = title,
        steps = steps,
        user = user.toCraftUser()

    )
}

fun GetCraftsResponse.toCrafts(): List<Craft> {
    return data.map {
        Craft(
            image = it.image,
            materials = it.materials,
            id = it.id,
            title = it.title,
            steps = it.steps,
            user = it.user.toCraftUser()
        )
    }
}

fun User.toCraftUser(): CraftUser {
    return CraftUser(
        name = name,
        photo = photo,
        id = id
    )
}

fun GetHistoryPredictionItemResponse.toPredictionResult(): PredictionResult {
    val crafts = data.crafts.map { it.toCraft() }
    return PredictionResult(
        result = data.result,
        crafts = crafts,
        percentage = data.percentage,
        materialImage = data.materialImage
    )
}

fun CraftsItemHistoryPrediction.toCraft(): Craft {
    return Craft(
        image = image,
        materials = materials,
        id = id,
        title = title,
        steps = steps,
        user = null
    )
}