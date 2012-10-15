package net.escritoriodigital.unicamp.redefor.bean.secure;

import net.escritoriodigital.unicamp.redefor.bean.BaseBean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("credentialBean")
public class CredentialBean extends BaseBean{
	
	
	
}
