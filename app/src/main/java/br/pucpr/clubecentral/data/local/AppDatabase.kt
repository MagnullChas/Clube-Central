
package br.pucpr.clubecentral.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.pucpr.clubecentral.model.Evento
import br.pucpr.clubecentral.model.PerfilPreferencias
import br.pucpr.clubecentral.model.Usuario

@Database(
    entities = [
        Evento::class,
        PerfilPreferencias::class,
        Usuario::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventoDao(): EventoDao
    abstract fun perfilDao(): PerfilDao
    abstract fun usuarioDao(): UsuarioDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `usuarios` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `nome` TEXT NOT NULL,
                        `email` TEXT NOT NULL,
                        `senhaHash` TEXT NOT NULL,
                        `salt` TEXT NOT NULL,
                        `tipo` TEXT NOT NULL,
                        `sessaoAtiva` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS
                    `index_usuarios_email`
                    ON `usuarios` (`email`)
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            db.execSQL(
                                """
                                INSERT INTO eventos
                                (nome, data, descricao)
                                VALUES
                                ('a', 'a', 'a'),
                                ('b', 'b', 'b')
                                """.trimIndent()
                            )
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
