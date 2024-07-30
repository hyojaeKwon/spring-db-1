package test.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

class UnCheckedAppTest {

    @Test
    void unChecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(controller::request).isInstanceOf(RuntimeException .class);
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic() {
            networkClient.call();
            repository.call();
        }

    }
    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL () throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        /**
         * 예외를 발생 시킬 떄는 기존 예외를 꼭 포함하라
         * @param cause
         */
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
