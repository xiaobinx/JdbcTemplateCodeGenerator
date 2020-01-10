package xyz.bq.jdbctool.template;


import xyz.bq.jdbctool.domain.JavaMeta;

import java.util.HashSet;

public class JavaBeanTemplate {
    public static final String indents = "    ";

    public static String formate(JavaMeta mt) {
        var fields = mt.getFeilds();
        var imports = imports(mt);
        var src = new StringBuilder();
        for (var imp : imports) {
            src.append(imp).append("\n");
        }
        src.append("\n");
        src.append("public class ").append(mt.getClassName()).append(" {\n");
        // private fiedl
        for (var field : fields) {
            src.append(indents).append(String.format("private %s %s;\n", field.getJavaType().getSimpleName(), field.getName()));
        }
        src.append("\n");
        // toString
        var tostringsb = new StringBuilder(indents).append("public String toString() {\n        ");
        tostringsb.append("return \"").append(mt.getClassName()).append("{\" +\n");
        // getter setter
        for (var i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            var clsSmpName = field.getJavaType().getSimpleName();
            var feildName = field.getName();
            src.append(indents).append("public ").append(clsSmpName).append(" ").append(field.getGetterName()).append("() {\n")
                    .append(indents).append(indents).append("return this.").append(feildName).append(";\n")
                    .append(indents).append("}\n\n");
            src.append(indents).append("public void ").append(field.getSetterName()).append("(").append(clsSmpName).append(" ").append(feildName).append(") {\n")
                    .append(indents).append(indents).append("this.").append(feildName).append(" = ").append(feildName)
                    .append(";\n").append(indents).append("}\n\n");

            if (i == 0) {
                tostringsb.append(indents).append(indents).append(indents).append(indents)
                        .append("\"").append(feildName).append("='\" + ").append(feildName).append(" + \"'\" +\n");
            } else {
                tostringsb.append(indents).append(indents).append(indents).append(indents)
                        .append("\", ").append(feildName).append("='\" + ").append(feildName).append(" + \"'\" +\n");
            }
        }
        tostringsb.append(indents).append(indents).append(indents)
                .append(indents).append("'}';\n");
        tostringsb.append(indents).append("}\n");
        src.append(tostringsb);
        src.append("}");
        return src.toString();
    }

    public static HashSet<String> imports(JavaMeta mt) {
        var imports = new HashSet<String>();
        for (var field : mt.getFeilds()) {
            addImport(imports, field.getJavaType());
        }
        return imports;
    }

    private static void addImport(HashSet<String> imports, Class<?> cls) {
        if (cls != String.class &&
                cls != byte.class &&
                cls != Byte.class &&
                cls != float.class &&
                cls != Float.class &&
                cls != Character.class &&
                cls != double.class &&
                cls != Double.class &&
                cls != short.class &&
                cls != Short.class &&
                cls != boolean.class &&
                cls != Boolean.class &&
                cls != long.class &&
                cls != Long.class &&
                cls != int.class &&
                cls != Integer.class) {
            imports.add("import " + cls.getName() + ";");
        }
    }
}
