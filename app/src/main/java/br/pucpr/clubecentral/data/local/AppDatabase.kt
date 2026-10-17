package br.pucpr.clubecentral.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.pucpr.clubecentral.model.Evento
import br.pucpr.clubecentral.model.PerfilPreferencias
import androidx.room.migration.Migration

@Database(
    entities = [
        Evento::class,
        PerfilPreferencias::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventoDao(): EventoDao
    abstract fun perfilDao(): PerfilDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `perfil_preferencias` (
                `id` INTEGER NOT NULL,
                `aparencia` TEXT NOT NULL,
                `idioma` TEXT NOT NULL,
                `notificacoesAtivas` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
                )
            }
        }
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eventos.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                "INSERT INTO eventos (nome, data, descricao) " +
                                        "VALUES ('a', 'a', 'a'), " +
                                        "('b', 'b', 'b')"
                            )
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}