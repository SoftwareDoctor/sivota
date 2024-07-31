package it.softwaredoctor.sivota.utility;


public class EmailDetails {

    public static final String SUBJECT = "Votazione";
    public static final String SUBJECTCONFIRM ="Registrazione";
    public static final String LINKVOTAZIONE = "http://localhost:8080/api/v1/votazione/";
    public static final String BODY = "<h1> Benvenut alla votazione di oggi. " + "<p>Ecco il link per votare: <a href=\"" + LINKVOTAZIONE + "\"> Grazie</a></p>";


    public static String generateBodyLinkWithUUID(String url) {
        return "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<div style='background-color: #f0f0f0; padding: 20px;'>"
                + "<img src='https://example.com/logo.png' alt='Logo' style='display: block; margin: 0 auto; max-width: 200px;'>"
                + "<h2 style='color: #007bff; text-align: center;'>Gentile utente,</h2>"
                + "<p style='text-align: center;'>La sua votazione è stata avviata. Cliccare sul link sottostante per procedere:</p>"
                + "<p style='text-align: center;'><a href='" + url + "' style='display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;'>Link alla votazione</a></p>"
                + "</div>"
                + "</body></html>";
    }





    public static String generateBodyEmailConfirm(String confirmationUrl) {
        return "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<div style='background-color: #f0f0f0; padding: 20px;'>"
                + "<img src='https://example.com/logo.png' alt='Logo' style='display: block; margin: 0 auto; max-width: 200px;'>"
                + "<h2 style='color: #007bff; text-align: center;'>Gentile utente,</h2>"
                + "<p style='text-align: center;'>Grazie per esserti registrato. Clicca sul link sottostante per confermare la tua registrazione:</p>"
                + "<p style='text-align: center;'><a href='" + confirmationUrl + "' style='display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;'>Conferma Registrazione</a></p>"
                + "</div>"
                + "</body></html>";
    }


}
