package com.flea.common.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;

public class MyPermissionResolver extends WildcardPermissionResolver {
	@Override
	public Permission resolvePermission(String permissionString) {
		return new MyWildcardPermission(permissionString);
	}
}
