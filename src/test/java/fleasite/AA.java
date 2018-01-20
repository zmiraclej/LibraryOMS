package fleasite;

/**
 * @author bruce
 * @2015年12月14日 下午3:07:20
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.pojo.User;
import com.flea.modules.news.dao.NewsDao;
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional
@TransactionConfiguration(defaultRollback=false,transactionManager="transactionManager")
public class AA {
	@Autowired
	private NewsDao dao;
	@Test
	public void testPush() {
		User user = new User();
		user.setHallCode("TZPT");
		dao.getSelectMore("客户资讯", user);
	}
}
