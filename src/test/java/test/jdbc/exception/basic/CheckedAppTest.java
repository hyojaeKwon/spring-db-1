package test.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.MemberSubstitution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("error", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic() throws SQLException, ConnectException {
            networkClient.call();
            repository.call();
        }

    }
    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
