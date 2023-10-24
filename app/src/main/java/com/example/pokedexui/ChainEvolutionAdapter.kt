package com.example.pokedexui
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ChainEvolutionAdapter(
    private val chainEvolution: List<Pokemon>,
    private val context: Context,
    private val clickListener: ((Pokemon) -> Unit)? = null
) : RecyclerView.Adapter<ChainEvolutionAdapter.ChainEvolutionViewHolder>() {

    inner class ChainEvolutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pokemonImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChainEvolutionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chain_evolution, parent, false)
        return ChainEvolutionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChainEvolutionViewHolder, position: Int) {
        val pokemon = chainEvolution[position]

        //Carica immagine delle evoluzioni
        Glide.with(context)
            .load(pokemon.imageUrl)
            .into(holder.imageView)

        //Suono del click
        holder.imageView.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(context, R.raw.pokemon_button)
            mediaPlayer.start()
            clickListener?.invoke(pokemon)
        }

    }

    override fun getItemCount(): Int {
        return chainEvolution.size
    }
}
