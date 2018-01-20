package com.flea.modules.system.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.PasswordHelper;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.dao.LibraryDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryBook;
import com.flea.modules.system.pojo.LibraryCirculateMap;
import com.flea.modules.system.pojo.LibraryCirculateRel;
import com.flea.modules.system.pojo.vo.LibraryCity;
import com.flea.modules.system.pojo.vo.Librarys_Books;
import com.google.gson.Gson;

/**
 * @author bruce
 * @2016年5月25日 上午9:49:27
 */
@Repository
public class LibraryDaoImpl extends BaseDaoImpl<Library, Long> implements LibraryDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#findLibraryWithGroupByCity()
	 */
	@Override
	public Map<String,List<LibraryCity>> getLibrarysWithGroupByCity() {
		String hql ="SELECT new com.flea.modules.system.pojo.vo.LibraryCity(l.id,l.name,l.city.code,l.city.name) from Library l,City s where l.city=s.code";
		Session session = getSession();
		Query query = session.createQuery(hql);
		List<LibraryCity> list=query.list();
		Map<String,List<LibraryCity>> map = new HashMap<String,List<LibraryCity>>();
		for(LibraryCity library:list){
			String cityCode = library.getCityName();
			if(map.containsKey(cityCode)){
				map.get(cityCode).add(library);
			}else {
				List<LibraryCity> l = new ArrayList<LibraryCity>();
				l.add(library);
				map.put(cityCode, l);
			}
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#findByCode(java.lang.String)
	 */
	@Override
	public Library findByCode(String code) {
		String hql ="from Library where hallCode='"+code+"'";
		Query query = getSession().createQuery(hql);
		return (Library)query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#updateBorrowModelById(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateBorrowModelById(Integer maxSum,Integer freeRentDays,String rent,String libId) {
		String sql ="update librarys set maxSum=?,freeRentDate=?,rent =? where id=?";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setInteger(0, maxSum);
		query.setInteger(1, freeRentDays);
		query.setDouble(2, Double.valueOf(rent));
		query.setInteger(3, Integer.parseInt(libId));
		return query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#updateDepositModelById(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateDepositModelById(String reader,String deposit,String libId) {
		String sql ="update librarys set readerLimited=?,needDeposit=? where id=?";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setString(0,reader);
		query.setString(1,deposit);
		query.setInteger(2,Integer.parseInt(libId));
		return query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#updateCirculateById(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateCirculateById(String Operation, String libId) {
		return false;
	}
	
	/**
	 * 更新用户所选中的图书馆范围
	 */
	@Override
	public boolean updateLibraryScope(String allId, int scope) {
		boolean flage = false;
		if(allId == null || allId.trim().length() ==0)
		{
			return false;
		}
		else
		{
		String sql ="update librarys set scope=? where id=?";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//把需要更新的id遍历出来
		String[] arr = allId.split(",");
		
		if(arr.length>1){
		String id = Arrays.toString(arr);
		//转化成数字
		id = id.substring(1,id.length()-1);
		String querySql = "SELECT lb.needDeposit FROM librarys AS lb WHERE lb.id IN ("+id+")";
		SQLQuery query1 = this.getSession().createSQLQuery(querySql);
		//查找押金模式是否统一
		List<String> list = query1.list();
		for (int i = 0; i < list.size(); i++) {
			if(!list.get(0).equals(list.get(i))){
				//如果所选的模式分别有押金和非押金模式的话    
				flage = true;
			}
		}
		//都是统一的模式
		if(!flage){
			for (int i = 0; i < arr.length; i++) {
				query.setInteger(0,scope);
				query.setInteger(1,Integer.parseInt(arr[i]));
				//执行语句
				query.executeUpdate();
			}
		}else{
		//非统一模式
			return false;
		}
		//如果只更改一个区域
		}else{
			query.setInteger(0,scope);
			query.setInteger(1,Integer.parseInt(arr[0]));
			//执行语句
			query.executeUpdate();
		}
		
		return true;
		
		}
		
		
	}

	
	
	@Override
	public int changeStatusToSotpOrUndelete(String statusKey,
			Object statusValue, String idKey, Object idValue) {
		Session session = getSession();
		Query query = session.createQuery("update Library set "+statusKey+"=? where "+idKey+"=? and "+statusKey+"!=?");
		query.setParameter(0, statusValue);
		query.setParameter(1, idValue);
		query.setParameter(2, Common.FLAG_DISABLE);
		return query.executeUpdate();
	}

	@Override
	public int getMaxScope() {
		//得到当前登录者的
		/*User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();*/
		String sql = "SELECT max(li.scope) FROM librarys as li";
				//+ " WHERE li.customerId = ?";
		SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
	//	query.setInteger(0, id);
		List list = query.list();
		String str = new Gson().toJson(list).toString();
		//返回当前用户的最大值
		if("[null]".equals(str))
		{
			return 0;
		}
		else
		{
			return (int)list.get(0);
		}
	}

	
	/**
	 * 
	 * @method:find
	 * @Description:find	馆际流通审核列表
	 * @author: HeTao
	 * @date:2016年6月20日 上午11:06:27
	 * @param:@param page
	 * @param:@param cil
	 * @param:@param status
	 * @param:@return
	 * @return:Page<LibraryCirculate>
	 */
	@Override
	public Map<String,Object> findLibarary(Page<LibraryCirculateRel> page,String searchText) {
		Map<String,Object> maps = new HashMap<String,Object>();
		List<Librarys_Books> lbs = null;
		List<LibraryCirculateRel> lis = null;
		User user = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		lbs = new ArrayList<Librarys_Books>();
		lis = new ArrayList<LibraryCirculateRel>();
		String hql = " SELECT lc.outDate,lc.outCode,lc.outHallCode,lc.inHallCode,lc.id  from library_circulate lc LEFT JOIN librarys l on lc.outHallCode = l.hallCode where 1=1";
		if(level.equals(Common.ROLE_SECOND_LEVLE)) {
			hql += " and l.customerId=?";
		}
		if(searchText != null){
			if("".equals(searchText)){
				hql += " and lc.outState=21 AND lc.inState=26";
			}else{
				hql += " and lc.outCode = '"+searchText+"' AND lc.outState=21 AND lc.inState=26";
			}
		}else{
			 hql += " and lc.outState=21 AND lc.inState=26";
		}
		
		hql += " order by id desc";
		
		SQLQuery query = getSession().createSQLQuery(hql);
		if(level.equals(Common.ROLE_SECOND_LEVLE)) {
			query.setInteger(0, user.getCustomer().getId());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(LibraryCirculateRel.class));
		List<LibraryCirculateRel> list = query.list();
		page.setCount(list.size());
		

		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		list = query.list();

		if(list != null || list.size() != 0){
			for (LibraryCirculateRel lb : list) {
				String out = queryLibraryName(lb.getOutHallCode());
				String in = queryLibraryName(lb.getInHallCode());
				String username = getName(lb.getId());
				Librarys_Books look = new Librarys_Books();
				look.setInLib(in);look.setOutLib(out);look.setUserName(username);
				lbs.add(look);
				lis.add(lb);
			}
		}
		maps.put("list",lis);
		maps.put("val",lbs);
		return maps;
	}

	//得到发布者的名字。。。。。
	private String getName(Integer id) {
		String con = "";
		String sql = "SELECT lu.userName FROM library_circulate lc JOIN librarys_users lu ON(lc.operUser = lu.id) WHERE lc.id = ?";
		SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setInteger(0,id);
		Object obj = query.uniqueResult();
		if(obj != null)
		{
			con = new Gson().toJson(obj).toString();
			return con;
		}
		return "暂未填写";
	}

	//得到流通馆的名字
	private String queryLibraryName(String HallCode) {
		String hql = "FROM Library WHERE hallCode=?";
		Query query = this.getSession().createQuery(hql);
		query.setString(0, HallCode);
		Library lb= (Library) query.uniqueResult();
		if(lb == null)
		{
			return "暂无详情";
		}
		return lb.getName();
	}

	/**
	 * 
	 * @method:reviewLibraryInfo
	 * @Description:reviewLibraryInfo	馆际流通审核详情
	 * @author: HeTao
	 * @date:2016年6月22日 下午3:00:09
	 * @param:@param id
	 * @param:@return
	 * @return:List<LibraryBook>
	 */
	@Override
	public List<LibraryBook> reviewLibraryInfo(Integer id) {
		List<LibraryBook> list = null;
		list = new ArrayList<LibraryBook>();
		int arr[] = null;
		String hql = "FROM LibraryCirculateMap WHERE circulateId = ?";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0,id);
		List<LibraryCirculateMap> lis = query.list();
		if(lis.size() != 0 || lis != null){
			arr = new int[lis.size()];
			for (int i=0;i<lis.size();i++) {
				arr[i] =lis.get(i).getLibraryBookId(); 
			}
		}
		if(arr!=null){
			String sql = "FROM LibraryBook WHERE id = ?";
			for (int i = 0; i < arr.length; i++) {
				Query qy = this.getSession().createQuery(sql);
				qy.setInteger(0,arr[i]);
				LibraryBook lb = (LibraryBook) qy.uniqueResult();
				list.add(lb);
			}
		}
		return list;
	}

	/**
	 * 
	 * @method:circulateInfo
	 * @Description:circulateInfo	借书详情
	 * @author: HeTao
	 * @date:2016年6月23日 上午10:49:50
	 * @param:@param id
	 * @param:@return
	 * @return:LibraryCirculateRel
	 */
	@Override
	public Map<String, Object> queryCirculate(Integer id) {
		Map<String, Object> maps = new HashMap<String,Object>();
		String hql = "FROM LibraryCirculateRel WHERE id = ?";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0, id);
		LibraryCirculateRel lb = (LibraryCirculateRel) query.uniqueResult();
		Librarys_Books books = new Librarys_Books();
		String out = queryLibraryName(lb.getOutHallCode());
		String in = queryLibraryName(lb.getInHallCode());
		books.setInLib(in);
		books.setOutLib(out);
		maps.put("lb",lb);
		maps.put("book",books);
		return maps;
	}

	@Override
	public com.flea.modules.system.pojo.vo.User getUserInfo(String hallcode) {
		String sql = "SELECT conperson,phone FROM librarys WHERE hallCode = ?";
		SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setString(0,hallcode);
		List lis = query.list();
		if(lis != null){
			Object[] obj = (Object[]) lis.get(0);
			com.flea.modules.system.pojo.vo.User us = new com.flea.modules.system.pojo.vo.User();
			us.setUsername(obj[0].toString());
			us.setPhone(obj[1].toString());
			return us;
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * @method:updateCirculate
	 * @Description:updateCirculate 改变审核订单状态
	 * @author: HeTao
	 * @date:2016年6月23日 下午2:53:45
	 * @param:@param state
	 * @param:@param id
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean updateCirculate(Integer state, Integer id) {
		String sql = "";
		SQLQuery query = null;
		//得到当前用户
		User user = ShiroUtils.getCurrentUser();
		String name = user.getUserName();
		//批准
		if(state == 1)
		{
			sql = "UPDATE library_circulate as l SET l.outState=22,l.inState=27,l.auditDate=NOW(),l.auditPerson=? WHERE l.id = ?";
			query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			query.setString(0,name);
			query.setInteger(1,id);
			return query.executeUpdate()==1?true:false;
			//this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		}
		//否决
		else
		{
			sql = "UPDATE library_circulate as l SET l.outState=23,l.inState=null,l.auditDate=NOW() WHERE l.id = ?";
			query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
			query.setInteger(0,id);
			return query.executeUpdate()==1?true:false;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#findLibraryBooksSumByCode(java.lang.String)
	 */
	@Override
	public int findLibraryBooksSumByCode(String libCode) {
		String sqlString ="SELECT COUNT(*) FROM library_books WHERE belongLibraryHallCode='"+libCode+"'";
		SQLQuery  query = getSession().createSQLQuery(sqlString);
		getBySQL(sqlString, null);
		return ((BigInteger)query.uniqueResult()).intValue();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#saveLibraryUser(java.lang.String)
	 */
	@Override
	public Boolean saveLibraryUser(String hallCode) {
		org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
		String userPwd = PasswordHelper.getStringRandom(6);//随机生成密码
		currentSession.setAttribute(hallCode+"userPassword",userPwd);
	 	userPwd = EncryptUtils.encryptMD5(userPwd);
		String sqlString ="INSERT INTO librarys_users (libraryHallCode,userName,password,isManager,isEffective) VALUES('"+hallCode+"','admin','"+userPwd+"','1','1')";
	 	SQLQuery query = getSession().createSQLQuery(sqlString);
	 	return query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#updateLibraryUserPassword(java.lang.String)
	 */
	@Override
	public boolean updateLibraryUserPassword(String hallCode,String userPwd) {
		String sqlString ="update  librarys_users  set `password` =?  where libraryHallCode=? and isManager='1' and isEffective='1' ";
	 	SQLQuery query = getSession().createSQLQuery(sqlString);
	 	query.setString(0, userPwd);
	 	query.setString(1, hallCode);
	 	return query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#findLibUserByHallCodeAndUserName(java.lang.String, java.lang.String)
	 */
	@Override
	public Object findLibUserByHallCodeAndUserName(String hallCode,
			String userName) {
		String sqlString ="SELECT id from librarys_users where libraryHallCode='"+hallCode+"' AND userName='"+userName+"' and isEffective=1";
	 	SQLQuery query = getSession().createSQLQuery(sqlString);
	 	 return query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.LibraryDao#findLevelByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<String> findLevelByCustomerId(Integer customerId) {
		String sqlString =" select distinct libraryLevel FROM librarys where  isEffective=1";
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_SECOND_LEVLE.equals(le)){
			sqlString +=" and customerId="+customerId;
		}
		 SQLQuery query = getSession().createSQLQuery(sqlString);
		 return query.list();
	}
    /**
     * 获取图书馆待审核的消息
     */
	@Override
	public int getLibrarysCount() {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from librarys where libraryStatus=1 or libraryUpdateStatus = 4 or libraryUpdateStatus = 6 or libraryUpdateStatus = 9");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		//进行查询
		BigInteger librarysCount = (BigInteger) query.uniqueResult();
		//返回
		return librarysCount.intValue();
	}

}
