package guru.qa.niffler.utils;

import javax.sql.DataSource;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class FailingCommitJdbcTransactionManager extends JdbcTransactionManager {

    public FailingCommitJdbcTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doCommit(@NotNull DefaultTransactionStatus status) {
        throw new RuntimeException("имитированная ошибка на commit");
    }
}
