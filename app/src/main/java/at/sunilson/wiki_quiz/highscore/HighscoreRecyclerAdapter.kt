package at.sunilson.wiki_quiz.highscore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.sunilson.wiki_quiz.R
import at.sunilson.wiki_quiz.game.models.Highscore
import kotlinx.android.synthetic.main.highscore_dialog_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Linus Weiss
 */

class HighscoreRecyclerAdapter(private val highscores: MutableList<Highscore>) : RecyclerView.Adapter<HighscoreRecyclerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return highscores.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.highscore_dialog_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vhView.highscore_dialog_points.text = highscores[position].points.toString()
        if (highscores[position].time > 9999) {
            holder.vhView.highscore_dialog_duration.text = "9999+ s"
        } else {
            holder.vhView.highscore_dialog_duration.text = highscores[position].time.toString() + "s"
        }
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        holder.vhView.highscore_dialog_date.text = format.format(Date(highscores[position].date))
        holder.vhView.highscore_dialog_correct_answers.text = highscores[position].correctAnswers.toString() + " / 10"
    }

    class ViewHolder(val vhView: View) : RecyclerView.ViewHolder(vhView)
}