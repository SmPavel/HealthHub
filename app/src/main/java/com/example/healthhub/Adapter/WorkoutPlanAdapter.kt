import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthhub.R
import com.google.firebase.firestore.FirebaseFirestore

data class WorkoutData(val exerName: String, val count: Long, var checkBox: Boolean, val documentId: String)

class WorkoutPlanAdapter(private val context: Context, private val data: List<WorkoutData>) :
    RecyclerView.Adapter<WorkoutPlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_workout_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvWorkName: TextView = itemView.findViewById(R.id.exercises_name)
        private val tvWorkCount: TextView = itemView.findViewById(R.id.count)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        fun bind(item: WorkoutData) {
            tvWorkName.text = item.exerName
            tvWorkCount.text = item.count.toString()

            checkbox.isChecked = item.checkBox
            checkbox.setOnCheckedChangeListener(null)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = data[position]
                    item.checkBox = isChecked
                    db.collection("workout").document(item.documentId)
                        .update("completed", isChecked)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                }
            }
        }
    }
}