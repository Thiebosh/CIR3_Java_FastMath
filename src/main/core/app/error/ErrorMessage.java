package core.app.error;

import java.util.HashMap;

/**
 * Messages d'erreurs possibles, associées à une enum
 * @see ErrorCode
 */
public class ErrorMessage {
    /**
     * Lien code d'erreur / message d'erreur
     */
    private static final HashMap<ErrorCode, String> messages = new HashMap<>() {{
        put(ErrorCode.AlreadyUsedName, "Nom d'expression déjà utilisé");
        put(ErrorCode.UnknowName, "Nom d'expression inconnu");
        put(ErrorCode.InvalidNameCauseNumber, "Nom d'expression invalide : Convertible en nombre");
        put(ErrorCode.InvalidNameCauseSyntax, "Nom d'expression invalide : Réservé par le langage");
        put(ErrorCode.UnhautorizedDeletion, "Impossible de supprimer le contenu");
    }};

    /**
     * Accès au message d'erreur
     * @param error code d'erreur rencontré
     * @return message d'erreur associé
     */
    public static String getMessage(ErrorCode error) { return messages.get(error); }
}
