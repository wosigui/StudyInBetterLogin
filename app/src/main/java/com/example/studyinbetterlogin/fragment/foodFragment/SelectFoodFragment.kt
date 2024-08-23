package com.example.studyinbetterlogin.fragment.foodFragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.api.Recipe
import com.example.studyinbetterlogin.api.RecipeResponse
import com.example.studyinbetterlogin.api.RetrofitClient
import com.example.studyinbetterlogin.databinding.FragmentStudyBinding
import com.example.studyinbetterlogin.databinding.MovieItemLayoutBinding
import com.example.studyinbetterlogin.fragment.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectFoodFragment : BaseFragment<FragmentStudyBinding>(){
    override fun initBinding(): FragmentStudyBinding {
        return FragmentStudyBinding.inflate(layoutInflater)
    }
    var thisMealType="breakfast"
    var thisDietType="vegan"
    var isTypeExist=false
    //东哥的api：
    private val apiKey="1a0edebda73f4a17ad82375357e41313"
    //private val apiKey = "934a1819fcb5482e9ec708c1a140808b"
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun initView() {
        val swipeRefreshLayout = mBinding.refreshView
        val recyclerView = mBinding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            recyclerView.postDelayed({
                val call = RetrofitClient.instance.searchRecipes(
                    query = "sweet",
                    diet = "vegan",
                    type = "breakfast",
                    instructionsRequired = true,
                    fillIngredients = true,
                    number = 10,
                    addRecipeInformation = true,
                    apiKey = apiKey
                )
                refreshFoodRecycleView(call)
                swipeRefreshLayout.isRefreshing = false
            }, 2000) // 假设数据加载耗时2秒
        }
        val call = RetrofitClient.instance.searchRecipes(
            query = "",
            diet = "vegan",
            type = "breakfast",
            instructionsRequired = true,
            fillIngredients = true,
            number = 10,
            addRecipeInformation = true,
            apiKey = apiKey
        )
        mBinding.selectFood.setOnClickListener{
            slideViewUp(mBinding.tagSelect,300f)
            mBinding.selectFood.visibility=View.GONE
            mBinding.backgroundBlack.visibility=View.VISIBLE
            isTypeExist=true
        }
        refreshFoodRecycleView(call)
        for(view in listOf(mBinding.MainCourse,mBinding.snack,mBinding.dessert,mBinding.Appetizer)){
            view.setOnClickListener {
                val call = RetrofitClient.instance.searchRecipes(
                    query = "",
                    diet = thisDietType,
                    type = view.text.toString(),
                    instructionsRequired = true,
                    fillIngredients = true,
                    number = 10,
                    addRecipeInformation = true,
                    apiKey = apiKey
                )
                thisMealType=view.text.toString()
                refreshFoodRecycleView(call)
            }
        }
        for(view in listOf(mBinding.Vegan,mBinding.Vegetarian,mBinding.Ketogenic,mBinding.DairyFree)){
            view.setOnClickListener {
                val call = RetrofitClient.instance.searchRecipes(
                    query = "",
                    diet = view.text.toString(),
                    type = thisMealType,
                    instructionsRequired = true,
                    fillIngredients = true,
                    number = 10,
                    addRecipeInformation = true,
                    apiKey = apiKey
                )
                thisDietType=view.text.toString()
                refreshFoodRecycleView(call)
            }
        }
        mBinding.backgroundBlack.setOnClickListener {
            slideViewUp(mBinding.tagSelect,-300f)
            mBinding.selectFood.visibility=View.VISIBLE
            mBinding.backgroundBlack.visibility=View.GONE
            isTypeExist=false
        }

    }

    private fun slideViewUp(view: View, distanceDp: Float) {
            // 将 dp 转换为 px
            val distancePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                distanceDp,
                view.context.resources.displayMetrics
            ).toInt()

            // 获取视图的当前 LayoutParams
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            val startMargin = layoutParams.topMargin
            val endMargin = startMargin - distancePx

            // 创建 ValueAnimator 动画
            val animator = ValueAnimator.ofInt(startMargin, endMargin)
            animator.duration = 500 // 动画时长，单位为毫秒
            animator.addUpdateListener { animation ->
                // 动态更新视图的 topMargin
                layoutParams.topMargin = animation.animatedValue as Int
                view.layoutParams = layoutParams
            }

            animator.start()
    }
    private fun refreshFoodRecycleView(call:Call<RecipeResponse>){
        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.results
                    recipes?.forEach { recipe ->
                        Log.d("MainActivity", "Recipe: ${recipe.title}, Instructions: ${recipe.instructions}")
                    }
                    recipes?.let {
                        mBinding.recyclerView.models = it // 这里将食物API的数据应用到RecyclerView
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch data: ${t.message}")
            }
        })
        mBinding.recyclerView.linear().setup {
            addType<Recipe>(R.layout.movie_item_layout)
            onBind {
                val binding = getBinding<MovieItemLayoutBinding>()
                val model = getModel<Recipe>()
                Glide.with(context)
                    .load(model.image)
                    .into(binding.ivIcon)
                binding.tvTitle.text = model.title
                binding.tvCountry.text = model.diet
            }
        }

    }

}
