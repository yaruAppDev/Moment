package com.yaruappdev.moment

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var inputText: EditText

    private val todoList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI要素の取得
        listView = findViewById(R.id.listView)
        addButton = findViewById(R.id.addButton)
        inputText = findViewById(R.id.inputText)

        // アダプターの設定
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoList)
        listView.adapter = adapter

        // 項目を追加する
        addButton.setOnClickListener {
            val text = inputText.text.toString().trim()
            if (text.isNotEmpty()) {
                todoList.add(text)
                adapter.notifyDataSetChanged()
                inputText.text.clear()
            } else {
                Toast.makeText(this, "TODOを入力してください", Toast.LENGTH_SHORT).show()
            }
        }

        // 項目を編集する
        listView.setOnItemClickListener { _, _, position, _ ->
            showEditDialog(position)
        }

        // 項目を長押しで削除する
        listView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteConfirmationDialog(position)
            true
        }
    }

    private fun showEditDialog(position: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("TODOを編集")

        // ダイアログ内の入力欄を設定
        val editText = EditText(this)
        editText.setText(todoList[position]) // 現在のTODO内容をセット
        dialogBuilder.setView(editText)

        // 保存ボタンの設定
        dialogBuilder.setPositiveButton("保存") { _, _ ->
            val newText = editText.text.toString().trim()
            if (newText.isNotEmpty()) {
                todoList[position] = newText
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "TODOを更新しました", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "内容を空にはできません", Toast.LENGTH_SHORT).show()
            }
        }

        // キャンセルボタンの設定
        dialogBuilder.setNegativeButton("キャンセル", null)

        // ダイアログを表示
        dialogBuilder.create().show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        // 確認ダイアログの表示
        AlertDialog.Builder(this)
            .setTitle("削除確認")
            .setMessage("このTODOを削除しますか？")
            .setPositiveButton("削除") { _, _ ->
                todoList.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "TODOを削除しました", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("キャンセル", null)
            .create()
            .show()
    }
}
