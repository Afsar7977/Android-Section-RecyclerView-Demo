package com.afsar.sectionrecycler.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DResponse(
    @SerializedName("vthumb")
    @Expose
    var thumb: String,
    @SerializedName("vid_teacher")
    @Expose
    var teacher: String,
    @SerializedName("vdesc")
    @Expose
    var desc: String,
    @SerializedName("vid_id")
    @Expose
    var vidid: String,
    @SerializedName("sub_start_at")
    @Expose
    var substart: String,
    @SerializedName("subject_name")
    @Expose
    var subjName: String,
    @SerializedName("t_name")
    @Expose
    var tName: String,
    @SerializedName("vtitle")
    @Expose
    var title: String,
    @SerializedName("sub_end_at")
    @Expose
    var subend: String
)