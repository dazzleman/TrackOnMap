package com.example.admin.testproject01.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ATrack(
        @SerializedName("type")
        @Expose
        var type: String,
        @SerializedName("dt_start")
        @Expose
        var dtStart: String,
        @SerializedName("dt_end")
        @Expose
        var dtEnd: String,
        @SerializedName("time")
        @Expose
        var time: String,
        @SerializedName("distance")
        @Expose
        var distance: String,
        @SerializedName("id_track")
        @Expose
        var idTrack: String,
        @SerializedName("sp_avg")
        @Expose
        var spAvg: String,
        @SerializedName("sp_max")
        @Expose
        var spMax: String,
        @SerializedName("calories")
        @Expose
        var calories: String,
        @SerializedName("description")
        @Expose
        var description: String? = null,
        @SerializedName("access")
        @Expose
        var access: String,
        @SerializedName("weight")
        @Expose
        var weight: String,
        @SerializedName("cardio")
        @Expose
        var cardio: String,
        @SerializedName("hr_max")
        @Expose
        var hrMax: Int,
        @SerializedName("hr_avg")
        @Expose
        var hrAvg: Int,
        @SerializedName("var_max")
        @Expose
        var varMax: String,
        @SerializedName("var_min")
        @Expose
        var varMin: String,
        @SerializedName("status")
        @Expose
        var status: Boolean
        )