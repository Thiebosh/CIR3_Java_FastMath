package core.app.error;

import java.util.HashMap;

public class ErrorMessage {
    private static final HashMap<ErrorCode, String> messages = new HashMap<>() {{
        put(ErrorCode.AlreadyUsedName, "Nom d'expression déjà utilisé");
        put(ErrorCode.UnknowName, "Nom d'expression inconnu");
        put(ErrorCode.InvalidNameCauseNumber, "Nom d'expression invalide : Convertible en nombre");
        put(ErrorCode.InvalidNameCauseSyntax, "Nom d'expression invalide : Réservé par le langage");
        put(ErrorCode.UnhautorizedDeletion, "Impossible de supprimer le contenu");
    }};

    public static String getMessage(ErrorCode error) {
        return messages.get(error);
    }
}
