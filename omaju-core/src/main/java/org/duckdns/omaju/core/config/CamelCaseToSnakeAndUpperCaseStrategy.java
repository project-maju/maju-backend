package org.duckdns.omaju.core.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CamelCaseToSnakeAndUpperCaseStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convert(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convert(name);
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    private Identifier convert(Identifier name) {
        if (name == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(name.getText());
        for (int i = 1; i < sb.length() - 1; i++) {
            if (Character.isLowerCase(sb.charAt(i - 1)) && Character.isUpperCase(sb.charAt(i))
                    && Character.isLowerCase(sb.charAt(i + 1))) {
                sb.insert(i++, '_');
            }
        }

        return new Identifier(sb.toString().toUpperCase(), name.isQuoted());
    }
}
