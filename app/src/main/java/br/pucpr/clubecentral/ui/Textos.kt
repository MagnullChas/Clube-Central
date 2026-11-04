package br.pucpr.clubecentral.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

// Guarda o idioma atual para todas as telas (preenchido na MainActivity)
val LocalIdioma = compositionLocalOf { "Português" }

// Ordem de cada lista: Português, English, Español
private val textos: Map<String, List<String>> = mapOf(

    // ================= MAIS COMUNS =================
    "app_nome" to listOf("Clube Central", "Clube Central", "Clube Central"),
    "nome" to listOf("Nome", "Name", "Nombre"),
    "email" to listOf("E-mail", "E-mail", "Correo electrónico"),
    "senha" to listOf("Senha", "Password", "Contraseña"),
    "adicionar" to listOf("Adicionar", "Add", "Agregar"),
    "editar" to listOf("Editar", "Edit", "Editar"),
    "excluir" to listOf("Excluir", "Delete", "Eliminar"),
    "salvar" to listOf("Salvar", "Save", "Guardar"),
    "salvar_alteracoes" to listOf("Salvar alterações", "Save changes", "Guardar cambios"),
    "cancelar" to listOf("Cancelar", "Cancel", "Cancelar"),
    "fechar" to listOf("Fechar", "Close", "Cerrar"),
    "ativar" to listOf("Ativar", "Enable", "Activar"),
    "ativado" to listOf("Ativado", "Enabled", "Activado"),
    "desativar" to listOf("Desativar", "Disable", "Desactivar"),
    "desativado" to listOf("Desativado", "Disabled", "Desactivado"),


    // ================= PÁGINAS =================


    // ================= LOGIN =================
    "login_subtitulo" to listOf("Entre na sua conta", "Sign in to your account", "Inicia sesión en tu cuenta"),
    "login_entrar" to listOf("Entrar", "Sign in", "Iniciar sesión"),
    "login_criar_conta" to listOf(
        "Não tem uma conta? Criar perfil",
        "Don't have an account? Create profile",
        "¿No tienes una cuenta? Crear perfil"
    ),

    // ================= CADASTRO =================
    "cadastro_titulo" to listOf("Criar perfil", "Create profile", "Crear perfil"),
    "cadastro_nome_completo" to listOf("Nome completo", "Full name", "Nombre completo"),
    "cadastro_senha" to listOf(
        "Senha (mínimo 4 caracteres)",
        "Password (minimum 4 characters)",
        "Contraseña (mínimo 4 caracteres)"
    ),
    "cadastro_confirmar_senha" to listOf("Confirmar senha", "Confirm password", "Confirmar contraseña"),
    "cadastro_criar" to listOf("Criar conta", "Create account", "Crear cuenta"),
    "cadastro_voltar" to listOf(
        "Já tem uma conta? Voltar ao login",
        "Already have an account? Back to sign in",
        "¿Ya tienes una cuenta? Volver a iniciar sesión"
    ),

    // ================= EVENTOS =================
    "evento_nome" to listOf("Nome do evento", "Event name", "Nombre del evento"),
    "evento_data" to listOf("Data", "Date", "Fecha"),
    "evento_descricao" to listOf("Descrição", "Description", "Descripción"),
    "evento_data_valor" to listOf("Data:", "Date:", "Fecha:"),

    // ================= HOME =================
    "home_espacos" to listOf("SEUS ESPAÇOS", "YOUR SPACES", "TUS ESPACIOS"),
    "home_ola" to listOf("Olá,", "Hello,", "Hola,"),
    "home_buscar" to listOf("Buscar clube", "Search club", "Buscar club"),
    "home_meus_clubes" to listOf("MEUS CLUBES", "MY CLUBS", "MIS CLUBES"),
    "home_ativos" to listOf("%02d ativos", "%02d active", "%02d activos"),
    "area_imagem" to listOf("ÁREA DE IMAGEM", "IMAGE AREA", "ÁREA DE IMAGEN"),
    "clube_membros" to listOf("membros", "members", "miembros"),
    "clube_eventos" to listOf("eventos", "events", "eventos"),


    // ================= CLUBE =================
    "clube_visao" to listOf("VISÃO DO CLUBE", "CLUB OVERVIEW", "VISIÓN DEL CLUB"),
    "clube_avisos_titulo" to listOf("AVISOS E ATUALIZAÇÕES", "NOTICES AND UPDATES", "AVISOS Y ACTUALIZACIONES"),
    "clube_nenhum_aviso" to listOf("Nenhum aviso.", "No notices.", "Ningún aviso."),
    "clube_novo_aviso" to listOf("Novo aviso", "New notice", "Nuevo aviso"),
    "clube_editar_aviso" to listOf("Editar aviso", "Edit notice", "Editar aviso"),
    "clube_excluir_aviso" to listOf("Excluir aviso", "Delete notice", "Eliminar aviso"),
    "clube_aviso_quando" to listOf(
        "Quando (ex.: HOJE, 12 OUT)",
        "When (e.g., TODAY, OCT 12)",
        "Cuándo (ej.: HOY, 12 OCT)"
    ),
    "clube_aviso" to listOf("Aviso", "Notice", "Aviso"),
    "clube_eventos_marcados" to listOf("EVENTOS MARCADOS", "SCHEDULED EVENTS", "EVENTOS PROGRAMADOS"),
    "clube_nenhum_evento" to listOf("Nenhum evento marcado.", "No scheduled events.", "Ningún evento programado."),
    "clube_informacoes" to listOf("INFORMAÇÕES DO CLUBE", "CLUB INFORMATION", "INFORMACIÓN DEL CLUB"),
    "clube_editar_info" to listOf("Editar informações", "Edit information", "Editar información"),
    "clube_encontros" to listOf("Encontros semanais", "Weekly meetings", "Reuniones semanales"),
    "clube_encontros_caps" to listOf("ENCONTROS SEMANAIS", "WEEKLY MEETINGS", "REUNIONES SEMANALES"),
    "clube_local" to listOf("Local", "Location", "Lugar"),
    "clube_local_caps" to listOf("LOCAL", "LOCATION", "LUGAR"),
    "clube_responsavel" to listOf("Responsável", "Person in charge", "Responsable"),
    "clube_responsavel_caps" to listOf("RESPONSÁVEL", "PERSON IN CHARGE", "RESPONSABLE"),
    "clube_nao_informado" to listOf("Não informado", "Not provided", "No informado"),
    "clube_mes_anterior" to listOf("Mês anterior", "Previous month", "Mes anterior"),
    "clube_proximo_mes" to listOf("Próximo mês", "Next month", "Mes siguiente"),
    "clube_legenda_evento" to listOf("Evento", "Event", "Evento"),
    "clube_toque_dia" to listOf(
        "   ·   Toque em um dia para marcar um evento",
        "   ·   Tap a day to schedule an event",
        "   ·   Toca un día para programar un evento"
    ),

    // ================= CALENDÁRIO =================
    // ----- Meses -----
    "mes_1" to listOf("JANEIRO", "JANUARY", "ENERO"),
    "mes_2" to listOf("FEVEREIRO", "FEBRUARY", "FEBRERO"),
    "mes_3" to listOf("MARÇO", "MARCH", "MARZO"),
    "mes_4" to listOf("ABRIL", "APRIL", "ABRIL"),
    "mes_5" to listOf("MAIO", "MAY", "MAYO"),
    "mes_6" to listOf("JUNHO", "JUNE", "JUNIO"),
    "mes_7" to listOf("JULHO", "JULY", "JULIO"),
    "mes_8" to listOf("AGOSTO", "AUGUST", "AGOSTO"),
    "mes_9" to listOf("SETEMBRO", "SEPTEMBER", "SEPTIEMBRE"),
    "mes_10" to listOf("OUTUBRO", "OCTOBER", "OCTUBRE"),
    "mes_11" to listOf("NOVEMBRO", "NOVEMBER", "NOVIEMBRE"),
    "mes_12" to listOf("DEZEMBRO", "DECEMBER", "DICIEMBRE"),

    // ----- Dias da semana (inicial, de domingo a sábado) -----
    "dia_1" to listOf("D", "S", "D"),
    "dia_2" to listOf("S", "M", "L"),
    "dia_3" to listOf("T", "T", "M"),
    "dia_4" to listOf("Q", "W", "X"),
    "dia_5" to listOf("Q", "T", "J"),
    "dia_6" to listOf("S", "F", "V"),
    "dia_7" to listOf("S", "S", "S"),

    // ================= CONFIGURAÇÕES =================
    "config_titulo" to listOf("CONFIGURAÇÕES", "SETTINGS", "AJUSTES"),
    "config_subtitulo" to listOf("Personalize seu Sistema", "Customize your system", "Personaliza tu sistema"),
    "config_preferencias" to listOf("PREFERÊNCIAS", "PREFERENCES", "PREFERENCIAS"),
    "privacidade" to listOf("Privacidade e segurança", "Privacy and security", "Privacidad y seguridad"),
    "clubes_permissoes" to listOf("Clubes e permissões", "Clubs and permissions", "Clubes y permisos"),
    "notificacoes" to listOf("Notificações", "Notifications", "Notificaciones"),
    "aparencia" to listOf("Aparência", "Appearance", "Apariencia"),
    "idioma" to listOf("Idioma", "Language", "Idioma"),
    "ap_sistema" to listOf("Sistema", "System", "Sistema"),
    "ap_claro" to listOf("Claro", "Light", "Claro"),
    "ap_escuro" to listOf("Escuro", "Dark", "Oscuro"),
    "notif_ativas" to listOf(
        "As notificações estão ATIVAS. Deseja desativá-las?",
        "Notifications are ENABLED. Do you want to disable them?",
        "Las notificaciones están ACTIVADAS. ¿Deseas desactivarlas?"
    ),
    "notif_desativadas" to listOf(
        "As notificações estão DESATIVADAS. Deseja ativá-las?",
        "Notifications are DISABLED. Do you want to enable them?",
        "Las notificaciones están DESACTIVADAS. ¿Deseas activarlas?"
    ),
    "privacidade_msg" to listOf(
        "Esta área poderá reunir as opções de proteção e privacidade da sua conta.",
        "This area may gather the protection and privacy options of your account.",
        "Esta área podrá reunir las opciones de protección y privacidad de tu cuenta."
    ),
    "clubes_msg" to listOf(
        "Aqui poderão ser exibidos os clubes associados ao usuário e suas permissões.",
        "Here you may see the clubs linked to the user and their permissions.",
        "Aquí se podrán mostrar los clubes asociados al usuario y sus permisos."
    ),

    // ================= PERFIL =================
    "perfil_conta" to listOf("CONTA PESSOAL", "PERSONAL ACCOUNT", "CUENTA PERSONAL"),
    "perfil_titulo" to listOf("Seu perfil", "Your profile", "Tu perfil"),
    "perfil_usuario" to listOf("Usuário", "User", "Usuario"),
    "perfil_config" to listOf("CONFIGURAÇÕES DE PERFIL", "PROFILE SETTINGS", "AJUSTES DEL PERFIL"),
    "perfil_editar_dados" to listOf("Editar dados pessoais", "Edit personal data", "Editar datos personales"),
    "sair_conta" to listOf("Sair da conta", "Sign out", "Cerrar sesión"),
    "sair_confirma" to listOf(
        "Deseja realmente encerrar sua sessão?",
        "Do you really want to end your session?",
        "¿Realmente deseas cerrar tu sesión?"
    ),
    "sair" to listOf("Sair", "Sign out", "Salir")
)

// Uso: Text(t("config_titulo"))
@Composable
fun t(chave: String): String {
    val posicao = when (LocalIdioma.current) {
        "English" -> 1
        "Español" -> 2
        else -> 0
    }
    return textos[chave]?.getOrNull(posicao) ?: chave
}