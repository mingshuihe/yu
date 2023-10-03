package com.helo.ming.yu;

import com.helo.ming.yu.crawler.AhhhhfsCrawler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YuApplicationTests {

	@Autowired
	private AhhhhfsCrawler ahhhhfsCrawler;
	@Test
	void contextLoads() {
		ahhhhfsCrawler.execute();
	}


}
