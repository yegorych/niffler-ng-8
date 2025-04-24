package guru.qa.niffler.utils;

import javax.sql.DataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class FailingCommitJdbcTransactionManager extends JdbcTransactionManager {

    public FailingCommitJdbcTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        throw new RuntimeException("имитированная ошибка на commit");
    }
}
