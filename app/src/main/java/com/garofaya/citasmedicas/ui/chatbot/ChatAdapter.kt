package com.garofaya.citasmedicas.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.garofaya.citasmedicas.R

class ChatAdapter(private val mensajes: List<Mensaje>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USUARIO = 0
        private const val BOT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].esUsuario) USUARIO else BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == USUARIO) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mensaje_usuario, parent, false)
            UsuarioViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mensaje_bot, parent, false)
            BotViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = mensajes[position]
        if (holder is UsuarioViewHolder) holder.bind(mensaje)
        if (holder is BotViewHolder) holder.bind(mensaje)
    }

    override fun getItemCount(): Int = mensajes.size

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMensaje: TextView = itemView.findViewById(R.id.tvMensaje)
        fun bind(mensaje: Mensaje) {
            tvMensaje.text = mensaje.texto
        }
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMensaje: TextView = itemView.findViewById(R.id.tvMensaje)
        fun bind(mensaje: Mensaje) {
            tvMensaje.text = mensaje.texto
        }
    }
}
