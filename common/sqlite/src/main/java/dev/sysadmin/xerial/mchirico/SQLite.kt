package dev.sysadmin.xerial.mchirico

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Logger
import kotlin.reflect.KProperty1

//Ref : https://gist.github.com/mchirico/4751124
class SQLite {
    private val workscope = CoroutineScope(Dispatchers.IO)
    private val logger = Logger.getLogger("SQLite")
    private lateinit var connection: Connection
    private lateinit var statement: Statement
    private var rs: ResultSet? = null

    companion object {

        private val LOCK = Any()
        private var instance: SQLite? = null

        @JvmStatic
        fun getSQLite(file_with_path: String): SQLite {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = SQLite(file_with_path)
                }
            }
            return instance!!
        }
    }

    private constructor(File: String) {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:$File")
            // example of how you would do it in memory
            // Connection connection =
            // DriverManager.getConnection("jdbc:sqlite::memory:");
            statement = connection.createStatement()
            statement.setQueryTimeout(30)
        } catch (e: Exception) {
            logger.info("SQLite init JDBC error ${e.message}")
            System.err.println(e.message)
        }
    }

    private constructor(File: String, TimeOut: Int) {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:$File")
            // example of how you would do it in memory
            // Connection connection =
            // DriverManager.getConnection("jdbc:sqlite::memory:");
            statement = connection.createStatement()
            statement.setQueryTimeout(TimeOut)
        } catch (e: Exception) {
            logger.info("SQLite init JDBC error ${e.message}")
            System.err.println(e.message)
        }
    }

    fun initTable(table_name: String, command: String) {
        execSQL("drop table if exists $table_name")
        execSQL(command)
    }

    fun execSQL(command: String) {
        logger.info("SQLite executeUpdate get command $command")
        try {
            statement!!.executeUpdate(command)
            /*
             * statement.executeUpdate("drop table if exists person"); statement
             * .executeUpdate("create table person (id integer, name string)");
             * statement.executeUpdate("insert into person values(1, 'leo')");
             * statement.executeUpdate("insert into person values(2, 'yui')");
             */
        } catch (e: Exception) {
            logger.info("SQLite executeUpdate error ${e.message}")
            System.err.println(e.message)
        }
    }

    fun select(command: String) {
        try {
            rs = statement.executeQuery(command)
            while (rs!!.next()) {
                // read the result set
                val rsmd = rs!!.getMetaData()
                val colCount = rsmd.columnCount

                // System.out.println("colCount:" + colCount);
                var sep = ""
                for (i in 1 until colCount + 1) {
                    print(sep + rs!!.getString(i))
                    sep = ","
                }
                println()
            }
        } catch (e: Exception) {
            logger.info("SQLite select error ${e.message}")
            System.err.println(e.message)
        }
    }

    fun close() {
        try {
            if (connection != null) connection.close()
        } catch (e: Exception) {
            logger.info("SQLite close error ${e.message}")
            println(e.message)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}

inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        null
    }
}