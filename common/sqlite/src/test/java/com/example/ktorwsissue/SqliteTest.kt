package com.example.ktorwsissue

import dev.sysadmin.xerial.mchirico.SQLite
import dev.sysadmin.xerial.mchirico.getThroughReflection
import kotlinx.coroutines.runBlocking
import org.junit.Test
import tw.gov.president.cks.fcm.data.FCMToken
import java.util.*
import java.util.logging.Logger
import kotlin.reflect.full.memberProperties

class SqliteTest {
    private val logger = Logger.getLogger("SqliteTest")

    @Test
    fun testPerson() = runBlocking {
//        val sp = SQLite("./sqlitejava.db")
        val sp = SQLite.getSQLite("./sqlitejava.db")
        val table_name = "person"
        val colum_id = "_id"
        val CONFLICT_VALUES =  arrayOf("", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE ")
        sp.execSQL("drop table if exists $table_name")
//        sp.execSQL("create table person (id int, name string,timeEnter DATE)")
//        val CREATE_STUDENT_TABLE = ("CREATE TABLE $table_name ("
//                + colum_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "id int, "
//                + Config.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
//                 + "timeEnter DATE )")
//        sp.execSQL(CREATE_STUDENT_TABLE)
        //        val keyset   = mutableListOf<String>()
//        val dbcolumkeyset = hashMapOf<String, String>()
        val dbcolumkeyset = linkedMapOf<String, String>()
        var person = Person()
        person.javaClass.kotlin.memberProperties.forEach {
//            logger.info("Show class members name  ${it.name}")
//            logger.info("Show class members class type  ${it.returnType}")
            val key = it.name
            val value =
                it.returnType.toString().replace("kotlin.", "").replace("String", "TEXT NOT NULL")
            dbcolumkeyset.put(key, value)
        }
        val sql = StringBuilder()
        sql.append("CREATE TABLE")
        sql.append(" $table_name ")
        sql.append('(')
        sql.append("$colum_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
        var size =
            if (dbcolumkeyset != null && dbcolumkeyset.isNotEmpty()) dbcolumkeyset.size else 0
        if (size > 0) {
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
                sql.append(colName)
                sql.append(" ")
                sql.append(dbcolumkeyset.get(colName))
            }
            sql.append(",timeEnter DATE")
            sql.append(')')
        }
        logger.info("Show class gen sql command   ${sql.toString()}")
        sp.execSQL(sql.toString())
        sp.execSQL("DROP TRIGGER IF EXISTS insert_${table_name}_timeEnter")
        sp.execSQL(
            "CREATE TRIGGER insert_${table_name}_timeEnter AFTER  INSERT ON $table_name "
                    + "BEGIN "
                    + "UPDATE $table_name SET timeEnter = DATETIME('NOW')  WHERE rowid = new.rowid; "
                    + "END;"
        )
//        for (i in 0..4) sp.execSQL(
//            "insert into $table_name (id,name) values (" + i
//                    + ",'temp" + i + "')"
//        )


        for (i in 0..4) {
            person = Person(i, "temp${i}")
            sql.clear()
            sql.append("INSERT")
            sql.append(CONFLICT_VALUES.get(0))
            sql.append(" INTO ")
            sql.append(" $table_name ")
            sql.append('(')
//            val memberProperties=person.javaClass.kotlin.memberProperties
//            memberProperties.withIndex().forEach { indexedvalue ->
//                 val index= indexedvalue.index
//            }
//            for (member in memberProperties) {
//                val index = memberProperties.withIndex().forEach {
//
//                }
//                sql.append(if (index > 0) "," else "")
//                sql.append(colName)
//                sql.append(" ")
//                sql.append(dbcolumkeyset.get(colName))
//            }
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
                sql.append(colName)
            }
            sql.append(')')
            sql.append(" values ")
            sql.append('(')
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
//                val value =  person.getThroughReflection<String>(colName)
                val value =  if (index == 0) person.getThroughReflection<Int>(colName) else person.getThroughReflection<String>(colName)
                sql.append(if (index == 0)value else "'"+value+"'")
            }
            sql.append(')')
            sp.execSQL(sql.toString())
        }
        sp.execSQL("insert into $table_name (id,name) values (200,'sue')")
        sp.select("select * from $table_name")
        sp.close()
    }

    @Test
    fun testReflection() = runBlocking {
        data class Person(val name: String, val employed: Boolean)

        val p = Person("Jane", true)
        val name = p.getThroughReflection<String>("name")
        val employed = p.getThroughReflection<Boolean>("employed")

        println("$name - $employed")
    }

    @Test
    fun testFCMToken() = runBlocking {
        val table_name = FCMToken::class.simpleName!!.lowercase(Locale.getDefault())
        logger.info("Show table_name   $table_name")
//        val sp = SQLite("./${table_name}.db")
        val sp = SQLite.getSQLite("./${table_name}.db")
        val colum_id = "_id"
        val CONFLICT_VALUES =
            arrayOf("", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE ")
        sp.execSQL("drop table if exists $table_name")
        val dbcolumkeyset = linkedMapOf<String, String>()
        var token = FCMToken()
        token.javaClass.kotlin.memberProperties.forEach {
            val key = it.name
            val value =
                it.returnType.toString().replace("kotlin.", "").replace("String", "TEXT NOT NULL")
            dbcolumkeyset.put(key, value)
        }
        val sql = StringBuilder()
        sql.append("CREATE TABLE")
        sql.append(" $table_name ")
        sql.append('(')
        sql.append("$colum_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
        var size =
            if (dbcolumkeyset != null && dbcolumkeyset.isNotEmpty()) dbcolumkeyset.size else 0
        if (size > 0) {
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
                sql.append(colName)
                sql.append(" ")
                sql.append(dbcolumkeyset.get(colName))
            }
            sql.append(",timeEnter DATE")
            sql.append(')')
        }
        logger.info("Show class gen sql command   ${sql.toString()}")
        sp.execSQL(sql.toString())
        sp.execSQL("DROP TRIGGER IF EXISTS insert_${table_name}_timeEnter")
        sp.execSQL(
            "CREATE TRIGGER insert_${table_name}_timeEnter AFTER  INSERT ON $table_name "
                    + "BEGIN "
                    + "UPDATE $table_name SET timeEnter = DATETIME('NOW')  WHERE rowid = new.rowid; "
                    + "END;"
        )

        for (i in 0..4) {
            token = FCMToken ("erewrqew$i", "${i}temp${i*10}")
            sql.clear()
            sql.append("INSERT")
            sql.append(CONFLICT_VALUES.get(0))
            sql.append(" INTO ")
            sql.append(" $table_name ")
            sql.append('(')
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
                sql.append(colName)
            }
            sql.append(')')
            sql.append(" values ")
            sql.append('(')
            for (colName in dbcolumkeyset.keys) {
                val index = dbcolumkeyset.keys.indexOf(colName)
                sql.append(if (index > 0) "," else "")
//                val value =  person.getThroughReflection<String>(colName)
                val value =
                    if (index == 0) token.getThroughReflection<String>(colName) else token.getThroughReflection<String>(
                        colName
                    )
                sql.append( "'" + value + "'")
            }
            sql.append(')')
            sp.execSQL(sql.toString())
        }
        sp.execSQL("insert into $table_name (deviceId,fcmToken) values ('fdregewrtretewyrwyreyrewyewrtqt43','vcxvdsfgdgsdfgsdfggsdfgsdfg')")
        sp.select("select * from $table_name")
        sp.close()
    }

}