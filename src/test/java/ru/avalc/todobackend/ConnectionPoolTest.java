package ru.avalc.todobackend;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexei Valchuk, 11.04.2023, email: a.valchukav@gmail.com
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConnectionPoolTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void giveTomcatConnectionPoolInstance() {
        DataSource dataSource = context.getBean("dataSource", DataSource.class);

        assertThat(dataSource.getMaxIdle()).isEqualTo(15);
        assertThat(dataSource.getClass().getName())
                .isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
    }
}
