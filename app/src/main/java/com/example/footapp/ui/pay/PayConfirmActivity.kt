package com.example.footapp.ui.pay

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.databinding.ActivityPayConfirmBinding
import com.example.footapp.interface1.PayConfirmInterface
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.presenter.PayConfirmPresenter
import com.example.footapp.ui.BaseActivity
import com.example.footapp.utils.*
import java.text.SimpleDateFormat
import java.util.*

class PayConfirmActivity : BaseActivity<ActivityPayConfirmBinding>(), PayConfirmInterface {
    var tablePos = 0
    var map: HashMap<Int, DetailItemChoose> = hashMapOf()
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
    lateinit var presenter: PayConfirmPresenter
    lateinit var adapter: ItemConfirmAdapter
    var items: ArrayList<DetailItemChoose> = arrayListOf()
    override fun getContentLayout(): Int {
        return R.layout.activity_pay_confirm
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        tablePos = intent.getIntExtra(TABLE_POSITION, 0)

        presenter = PayConfirmPresenter(this, this)
        map = intent.getSerializableExtra(MAP) as HashMap<Int, DetailItemChoose>
        binding.tvName.text = MyPreference().getInstance(this)?.getUser()?.name
        binding.tvTime.text = simpleDateFormat.format(Calendar.getInstance().time)

        items.addAll(map.values.filter { it.count!! > 0 })
        adapter = ItemConfirmAdapter(items)
        binding.rcItem.layoutManager = LinearLayoutManager(this)
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = intent.getIntExtra(TOTAL_PRICE, 0).toString() + " đ"

    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener { showDialog() }
        binding.imvBack.setOnClickListener {
            finish()
        }
    }

    fun showDialog() {
        var dialog = ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept(passwd: String) {
                presenter.payConfirm(map, passwd, intent.getIntExtra(TOTAL_PRICE, 0))
            }

        })
        dialog.show(supportFragmentManager, "")
    }


    override fun complete(message: String, flag: Boolean) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if (flag) {
            var intent = Intent(TABLE_ACTION)
            intent.putExtra(POS_BACK,tablePos)
            sendBroadcast(intent)
            finish()

        }
    }


}
