package cn.ruc.edu.hola.client.template;

interface InternalTemplateController {
    static boolean isTemplated(String text) {
        return !text.contains("${");
    }

    static String normalize(String text) {
        text = text.replaceAll("\"#", "");
        text = text.replaceAll("#\"", "");
        return text;
    }
}