package com.example.sudiplearningnestquiz.activity.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sudiplearningnestquiz.R
import com.example.sudiplearningnestquiz.activity.QuizActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LevelAdapter(
    private val listOfLevel: List<Levels>,
    private val context: Context,
    private val dataOfAllLevels: List<PreviousLevels>,
    private val index: Int
) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val levelTv: TextView = view.findViewById(R.id.levelTv)
        val itemsCardView: CardView = view.findViewById(R.id.itemsCardView)
        val star1IV: ImageView = view.findViewById(R.id.star1IV)
        val star2IV: ImageView = view.findViewById(R.id.star2IV)
        val star3IV: ImageView = view.findViewById(R.id.star3IV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_item, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = listOfLevel[position].level.toString()
        holder.levelTv.text = level

        fetchData(holder, position)

        holder.itemsCardView.setOnClickListener {
            val levelInt = level.toInt()
            moveToAnotherActivity(levelInt)
        }
    }

    override fun getItemCount(): Int {
        return listOfLevel.size
    }

    private fun moveToAnotherActivity(levelInt: Int) {
        val intent = Intent(context, QuizActivity::class.java).apply {
            putExtra("level", levelInt)
            putParcelableArrayListExtra("list", ArrayList(listOfLevel))
        }
        context.startActivity(intent)
    }

    private fun fetchData(holder: LevelViewHolder, levelIndex: Int) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userRef = db.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val existingData = documentSnapshot.data ?: return@addOnSuccessListener
                        val levelData = existingData["$levelIndex"] as? Map<String, Any>?

                        val correctAns = levelData?.get("correctAns")?.toString()?.toIntOrNull() ?: 0

                        when (correctAns) {
                            1 -> holder.star1IV.setImageResource(R.drawable.star_14441715)
                            2 -> {
                                holder.star1IV.setImageResource(R.drawable.star_14441715)
                                holder.star2IV.setImageResource(R.drawable.star_14441715)
                            }
                            3 -> {
                                holder.star1IV.setImageResource(R.drawable.star_14441715)
                                holder.star2IV.setImageResource(R.drawable.star_14441715)
                                holder.star3IV.setImageResource(R.drawable.star_14441715)
                            }
                            else -> {
                                holder.star1IV.setImageResource(R.drawable.star_2956792)
                                holder.star2IV.setImageResource(R.drawable.star_2956792)
                                holder.star3IV.setImageResource(R.drawable.star_2956792)
                            }
                        }
                    } else {
                        Log.d(ContentValues.TAG, "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error getting document", e)
                }
        } else {
            holder.star1IV.setImageResource(R.drawable.star_2956792)
            holder.star2IV.setImageResource(R.drawable.star_2956792)
            holder.star3IV.setImageResource(R.drawable.star_2956792)
        }
    }
}
