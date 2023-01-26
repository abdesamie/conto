package com.ximedes.conto.db;

import com.ximedes.conto.AbstractIntegrationTest;
import com.ximedes.conto.domain.Account;
import com.ximedes.conto.domain.AccountCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransferStressTest extends AbstractIntegrationTest {


    @Autowired
    private AccountMapper accountMapper;

    @BeforeEach
    public void initialize() {
        createUser("test");
        accountMapper.insertAccount(new Account("JQCNSIJSIJZZ855", "test", "description", 0L));

    }

    @Test
    public void testSummationWithConcurrency() throws InterruptedException {
        int numberOfThreads = 500;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {

                    accountMapper.updateAccountBalance("JQCNSIJSIJZZ855", 2L);

                latch.countDown();
            });
        }
        latch.await();
        List<Account> accounts = accountMapper.find(new AccountCriteria("test", "JQCNSIJSIJZZ855"));

        assertEquals(1100L, accounts.get(0).getBalance());
    }
}