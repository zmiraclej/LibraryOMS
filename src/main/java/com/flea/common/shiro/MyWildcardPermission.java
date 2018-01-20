package com.flea.common.shiro;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

public class MyWildcardPermission extends WildcardPermission {
	public MyWildcardPermission(String permissionString) {
		super(permissionString);
	}
	@Override
	public boolean implies(Permission p) {
		 if (!(p instanceof MyWildcardPermission)) {
	            return false;
	     }
		 MyWildcardPermission wp = (MyWildcardPermission) p;
		 List<Set<String>> otherParts = wp.getParts();
		 int i = 0;
	     for (Set<String> otherPart : otherParts) {
	    	 if (getParts().size() - 1 < i) {
	                return true;
	         }else{
	        	 Set<String> part = getParts().get(i);
	             if (!part.contains(WILDCARD_TOKEN) && !part.containsAll(otherPart) && !otherPart.contains(WILDCARD_TOKEN)) {
	                    return false;
	             }
	             i++;
	         }
	     }
	     for (; i < getParts().size(); i++) {
	            Set<String> part = getParts().get(i);
	          if (!part.contains(WILDCARD_TOKEN)) {
	                return false;
	          }
	     }
		return true;
	}
	@Override
	protected List<Set<String>> getParts() {
		return super.getParts();
	}
}
