import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.domain.Pokemon
import java.util.Locale

class PokemonAdapter(private var items: List<Pokemon?>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private var filteredList: List<Pokemon?> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.bindView(item)
    }

    fun filter(text: String?) {
        text?.let { query ->
            filteredList = if (query.isEmpty()) {
                items
            } else {
                items.filter { pokemon ->
                    pokemon?.name?.contains(query, true) == true
                }
            }
            notifyDataSetChanged()
        }
    }

    fun setItems(newItems: List<Pokemon?>) {
        items = newItems
        filteredList = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        private val tvNumber: TextView = itemView.findViewById(R.id.tvNumber)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvType1: TextView = itemView.findViewById(R.id.tvType1)

        fun bindView(item: Pokemon?) {
            item?.let {
                Glide.with(itemView.context).load(it.imageUrl).into(ivPokemon)
                tvNumber.text = "N° ${item.formattedNumber}"
                tvName.text = item.formattedName
                tvType1.text = item.types[0].name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }
        }
    }
}
