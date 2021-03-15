package com.example.gottest.ui.fragment.main_fragment

import com.example.gottest.R
import com.example.gottest.data.IceAndFireCharacter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.character_list_item.view.*

class CharacterUIItem(
    val char: IceAndFireCharacter,
    val onClick: (character: IceAndFireCharacter) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        val s = "${char.name}"
        itemView.characterName.text = s
//        itemView.characterBirthday.text = char.born
        itemView.setOnClickListener { onClick.invoke(char) }
    }

    override fun getLayout(): Int {
        return R.layout.character_list_item
    }

}