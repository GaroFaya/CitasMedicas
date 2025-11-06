package com.garofaya.citasmedicas.db.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performBlocking
import androidx.sqlite.SQLiteStatement
import com.garofaya.citasmedicas.db.entity.Usuario
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UsuarioDao_Impl(
  __db: RoomDatabase,
) : UsuarioDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUsuario: EntityInsertAdapter<Usuario>

  private val __deleteAdapterOfUsuario: EntityDeleteOrUpdateAdapter<Usuario>
  init {
    this.__db = __db
    this.__insertAdapterOfUsuario = object : EntityInsertAdapter<Usuario>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `usuario` (`id`,`name`,`dni`,`password`,`token`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Usuario) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.dni)
        statement.bindText(4, entity.password)
        val _tmpToken: String? = entity.token
        if (_tmpToken == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpToken)
        }
      }
    }
    this.__deleteAdapterOfUsuario = object : EntityDeleteOrUpdateAdapter<Usuario>() {
      protected override fun createQuery(): String = "DELETE FROM `usuario` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Usuario) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
  }

  public override fun insert(usuario: Usuario): Unit = performBlocking(__db, false, true) { _connection ->
    __insertAdapterOfUsuario.insert(_connection, usuario)
  }

  public override fun eliminar(usuario: Usuario): Unit = performBlocking(__db, false, true) { _connection ->
    __deleteAdapterOfUsuario.handle(_connection, usuario)
  }

  public override fun listar(): Usuario? {
    val _sql: String = "SELECT * FROM usuario LIMIT 1"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDni: Int = getColumnIndexOrThrow(_stmt, "dni")
        val _columnIndexOfPassword: Int = getColumnIndexOrThrow(_stmt, "password")
        val _columnIndexOfToken: Int = getColumnIndexOrThrow(_stmt, "token")
        val _result: Usuario?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDni: String
          _tmpDni = _stmt.getText(_columnIndexOfDni)
          val _tmpPassword: String
          _tmpPassword = _stmt.getText(_columnIndexOfPassword)
          val _tmpToken: String?
          if (_stmt.isNull(_columnIndexOfToken)) {
            _tmpToken = null
          } else {
            _tmpToken = _stmt.getText(_columnIndexOfToken)
          }
          _result = Usuario(_tmpId,_tmpName,_tmpDni,_tmpPassword,_tmpToken)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun validarUsuario(dni: String, password: String): Usuario? {
    val _sql: String = "SELECT * FROM usuario WHERE dni = ? AND password = ?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, dni)
        _argIndex = 2
        _stmt.bindText(_argIndex, password)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDni: Int = getColumnIndexOrThrow(_stmt, "dni")
        val _columnIndexOfPassword: Int = getColumnIndexOrThrow(_stmt, "password")
        val _columnIndexOfToken: Int = getColumnIndexOrThrow(_stmt, "token")
        val _result: Usuario?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDni: String
          _tmpDni = _stmt.getText(_columnIndexOfDni)
          val _tmpPassword: String
          _tmpPassword = _stmt.getText(_columnIndexOfPassword)
          val _tmpToken: String?
          if (_stmt.isNull(_columnIndexOfToken)) {
            _tmpToken = null
          } else {
            _tmpToken = _stmt.getText(_columnIndexOfToken)
          }
          _result = Usuario(_tmpId,_tmpName,_tmpDni,_tmpPassword,_tmpToken)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
