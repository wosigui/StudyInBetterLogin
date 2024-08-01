// UserAdapter.kt
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyinbetterlogin.databinding.LayoutUserItemBinding
import com.example.studyinbetterlogin.db.User
import com.example.studyinbetterlogin.viewmodel.MainViewModel

class UserAdapter(
    private val userList: List<User>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: LayoutUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, viewModel: MainViewModel) {
            binding.Account.text = user.account
            binding.root.setOnClickListener {
                Log.d("end",user.account)
                viewModel.LoginByParrern.value=binding.Account.text.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LayoutUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, viewModel)
    }
}
