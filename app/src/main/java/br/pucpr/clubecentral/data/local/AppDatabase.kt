
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
import br.pucpr.clubecentral.model.Clube
import br.pucpr.clubecentral.model.Aviso

@Database(
    entities = [
        Evento::class,
        PerfilPreferencias::class,
        Usuario::class,
        Clube::class,
        Aviso::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventoDao(): EventoDao
    abstract fun perfilDao(): PerfilDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun clubeDao(): ClubeDao
    abstract fun avisoDao(): AvisoDao

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

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `clubes` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nome` TEXT NOT NULL,
                `membros` INTEGER NOT NULL,
                `eventos` INTEGER NOT NULL,
                `imagem` TEXT NOT NULL
            )
            """.trimIndent()
                )

                db.execSQL(
                    """
            INSERT INTO clubes (nome, membros, eventos, imagem) VALUES
            ('Clube de RPG', 24, 2, 'rpg'),
            ('Atlética Universitária', 82, 4, 'atletica')
            """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE eventos ADD COLUMN clubeId INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE clubes ADD COLUMN encontros TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE clubes ADD COLUMN local TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE clubes ADD COLUMN responsavel TEXT NOT NULL DEFAULT ''")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `avisos` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `clubeId` INTEGER NOT NULL,
                        `quando` TEXT NOT NULL,
                        `texto` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    UPDATE clubes SET encontros = 'Sextas · 18:30',
                    local = 'Bloco C · Sala 204', responsavel = 'Marina Souza'
                    WHERE nome = 'Clube de RPG'
                    """.trimIndent()
                )
                db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, 'HOJE', 'A sessão de sexta mudou de sala.' FROM clubes WHERE nome = 'Clube de RPG'")
                db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, '12 OUT', 'Inscrições abertas para a nova campanha.' FROM clubes WHERE nome = 'Clube de RPG'")
                db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, '08 OUT', 'Atualização no calendário de encontros.' FROM clubes WHERE nome = 'Clube de RPG'")
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
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6
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

                            // Instalação nova: cria os clubes e os dados do RPG.
                            db.execSQL(
                                """
                                INSERT INTO clubes
                                (nome, membros, eventos, imagem, encontros, local, responsavel)
                                VALUES
                                ('Clube de RPG', 24, 2, 'rpg', 'Sextas · 18:30', 'Bloco C · Sala 204', 'Marina Souza'),
                                ('Atlética Universitária', 82, 4, 'atletica', '', '', '')
                                """.trimIndent()
                            )
                            db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, 'HOJE', 'A sessão de sexta mudou de sala.' FROM clubes WHERE nome = 'Clube de RPG'")
                            db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, '12 OUT', 'Inscrições abertas para a nova campanha.' FROM clubes WHERE nome = 'Clube de RPG'")
                            db.execSQL("INSERT INTO avisos (clubeId, quando, texto) SELECT id, '08 OUT', 'Atualização no calendário de encontros.' FROM clubes WHERE nome = 'Clube de RPG'")
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
