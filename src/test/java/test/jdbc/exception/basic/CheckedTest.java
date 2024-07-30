package test.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    void checked_test() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void throw_test() {
        Service service = new Service();
        Assertions.assertThatThrownBy(service::callThorw).isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception을 상속받은 class는 checked예외가 된다.
     */
    static class MyCheckedException extends Exception{
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();
        /**
         * 예외 처리
         */
        public void callCatch()  {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외처리, message = {}", e.getMessage(),e);
            }
        }

        /**
         * 예외 안처리
         * @throws MyCheckedException
         */
        public void callThorw() throws MyCheckedException {
            repository.call();
        }

    }
    static class Repository{
        public void call() throws MyCheckedException{
            throw new MyCheckedException("ex");
        }
    }
}
