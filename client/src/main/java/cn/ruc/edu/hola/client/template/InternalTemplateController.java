package cn.ruc.edu.hola.client.template;

interface InternalTemplateController {
    static boolean isTemplated(String text) {
        return !text.contains("${");
    }
}