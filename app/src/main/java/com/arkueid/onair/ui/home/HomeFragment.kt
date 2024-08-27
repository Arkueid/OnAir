package com.arkueid.onair.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.R
import com.arkueid.onair.databinding.FragmentHomeBinding
import com.arkueid.onair.ui.home.model.Module
import com.arkueid.onair.ui.home.model.ModuleItem
import com.arkueid.onair.ui.home.search.SearchActivity

class HomeFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener(this)

        binding.moduleRecyclerView.run {
            adapter = ModuleAdapter(testData)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.searchButton -> {
                val intent = Intent(context, SearchActivity::class.java)
                ActivityOptions.makeCustomAnimation(
                    context,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                ).let {
                    startActivity(intent, it.toBundle())
                }
            }
        }
    }

    private val testData: List<Module> by lazy {
        listOf(
            Module(
                Module.BANNER,
                null,
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=1",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=2",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=3",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=4",
                    )
                ),
                "https://www.google.com"
            ),
            Module(
                Module.SQUARE_LIST,
                "最近热播",
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=5",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=6",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=7",
                    ),
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=5",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=6",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=7",
                    ),
                ),
                null
            ),
            Module(
                Module.WIDE_RECTANGLE_LIST,
                "最近热播",
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=5",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=6",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=7",
                    ),
                ),
                null
            ),
            Module(
                Module.TALL_RECTANGLE_LIST,
                "日漫",
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=8",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=9",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=10"
                    ),
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=11",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=12",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=13"
                    )
                ),
                "ssssss"
            ),
            Module(
                Module.WIDE_RECTANGLE_GRID,
                "日漫",
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=8",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=9",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=10"
                    ),
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=11",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=12",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=13"
                    )
                ),
                "ssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID,
                "日漫",
                listOf(
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=8",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=9",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=10"
                    ),
                    ModuleItem(
                        "123",
                        "https://acg.suyanw.cn/sjdm/random.php?r=11",
                    ),
                    ModuleItem(
                        "456",
                        "https://acg.suyanw.cn/sjdm/random.php?r=12",
                    ),
                    ModuleItem(
                        "789",
                        "https://acg.suyanw.cn/sjdm/random.php?r=13"
                    )
                ),
                "ssss"
            ),
        )
    }
}