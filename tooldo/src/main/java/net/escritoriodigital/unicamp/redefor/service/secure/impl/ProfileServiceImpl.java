/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.service.secure.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.service.secure.ProfileService;

import org.springframework.stereotype.Service;

/**
 * @author andrefabbro
 *
 */
@Service ("profileService")
public class ProfileServiceImpl extends GeneralService<Profile> implements ProfileService {

	private static final long serialVersionUID = 1740743131064703300L;

	@Override
	public Profile getProfileByName(String name) throws ServiceException {
		Profile profile = null;
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", name);
		Collection<Profile> result = findByFields(fields, false);
		
		Iterator<Profile> it = result.iterator();
		
		while(it.hasNext()) { 
			profile = it.next(); 
			break;
		}
		
		return profile;
	}

	
	
	

}
