package com.gamefuse.app.ranking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.api.model.response.ScoreboardData
import com.squareup.picasso.Picasso


class RankingAdapter(private val scores: List<ScoreboardData>): RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        val iconImageView: ImageView = itemView.findViewById(R.id.imageViewTrophee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ranking_component, parent, false)
        return ViewHolder(itemView)
    }

    private val sortedScores: List<ScoreboardData> = scores.sortedByDescending { scoreboardData ->
        scoreboardData.scores.sumOf { scoreData -> scoreData.score }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = scores[position]
        player.let {
            try {
                Picasso.get()
                    .load(player.avatar.location)
                    .into(holder.avatarImageView)
            } catch (e: Exception){
                Picasso.get()
                    .load(R.drawable.photo_avatar_profil)
                    .into(holder.avatarImageView)
            }
            holder.usernameTextView.text = player.username
            holder.scoreTextView.text = player.scores.size.toString()
            if (position == 0) {
                holder.iconImageView.setImageResource(R.drawable.coupe_or)
            } else if (position == 1) {
                holder.iconImageView.setImageResource(R.drawable.coupe__argent)
            } else if (position == 2) {
                holder.iconImageView.setImageResource(R.drawable.coupe_bronze)
            } else {
                holder.iconImageView.setImageResource(R.drawable.classic_trophy)
            }

        }
    }

    override fun getItemCount(): Int {
        return sortedScores.size
    }
}