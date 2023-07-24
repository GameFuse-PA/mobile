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


class RankingAdapter(private val scores: List<ScoreboardData>):
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    private val sortedScores: List<ScoreboardData> = scores.sortedByDescending { scoreboardData ->
        scoreboardData.scores.sumOf { scoreData -> scoreData.score }
    }

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        val iconTextView: TextView = itemView.findViewById(R.id.iconTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ranking_component, parent, false)
        return RankingViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val player = scores[position]
        player.let {
            Picasso.get()
                .load(player.avatar.location)
                .into(holder.avatarImageView)

            holder.usernameTextView.text = player.username
            holder.scoreTextView.text = player.scores.size.toString()
            if(position == 0) {
                holder.iconTextView.text = //TODO : icone dorée
            } else if (position == 1){
                holder.iconTextView.text = //TODO: icone argentée
            } else if(position == 2) {
                holder.iconTextView.text = //TODO : icone bronze
            } else {
                holder.iconTextView.text = //TODO : icone neutre
            }

        }
    }

    override fun getItemCount(): Int {
        return sortedScores.size
    }
}