package com.example.admin.testproject01.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class ResponseData(
        @SerializedName("aWaypoints")
        @Expose
        var aWaypoints: List<Any>? = null,
        @SerializedName("aTrack")
        @Expose
        var aTrack: ATrack,
        @SerializedName("aPoints")
        @Expose
        var aPoints: List<List<Double>>,
        @SerializedName("sMsg")
        @Expose
        var sMsg: String,
        @SerializedName("sMsgTitle")
        @Expose
        var sMsgTitle: String,
        @SerializedName("bStateError")
        @Expose
        var bStateError: Boolean,
        @SerializedName("min_id")
        @Expose
        var minId: Int? = null
)