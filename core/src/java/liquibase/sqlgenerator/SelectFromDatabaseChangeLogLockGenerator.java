package liquibase.sqlgenerator;

import liquibase.statement.SelectFromDatabaseChangeLogLockStatement;
import liquibase.database.Database;
import liquibase.database.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.util.StringUtils;

public class SelectFromDatabaseChangeLogLockGenerator implements SqlGenerator<SelectFromDatabaseChangeLogLockStatement> {
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    public boolean supports(SelectFromDatabaseChangeLogLockStatement statement, Database database) {
        return true;
    }

    public ValidationErrors validate(SelectFromDatabaseChangeLogLockStatement statement, Database database) {
        ValidationErrors errors = new ValidationErrors();
        errors.checkRequiredField("columnToSelect", statement.getColumnsToSelect());

        return errors;
    }

    public Sql[] generateSql(SelectFromDatabaseChangeLogLockStatement statement, Database database) {
        String sql = "SELECT "+ StringUtils.join(statement.getColumnsToSelect(), ",")+" FROM " +
                database.escapeTableName(database.getDefaultSchemaName(), database.getDatabaseChangeLogLockTableName()) +
                " WHERE " + database.escapeColumnName(database.getDefaultSchemaName(), database.getDatabaseChangeLogLockTableName(), "ID") + "=1";

        if (database instanceof OracleDatabase) {
            sql += " for update";
        }
        return new Sql[] {
                new UnparsedSql(sql)
        };
    }
}