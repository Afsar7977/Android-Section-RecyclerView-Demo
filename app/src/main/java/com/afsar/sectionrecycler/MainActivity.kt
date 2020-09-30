@file:Suppress(
    "PrivatePropertyName", "unused",
    "HasPlatformType", "DEPRECATION"
)

package com.afsar.sectionrecycler

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afsar.sectionrecycler.Modal.DResponse
import com.afsar.sectionrecycler.Modal.SectionResponse
import com.afsar.sectionrecycler.Network.Status
import com.afsar.sectionrecycler.VModel.VModel
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var vModel: VModel
    private lateinit var sadapter: CustomAdapter
    private var responseList: ArrayList<DResponse> = ArrayList()
    private var TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        vModel = ViewModelProviders.of(this).get(VModel::class.java)
        recyclerView = findViewById(R.id.section_recycler_view)

        try {
            loadRecyclerData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        recyclerView.apply {
            val layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            sadapter = CustomAdapter(sectionResponse, applicationContext)
            recyclerView.adapter = sadapter
            sadapter.notifyDataSetChanged()
        }
    }

    @Suppress("PrivatePropertyName")
    inner class CustomAdapter(
        private val sList: ArrayList<SectionResponse>,
        private val vContext: Context
    ) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder1>() {

        var newList: ArrayList<DResponse> = ArrayList()
        private var viewPool = RecyclerView.RecycledViewPool()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder1 {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.list_header, parent, false)
            return ViewHolder1(v)
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder1, position: Int) {
            val parentItem = sList[position]
            val dateString =
                parentItem.headers[position].toString()
            val mydate: Date = SimpleDateFormat("yyyy-M-d").parse(dateString)!!
            val c = Calendar.getInstance()
            c.time = mydate
            when (c[Calendar.DAY_OF_WEEK]) {
                1
                -> {
                    holder.text1.text = "Sunday"
                }
                2 -> {
                    holder.text1.text = "Monday"
                }
                3 -> {
                    holder.text1.text = "Tuesday"
                }
                4 -> {
                    holder.text1.text = "Wednesday"
                }
                5 -> {
                    holder.text1.text = "Thursday"
                }
                6 -> {
                    holder.text1.text = "Friday"
                }
                7 -> {
                    holder.text1.text = "Saturday"
                }
            }
            val sessionList: ArrayList<DResponse> = ArrayList(parentItem.dResponse)
            val layoutManager = LinearLayoutManager(
                holder
                    .childRecyclerView
                    .context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager.initialPrefetchItemCount = parentItem.dResponse.size
            val childAdapter =
                ChildAdapter(sessionList, vContext)
            holder.childRecyclerView.layoutManager = layoutManager
            holder
                .childRecyclerView
                .setRecycledViewPool(viewPool)
            holder.childRecyclerView.adapter = childAdapter
        }

        override fun getItemCount(): Int {
            return sList.size
        }

        inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text1 = itemView.findViewById<TextView>(R.id.text1_task)
            val text2 = itemView.findViewById<TextView>(R.id.text2_task)
            val childRecyclerView =
                itemView.findViewById<RecyclerView>(R.id.child_recycler_view)
        }
    }

    inner class ChildAdapter(private val childList: ArrayList<DResponse>, val vContext: Context) :
        RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
        override fun getItemCount(): Int {
            return childList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(childList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.list_item, parent, false)
            return ViewHolder(v)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            fun bindItems(sdata: DResponse) {
                val cimage = itemView.findViewById<CircleImageView>(R.id.circular_image)
                val htitle = itemView.findViewById<TextView>(R.id.head_title)
                val title = itemView.findViewById<TextView>(R.id.sub_title)
                val name = itemView.findViewById<TextView>(R.id.sub_title1)
                Glide.with(vContext)
                    .asBitmap()
                    .fitCenter()
                    .load(sdata.thumb)
                    .error(R.drawable.b1)
                    .into(cimage)
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = dateFormatter.parse(sdata.substart)!!
                val timeFormatter = SimpleDateFormat("h:mm a")
                val displayValue = timeFormatter.format(date)
                htitle.text = "$displayValue at ${sdata.substart.subSequence(0, 10)}"
                title.text = sdata.title
                name.text = "By ${sdata.tName} on ${sdata.subjName}"
            }
        }
    }

    private fun loadRecyclerData() {
        try {
            vModel.getDataResponse().observe(this, {
                it?.let { apiResponse ->
                    try {
                        when (apiResponse.status) {
                            Status.LOADING -> {
                                Log.d(TAG, "loadRecyclerData:Loading")
                            }
                            Status.SUCCESS -> {
                                Log.d(TAG, "loadRecyclerData:${it.data?.body()}")
                                populateData(it.data!!.body()!!)
                            }
                            Status.ERROR -> {
                                Log.d(TAG, "loadRecyclerData:Error")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateData(list: List<DResponse>) {
        try {
            responseList.clear()
            sectionResponse.clear()
            responseList.addAll(list)
            val dateList = responseList.groupBy { it.substart.subSequence(0, 10) }
            val distinct = dateList.keys.distinct().toList()
            val dValues = dateList.values
            for (i in dValues.indices) {
                sectionResponse.add(
                    SectionResponse(
                        distinct,
                        dValues.elementAt(i)
                    )
                )
            }
            sadapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var sectionResponse: ArrayList<SectionResponse> = ArrayList()
        private var dumpList: ArrayList<DResponse> = ArrayList()
    }

}