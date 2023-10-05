package com.helo.ming.yu;

import com.helo.ming.yu.crawler.AhhhhfsCrawler;
import com.helo.ming.yu.halo.BatchCreateHaloBlogFromDataBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YuApplicationTests {

	@Autowired
	private AhhhhfsCrawler ahhhhfsCrawler;

	@Autowired
	private BatchCreateHaloBlogFromDataBaseService batchCreateService;
	@Test
	void createFromAh() {
		ahhhhfsCrawler.execute();
	}

	@Test
	void createFromDb() {
		batchCreateService.execute();
	}

}
